package it.unitn.disi.wp.cup.service.open;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.service.model.health_service.CredentialsModel;
import it.unitn.disi.wp.cup.persistence.dao.HealthServiceDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.HealthService;
import it.unitn.disi.wp.cup.util.AuthUtil;
import it.unitn.disi.wp.cup.util.obj.JsonMessage;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
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
    private HttpServletRequest request;

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
            message.setError(JsonMessage.ERROR_VALIDATION);
        } else if (AuthUtil.getAuthPerson(request) != null || AuthUtil.getAuthHealthService(request) != null) {
            // Already signed in with other accounts
            message.setError(JsonMessage.ERROR_SESSION);
        } else {
            // ALL CORRECT, try to sign in
            healthService = AuthUtil.signInHealthService(credentialsModel.getId(), credentialsModel.getPassword(), credentialsModel.isRemember(), request);

            if (healthService == null) {
                message.setError(JsonMessage.ERROR_AUTHENTICATION);
            } else {
                // SIGNED IN
                message.setError(JsonMessage.ERROR_NO_ERROR);
            }
        }

        return Response.ok().entity(message.toJsonString()).build();
    }

    /**
     * Return the {@link HealthService Health Service} as {@link JSON} given its id.
     * The password & email is removed for security.
     *
     * @param healthServiceId The {@link HealthService id}
     * @return The {@link HealthService Health Service} as {@link JSON}
     */
    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHealthServiceById(@PathParam("id") Long healthServiceId) {
        Response.ResponseBuilder response;
        HealthService healthService;
        JSONObject o;

        if (healthServiceId == null) {
            // Health Service Id is missing
            response = Response.status(Response.Status.BAD_REQUEST);
        } else {
            try {
                if ((healthService = healthServiceDAO.getByPrimaryKey(healthServiceId)) == null) {
                    // Health Service Id is invalid
                    response = Response.status(Response.Status.BAD_REQUEST);
                } else {
                    // ALL CORRECT, set the Health Service entity
                    // Remove password
                    o = (JSONObject) JSON.toJSON(healthService);
                    o.remove("password");
                    o.remove("email");

                    response = Response
                            .ok()
                            .entity(o.toJSONString());
                }
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to return the Health Service given its id", ex);
                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            }
        }

        return response.build();
    }

}
