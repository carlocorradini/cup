package it.unitn.disi.wp.cup.service.open;

import it.unitn.disi.wp.cup.model.health_service.CredentialsModel;
import it.unitn.disi.wp.cup.persistence.dao.HealthServiceDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.HealthService;
import it.unitn.disi.wp.cup.util.obj.JsonMessage;

import javax.servlet.ServletContext;
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
    private HealthServiceDAO healthServiceDAO = null;

    @Context
    public void setServletContext(ServletContext servletContext) {
        if (servletContext != null) {
            try {
                healthServiceDAO = DAOFactory.getDAOFactory(servletContext).getDAO(HealthServiceDAO.class);
            } catch (DAOFactoryException ex) {
                LOGGER.log(Level.SEVERE, "Impossible to get dao factory for storage system", ex);
            }
        }
    }

    /**
     * Authenticate the {@link HealthService Health Service} given id and password
     *
     * @param credentialsModel The {@link CredentialsModel credentials model} for authenticating
     *                         the {@link HealthService Health Service}
     * @return A {@link Response response} representing the authenticating status
     */
    @POST
    @Path("signin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signIn(CredentialsModel credentialsModel) {
        Response.ResponseBuilder response;
        JsonMessage message = new JsonMessage();
        HealthService healthService;

        if (!credentialsModel.isValid()) {
            // The Model is invalid
            response = Response.status(Response.Status.BAD_REQUEST);
            message.setError(JsonMessage.ERROR_INVALID_ID);
        } else {
            response = Response.ok();
        }

        return response.entity(message.toJsonString()).build();
    }

}
