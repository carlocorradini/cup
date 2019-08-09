package it.unitn.disi.wp.cup.service.restricted;

import it.unitn.disi.wp.cup.config.AppConfig;
import it.unitn.disi.wp.cup.model.prescription.PrescriptionExamModel;
import it.unitn.disi.wp.cup.model.prescription.PrescriptionMedicineModel;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Services for Authenticated Doctor
 *
 * @author Carlo Corradini
 */
@Path("doctor")
public class DoctorService {
    private static final Logger LOGGER = Logger.getLogger(DoctorService.class.getName());

    private Doctor doctor = null;
    private DoctorDAO doctorDAO = null;
    private PersonDAO personDAO = null;
    private MedicineDAO medicineDAO = null;
    private ExamDAO examDAO = null;
    private PrescriptionMedicineDAO prescriptionMedicineDAO = null;
    private PrescriptionExamDAO prescriptionExamDAO = null;

    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext servletContext;

    @Context
    public void setServletContext(ServletContext servletContext) {
        if (servletContext != null) {
            try {
                doctor = AuthUtil.getAuthDoctor(request);
                doctorDAO = DAOFactory.getDAOFactory(servletContext).getDAO(DoctorDAO.class);
                personDAO = DAOFactory.getDAOFactory(servletContext).getDAO(PersonDAO.class);
                medicineDAO = DAOFactory.getDAOFactory(servletContext).getDAO(MedicineDAO.class);
                examDAO = DAOFactory.getDAOFactory(servletContext).getDAO(ExamDAO.class);
                prescriptionMedicineDAO = DAOFactory.getDAOFactory(servletContext).getDAO(PrescriptionMedicineDAO.class);
                prescriptionExamDAO = DAOFactory.getDAOFactory(servletContext).getDAO(PrescriptionExamDAO.class);
            } catch (DAOFactoryException ex) {
                LOGGER.log(Level.SEVERE, "Impossible to get dao factory for storage system", ex);
            }
        }
    }

    /**
     * Given a valid {@link PrescriptionMedicineModel} and the Authenticated {@link Doctor doctor}
     * add the Prescription Medicine to the Persistence System
     *
     * @param prescriptionMedicineModel The Medicine Prescription Model to add
     * @return A JSON @{code {@link JsonMessage message}}
     */
    @POST
    @Path("prescription_medicine")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String prescriptionMedicine(PrescriptionMedicineModel prescriptionMedicineModel) {
        JsonMessage message = new JsonMessage();
        PrescriptionMedicine prescriptionMedicine = new PrescriptionMedicine();
        Person patient;
        Medicine medicine;

        if (doctor != null && prescriptionMedicineModel.isValid()) {
            try {
                if (doctor.equals(doctorDAO.getDoctorByPatientId(prescriptionMedicineModel.getPatientId()))
                        && (patient = personDAO.getByPrimaryKey(prescriptionMedicineModel.getPatientId())) != null
                        && (medicine = medicineDAO.getByPrimaryKey(prescriptionMedicineModel.getMedicineId())) != null) {
                    // The Authenticated Doctor is the Doctor of the Patient
                    //  and The Patient exists
                    //  and The Medicine exists
                    prescriptionMedicine.setPersonId(patient.getId());
                    prescriptionMedicine.setDoctorId(doctor.getId());
                    prescriptionMedicine.setMedicine(medicine);
                    prescriptionMedicine.setQuantity(prescriptionMedicineModel.getQuantity());

                    // Add the new Prescription Medicine
                    if (prescriptionMedicineDAO.add(prescriptionMedicine) != null) {
                        // Added successfully
                        message.setError(JsonMessage.ERROR_NO_ERROR);
                        EmailUtil.send(patient.getEmail(),
                                AppConfig.getName().toUpperCase() + " Prescription Medicine",
                                "A new Medicine Prescription has been added.\nMedicine: " + medicine.getName() + "\nQuantity: " + prescriptionMedicineModel.getQuantity());
                    }
                }
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to Prescribe a Medicine", ex);
            }
        }

        return message.toJsonString();
    }

    /**
     * Given a valid {@link PrescriptionExamModel} and the Authenticated {@link Doctor doctor}
     * add the Prescription Exam to the Persistence System
     *
     * @param prescriptionExamModel The Exam Prescription Model to add
     * @return A JSON @{code {@link JsonMessage message}}
     */
    @POST
    @Path("prescription_exam")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String prescriptionExam(PrescriptionExamModel prescriptionExamModel) {
        JsonMessage message = new JsonMessage();
        PrescriptionExam prescriptionExam = new PrescriptionExam();
        Person patient;
        Exam exam;

        if (doctor != null && prescriptionExamModel.isValid()) {
            try {
                if (doctor.equals(doctorDAO.getDoctorByPatientId(prescriptionExamModel.getPatientId()))
                        && (patient = personDAO.getByPrimaryKey(prescriptionExamModel.getPatientId())) != null
                        && (exam = examDAO.getByPrimaryKey(prescriptionExamModel.getExamId())) != null) {
                    // The Authenticated Doctor is the Doctor of the Patient
                    //  and The Patient exists
                    //  and The Exam exists
                    prescriptionExam.setPersonId(patient.getId());
                    prescriptionExam.setDoctorId(doctor.getId());
                    prescriptionExam.setExam(exam);

                    // Add the new Prescription Exam
                    if (prescriptionExamDAO.add(prescriptionExam) != null) {
                        // Added successfully
                        message.setError(JsonMessage.ERROR_NO_ERROR);
                        EmailUtil.send(patient.getEmail(),
                                AppConfig.getName().toUpperCase() + " Prescription Exam",
                                "A new Exam has been added.\nExam: " + exam.getName() + "\nPlease contact us at " + AppConfig.getInfoPhone() + " to arrange a meeting.");
                    }
                }
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to Prescribe an Exam", ex);
            }
        }

        return message.toJsonString();
    }
}
