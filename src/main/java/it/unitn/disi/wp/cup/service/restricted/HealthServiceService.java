package it.unitn.disi.wp.cup.service.restricted;

import it.unitn.disi.wp.cup.persistence.dao.DoctorSpecialistDAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.PrescriptionExamDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.*;
import it.unitn.disi.wp.cup.service.model.exception.ServiceModelException;
import it.unitn.disi.wp.cup.service.model.health_service.AssignPrescriptionExamModel;
import it.unitn.disi.wp.cup.service.model.prescription.PrescriptionReportModel;
import it.unitn.disi.wp.cup.util.AuthUtil;
import it.unitn.disi.wp.cup.util.EmailUtil;
import it.unitn.disi.wp.cup.util.WriteReportUtil;
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
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service for {@link HealthServiceService}
 *
 * @author Carlo Corradini
 */
@Path("health_service")
public class HealthServiceService {
    private static final Logger LOGGER = Logger.getLogger(HealthServiceService.class.getName());

    private HealthService healthService = null;
    private PersonDAO personDAO = null;
    private PrescriptionExamDAO prescriptionExamDAO = null;
    private DoctorSpecialistDAO doctorSpecialistDAO = null;

    @Context
    private HttpServletRequest request;

    @Context
    public void setServletContext(ServletContext servletContext) {
        if (servletContext != null) {
            try {
                healthService = AuthUtil.getAuthHealthService(request);

                personDAO = DAOFactory.getDAOFactory(servletContext).getDAO(PersonDAO.class);
                prescriptionExamDAO = DAOFactory.getDAOFactory(servletContext).getDAO(PrescriptionExamDAO.class);
                doctorSpecialistDAO = DAOFactory.getDAOFactory(servletContext).getDAO(DoctorSpecialistDAO.class);
            } catch (DAOFactoryException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get DAOs", ex);
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

        if (healthService == null) {
            // Unauthorized Health Service
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
                } else if (!healthService.getId().equals(prescriptionExam.getHealthServiceId())) {
                    // The Health Service of the Prescription is different from the current authenticated Health Service
                    response = Response.status(Response.Status.UNAUTHORIZED);
                    message.setError(JsonMessage.ERROR_AUTHENTICATION);
                } else if (!WriteReportUtil.write(prescriptionReportModel)) {
                    response = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                    message.setError(JsonMessage.ERROR_UNKNOWN);
                } else {
                    response = Response.status(Response.Status.OK);
                    message.setError(JsonMessage.ERROR_NO_ERROR);
                }
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to add a Report", ex);
                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                message.setError(JsonMessage.ERROR_UNKNOWN);
            } catch (ServiceModelException ex) {
                LOGGER.log(Level.SEVERE, "Report Model Error Validation", ex);
                response = Response.status(Response.Status.OK);
                message.setError(JsonMessage.ERROR_VALIDATION);
            }
        }

        return response.entity(message.toJsonString()).build();
    }

    /**
     * Given an {@link AssignPrescriptionExamModel Assign Prescritpion Exam Model} update the corresponding
     * {@link PrescriptionExam Prescription Exam} identified by {@code assignModel.prescriptionId} with
     * an Executor chosen from {@link HealthService Health Service} or {@link DoctorSpecialist Doctor Specialist}
     * depending on the {@link Exam Exam} support.
     * The {@link LocalDateTime Date and Time} of the {@link PrescriptionExam Prescritpion Exam} must be valid
     * and after the current {@link LocalDateTime Date and Time}.
     *
     * @param assignModel The {@link AssignPrescriptionExamModel} to identify and update the {@link PrescriptionExam}
     * @return A {@link JsonMessage message} representing the result of the process
     */
    @POST
    @Path("assign")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response assignPrescriptionExam(AssignPrescriptionExamModel assignModel) {
        Response.ResponseBuilder response;
        JsonMessage message = new JsonMessage();
        Person patient;
        Person doctorSpecialistAsPerson = null;
        PrescriptionExam prescription;
        DoctorSpecialist doctorSpecialist = null;

        if (healthService == null) {
            // Unauthorized Health Service
            response = Response.status(Response.Status.UNAUTHORIZED);
            message.setError(JsonMessage.ERROR_AUTHENTICATION);
        } else if (!assignModel.isValid()) {
            // The Assign Model is invalid
            response = Response.status(Response.Status.BAD_REQUEST);
            message.setError(JsonMessage.ERROR_VALIDATION);
        } else {
            try {
                if ((prescription = prescriptionExamDAO.getByPrimaryKey(assignModel.getPrescriptionId())) == null) {
                    // The Prescription Id is invalid
                    response = Response.status(Response.Status.BAD_REQUEST);
                    message.setError(JsonMessage.ERROR_INVALID_ID);
                } else if (prescription.getReport() != null || prescription.getHealthServiceId() != null || prescription.getSpecialistId() != null) {
                    // The Prescription is already assigned
                    response = Response.status(Response.Status.BAD_REQUEST);
                    message.setError(JsonMessage.ERROR_VALIDATION);
                } else if (!assignModel.getDateTime().toLocalDateTime().isAfter(LocalDateTime.now())) {
                    // The Date and Time of the Prescription are before than the current Date and Time
                    response = Response.status(Response.Status.BAD_REQUEST);
                    message.setError(JsonMessage.ERROR_VALIDATION);
                } else if ((patient = personDAO.getByPrimaryKey(prescription.getPersonId())) == null
                        || !patient.getCity().getProvince().equals(healthService.getProvince())) {
                    // The Patient of the Prescription cannot be applied with the current Health Service
                    response = Response.status(Response.Status.BAD_REQUEST);
                    message.setError(JsonMessage.ERROR_INVALID_ID);
                } else if (!prescription.getExam().isSupported()
                        && (doctorSpecialist = doctorSpecialistDAO.getByPrimaryKey(assignModel.getExecutorId())) == null) {
                    // The Prescription Exam is not supported and there is no Doctor Specialist
                    response = Response.status(Response.Status.BAD_REQUEST);
                    message.setError(JsonMessage.ERROR_INVALID_ID);
                } else if (!prescription.getExam().isSupported()
                        && doctorSpecialist != null
                        && (doctorSpecialistAsPerson = personDAO.getByPrimaryKey(doctorSpecialist.getId())) != null
                        && !doctorSpecialistDAO.getAllQualifiedbyExamId(prescription.getExam().getId()).contains(doctorSpecialistAsPerson)) {
                    // The Prescription Exam is not supported and the Doctor Specialist chosen is not qualified for the Exam
                    response = Response.status(Response.Status.BAD_REQUEST);
                    message.setError(JsonMessage.ERROR_INVALID_ID);
                } else {
                    // ALL CORRECT
                    // Set the correct Prescription Exam data
                    prescription.setDateTime(assignModel.getDateTime().toLocalDateTime());
                    if (!prescription.getExam().isSupported() && doctorSpecialist != null) {
                        // Doctor Specialist Executor
                        prescription.setSpecialistId(doctorSpecialist.getId());
                    } else {
                        // Health Service Executor
                        prescription.setHealthServiceId(healthService.getId());
                    }

                    // Update
                    if (!prescriptionExamDAO.update(prescription)) {
                        response = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                        message.setError(JsonMessage.ERROR_UNKNOWN);
                    } else {
                        response = Response.status(Response.Status.OK);
                        message.setError(JsonMessage.ERROR_NO_ERROR);

                        // Set the string based on the fact that is payed or not
                        String strPagamento = "Le ricordiamo inoltre che il suo esame <b>non</b> è ancora stato pagato.<br>";
                        if (prescription.getPaid()) {
                            strPagamento = "";
                        }

                        if (!prescription.getExam().isSupported() && doctorSpecialist != null && doctorSpecialistAsPerson != null) {
                            // Exam assigned to a doctorSpecialist
                            // Generate the string for the patient email
                            String htmlPatient =
                                    "<h1 style=\"color: #5e9ca0;\">Assegnamento <span style=\"color: #2b2301;\">esame</span>!</h1>" +
                                    "<p>" +
                                        "Ciao <span style=\"color: #2b2301;\"><b>" + patient.getName() + "</b></span>!<br>" +
                                        "Il tuo esame n° <b>" + prescription.getExam().getId() + "</b>, registrato in data <b>" + prescription.getDateTimeRegistration() + "</b>, è stato assegnato al medico specialista <b>" + doctorSpecialistAsPerson.getFullName() + "</b>(id: " + doctorSpecialist.getId() + ").<br>" +
                                        "L'esame si terrà il <b>" + prescription.getDateTime() + "</b>.<br>" +
                                        strPagamento +
                                        "Per info e necessità, non esitare a contattarci!<br>" +
                                    "</p>";

                            // Generate the string for the doctorSpecialist email
                            String htmlDoctor_Specialist =
                                    "<h1 style=\"color: #5e9ca0;\">Nuovo <span style=\"color: #2b2301;\">esame</span>!</h1>" +
                                    "<p>" +
                                        "Ciao <span style=\"color: #2b2301;\"><b>" + doctorSpecialistAsPerson.getName() + "</b></span>!<br>" +
                                        "Ti è stato assegnato l'esame n° <b>" + prescription.getExam().getId() + "</b>.<br>" +
                                        "<br>" +
                                        "Ecco un po' di informazioni a riguardo:<br>" +
                                        "<h3><b>Paziente</b>:</h3><br>" +
                                            "<b>Nome</b>: " + patient.getName() + "<br>" +
                                            "<b>Cognome</b>: " + patient.getSurname() + "<br>" +
                                            "<b>Codice Fiscale</b>: " + patient.getFiscalCode() + "<br>" +
                                            "<b>Sesso</b>: " + patient.getSex() + "<br>" +
                                        "<h3><b>Esame</b>:</h3><br>" +
                                            "<b>Nome</b>: " + prescription.getExam().getName() + "<br>" +
                                            "<b>Descrizione</b>: " + prescription.getExam().getDescription() + "<br>" +
                                        "Per saperne di più, visita il tuo profilo ed entra nella pagina dedicata.<br>" +
                                    "</p>";

                            // Send Emails
                            EmailUtil.sendHTML(patient.getEmail(), "assegnamento esame n° " + prescription.getId(), htmlPatient);
                            EmailUtil.sendHTML(doctorSpecialistAsPerson.getEmail(), "assegnamento esame n° " + prescription.getId(), htmlDoctor_Specialist);
                        } else if (prescription.getExam().isSupported() && healthService != null) {
                            // HEALTH SERVICE
                            // Generate the string for the patient email
                            String htmlPatient =
                                    "<h1 style=\"color: #5e9ca0;\">Assegnamento <span style=\"color: #2b2301;\">esame</span>!</h1>" +
                                    "<p>" +
                                        "Ciao <span style=\"color: #2b2301;\"><b>" + patient.getName() + "</b></span>!<br>" +
                                        "Il tuo esame n° <b>" + prescription.getExam().getId() + "</b>, registrato in data <b>" + prescription.getDateTimeRegistration() + "</b>, è stato assegnato al medico specialista <b>" + doctorSpecialistAsPerson.getFullName() + "</b>(id: " + doctorSpecialist.getId() + ").<br>" +
                                        "L'esame si terrà il <b>" + prescription.getDateTime() + "</b>.<br>" +
                                        strPagamento +
                                        "Per info e necessità, non esitare a contattarci!<br>" +
                                    "</p>";

                            // Send Emails
                            EmailUtil.sendHTML(patient.getEmail(), "assegnamento esame n° " + prescription.getId(), htmlPatient);
                        } else {
                            // ERRORE
                        }
                    }
                }
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to assign a Prescription Exam", ex);
                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                message.setError(JsonMessage.ERROR_UNKNOWN);
            }
        }

        return response.entity(message).build();
    }
}
