package it.unitn.disi.wp.cup.service.restricted;

import it.unitn.disi.wp.cup.persistence.dao.PrescriptionExamDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.*;
import it.unitn.disi.wp.cup.service.model.exception.ServiceModelException;
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
    private PrescriptionExamDAO prescriptionExamDAO = null;

    @Context
    private HttpServletRequest request;

    @Context
    public void setServletContext(ServletContext servletContext) {
        if (servletContext != null) {
            try {
                healthService = AuthUtil.getAuthHealthService(request);

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

        return response.entity(message.toJsonString()).

                build();
    }
}
