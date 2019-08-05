package it.unitn.disi.wp.cup.service.restricted;

import it.unitn.disi.wp.cup.model.prescription.PrescriptionMedicineModel;
import it.unitn.disi.wp.cup.persistence.dao.DoctorDAO;
import it.unitn.disi.wp.cup.persistence.dao.MedicineDAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.PrescriptionMedicineDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Doctor;
import it.unitn.disi.wp.cup.persistence.entity.Medicine;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.persistence.entity.PrescriptionMedicine;
import it.unitn.disi.wp.cup.util.AuthUtil;
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
 */
@Path("doctor")
public class DoctorService {
    private static final Logger LOGGER = Logger.getLogger(DoctorService.class.getName());

    private Doctor doctor = null;
    private DoctorDAO doctorDAO = null;
    private PersonDAO personDAO = null;
    private MedicineDAO medicineDAO = null;
    private PrescriptionMedicineDAO prescriptionMedicineDAO = null;

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
                prescriptionMedicineDAO = DAOFactory.getDAOFactory(servletContext).getDAO(PrescriptionMedicineDAO.class);
            } catch (DAOFactoryException ex) {
                LOGGER.log(Level.SEVERE, "Impossible to get dao factory for storage system", ex);
            }
        }
    }

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
                    }
                }
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to Prescribe a Medicine", ex);
            }
        }

        return message.toJsonString();
    }
}
