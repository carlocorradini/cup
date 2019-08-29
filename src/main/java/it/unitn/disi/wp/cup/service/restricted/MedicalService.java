package it.unitn.disi.wp.cup.service.restricted;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import it.unitn.disi.wp.cup.persistence.dao.HealthServiceDAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.PrescriptionExamDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.*;
import it.unitn.disi.wp.cup.util.AuthUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Services for Authenticated {@link Doctor Doctor} and {@link DoctorSpecialist Doctor Specialist}
 *
 * @author Carlo Corradini
 */
@Path("medical")
public class MedicalService {
    private static final Logger LOGGER = Logger.getLogger(MedicalService.class.getName());

    private Doctor authDoctor = null;
    private DoctorSpecialist authDoctorSpecialist = null;
    private PersonDAO personDAO = null;
    private PrescriptionExamDAO prescriptionExamDAO = null;

    @Context
    private HttpServletRequest request;

    @Context
    public void setServletContext(ServletContext servletContext) {
        if (servletContext != null) {
            try {
                authDoctor = AuthUtil.getAuthDoctor(request);
                authDoctorSpecialist = AuthUtil.getAuthDoctorSpecialist(request);

                personDAO = DAOFactory.getDAOFactory(servletContext).getDAO(PersonDAO.class);
                prescriptionExamDAO = DAOFactory.getDAOFactory(servletContext).getDAO(PrescriptionExamDAO.class);
            } catch (DAOFactoryException ex) {
                LOGGER.log(Level.SEVERE, "Impossible to get dao factory for storage system", ex);
            }
        }
    }

    private boolean isAuthenticated() {
        return authDoctor != null || authDoctorSpecialist != null;
    }

    /**
     * Return the {@link PrescriptionExam Prescription Exam} as {@link JSON} given its id
     *
     * @param prescriptionId The {@link PrescriptionExam Prescription Exam} id
     * @return The {@link PrescriptionExam Prescription Exam} as {@link JSON}
     */
    @GET
    @Path("prescription_exam/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrescriptionExamById(@PathParam("id") Long prescriptionId) {
        Response.ResponseBuilder response;
        PrescriptionExam prescriptionExam;

        if (!isAuthenticated()) {
            // Unauthorized
            response = Response.status(Response.Status.UNAUTHORIZED);
        } else if (prescriptionId == null) {
            // Prescription Id is missing
            response = Response.status(Response.Status.BAD_REQUEST);
        } else {
            try {
                if ((prescriptionExam = prescriptionExamDAO.getByPrimaryKey(prescriptionId)) == null) {
                    // Prescription Id is invalid
                    response = Response.status(Response.Status.BAD_REQUEST);
                } else {
                    // ALL CORRECT, set the Prescription Exam entity
                    response = Response
                            .ok()
                            .entity(JSON.toJSONString(prescriptionExam));
                }
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to return the Prescription Exam given its id", ex);
                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            }
        }

        return response.build();
    }

    /**
     * Return the {@link Person patient} as {@link JSON} given its id.
     * It's available only for authenticated and qualified users.
     * The password & email is removed for security.
     *
     * @param patientId The {@link Person id}
     * @return The {@link Person person} as {@link JSON}
     */
    @GET
    @Path("patient/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPatientById(@PathParam("id") Long patientId) {
        Response.ResponseBuilder response;
        Person patient;
        JSONObject o;

        if (!isAuthenticated()) {
            // Unauthorized
            response = Response.status(Response.Status.UNAUTHORIZED);
        } else if (patientId == null) {
            // Patient Id is missing
            response = Response.status(Response.Status.BAD_REQUEST);
        } else {
            try {
                if ((patient = personDAO.getByPrimaryKey(patientId)) == null) {
                    // Patient Id is invalid
                    response = Response.status(Response.Status.BAD_REQUEST);
                } else {
                    // ALL CORRECT, set the Person entity
                    // Remove password
                    o = (JSONObject) JSON.toJSON(patient);
                    o.remove("password");
                    o.remove("email");

                    response = Response
                            .ok()
                            .entity(o.toJSONString());
                }
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to return the Patient given its id", ex);
                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            }
        }

        return response.build();
    }
}
