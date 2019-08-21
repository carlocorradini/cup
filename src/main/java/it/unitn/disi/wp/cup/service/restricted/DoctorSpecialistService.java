package it.unitn.disi.wp.cup.service.restricted;

import it.unitn.disi.wp.cup.config.AppConfig;
import it.unitn.disi.wp.cup.model.prescription.PrescriptionReportModel;
import it.unitn.disi.wp.cup.persistence.dao.*;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.*;
import it.unitn.disi.wp.cup.util.AuthUtil;
import it.unitn.disi.wp.cup.util.EmailUtil;
import it.unitn.disi.wp.cup.util.obj.JsonMessage;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Services for Authenticated {@link DoctorSpecialist Doctor Specialist}
 *
 * @author Carlo Corradini
 */
@Path("specialist")
public class DoctorSpecialistService {
    private static final Logger LOGGER = Logger.getLogger(DoctorSpecialistService.class.getName());

    private DoctorSpecialist doctorSpecialist = null;
    private ExamDAO examDAO = null;
    private MedicineDAO medicineDAO = null;
    private ReportDAO reportDAO = null;
    private PersonDAO personDAO = null;
    private PrescriptionExamDAO prescriptionExamDAO = null;

    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext servletContext;

    @Context
    public void setServletContext(ServletContext servletContext) {
        if (servletContext != null) {
            try {
                doctorSpecialist = AuthUtil.getAuthDoctorSpecialist(request);
                examDAO = DAOFactory.getDAOFactory(servletContext).getDAO(ExamDAO.class);
                medicineDAO = DAOFactory.getDAOFactory(servletContext).getDAO(MedicineDAO.class);
                reportDAO = DAOFactory.getDAOFactory(servletContext).getDAO(ReportDAO.class);
                personDAO = DAOFactory.getDAOFactory(servletContext).getDAO(PersonDAO.class);
                prescriptionExamDAO = DAOFactory.getDAOFactory(servletContext).getDAO(PrescriptionExamDAO.class);
            } catch (DAOFactoryException ex) {
                LOGGER.log(Level.SEVERE, "Impossible to get dao factory for storage system", ex);
            }
        }
    }

    /**
     * Given a {@link PrescriptionReportModel Report Model} add a new {@link Report report} to the corresponding
     * {@link PrescriptionExam Prescription Exam} identified.
     * The {@link Report report} will be added with all suggested {@link Exam exams} and {@link Medicine medicines}
     * if present.
     * If the process is successful create a match between the {@link Report report} and the {@link PrescriptionExam Prescription},
     * after all send an {@link EmailUtil email} to the {@link Person patient}.
     *
     * @param prescriptionReportModel The {@link PrescriptionReportModel} to add
     * @return A {@link JsonMessage message} representing the result of the process
     */
    @POST
    @Path("report")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response writeReport(PrescriptionReportModel prescriptionReportModel) {
        Response.ResponseBuilder response;
        JsonMessage message = new JsonMessage();
        PrescriptionExam prescriptionExam;
        Report report;
        Person patient;
        Exam exam;
        Medicine medicine;
        List<Exam> exams;
        List<Medicine> medicines;

        if (doctorSpecialist == null) {
            // Unauthorized Doctor Specialist
            response = Response.status(Response.Status.UNAUTHORIZED);
            message.setError(JsonMessage.ERROR_AUTHENTICATION);
        } else if (!prescriptionReportModel.isValid()) {
            // The Report is invalid
            response = Response.status(Response.Status.BAD_REQUEST);
            message.setError(JsonMessage.ERROR_VALIDATION);
        } else {
            try {
                if ((prescriptionExam = prescriptionExamDAO.getByPrimaryKey(prescriptionReportModel.getPrescriptionId())) == null) {
                    // The Prescription Id is invalid
                    response = Response.status(Response.Status.BAD_REQUEST);
                    message.setError(JsonMessage.ERROR_INVALID_ID);
                } else if (prescriptionExam.getReport() != null) {
                    // The Prescription already has a Report
                    response = Response.status(Response.Status.BAD_REQUEST);
                    message.setError(JsonMessage.ERROR_INVALID_ID);
                } else if (!doctorSpecialist.getId().equals(prescriptionExam.getSpecialistId())) {
                    // The Doctor Specialist of the Prescription is different from the current authenticated Doctor Specialist
                    response = Response.status(Response.Status.UNAUTHORIZED);
                    message.setError(JsonMessage.ERROR_AUTHENTICATION);
                } else if ((patient = personDAO.getByPrimaryKey(prescriptionExam.getPersonId())) == null) {
                    // The Patient does not exists
                    response = Response.status(Response.Status.BAD_REQUEST);
                    message.setError(JsonMessage.ERROR_INVALID_ID);
                } else {
                    // ALL CORRECT, add Report
                    report = new Report();
                    report.setContent(prescriptionReportModel.getContent());
                    // Find Exams
                    exams = new ArrayList<>();
                    for (Long examId : prescriptionReportModel.getExams()) {
                        if ((exam = examDAO.getByPrimaryKey(examId)) != null) {
                            // Exam exists
                            exams.add(exam);
                        }
                    }
                    report.setExams(exams);
                    // Find Medicines
                    medicines = new ArrayList<>();
                    for (Long medicineId : prescriptionReportModel.getMedicines()) {
                        if ((medicine = medicineDAO.getByPrimaryKey(medicineId)) != null) {
                            // Medicine Exists
                            medicines.add(medicine);
                        }
                    }
                    report.setMedicines(medicines);

                    // ADD Report & SET matching with corresponding Prescription Exam
                    report.setId(reportDAO.add(report));
                    prescriptionExam.setReport(report);

                    // UPDATE Prescription Exam
                    if (report.getId() != null && prescriptionExamDAO.update(prescriptionExam)) {
                        // Get all information available
                        report = reportDAO.getByPrimaryKey(report.getId());
                        // Updated Successfully
                        response = Response.ok();
                        message.setError(JsonMessage.ERROR_NO_ERROR);

                        // Send Email
                        String email_body = "The Report for the Prescribed Exam with id " + prescriptionExam.getId() + " is available!."
                                + "\nReport Id: " + report.getId()
                                + "\nReport Date: " + report.getDateTime().toLocalDate()
                                + "\nReport Time: " + report.getDateTime().toLocalTime()
                                + "\nSpecialist Id: " + doctorSpecialist.getId()
                                + "\nReport Content: "
                                + "\n============================"
                                + "\n" + report.getContent()
                                + "\n============================"
                                + "\nSuggested Exams: ";
                        if (report.getExams().isEmpty()) {
                            email_body += "NO EXAMS";
                        } else {
                            for (Exam e : report.getExams()) {
                                email_body += "\n\t* " + e.getName();
                            }
                        }
                        email_body += "\nSuggested Medicines: ";
                        if (report.getMedicines().isEmpty()) {
                            email_body += "NO MEDICINES";
                        } else {
                            for (Medicine m : report.getMedicines()) {
                                email_body += "\n\t* " + m.getName();
                            }
                        }

                        EmailUtil.send(patient.getEmail(), AppConfig.getName().toUpperCase() + " New Report Available", email_body);
                    } else {
                        // Error
                        response = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                        message.setError(JsonMessage.ERROR_UNKNOWN);
                    }
                }
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to add a Report", ex);
                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            }
        }

        return response.entity(message.toJsonString()).build();
    }
}
