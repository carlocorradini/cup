package it.unitn.disi.wp.cup.service.open;

import com.alibaba.fastjson.JSON;
import it.unitn.disi.wp.cup.persistence.dao.*;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.*;
import it.unitn.disi.wp.cup.util.EntitySanitizerUtil;

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
 * Services for Authenticated {@link Doctor Doctor}
 *
 * @author Carlo Corradini
 */
@Path("doctor")
public class DoctorService {

    private static final Logger LOGGER = Logger.getLogger(DoctorService.class.getName());
    private PersonDAO personDAO = null;
    private DoctorDAO doctorDAO = null;

    @Context
    private HttpServletRequest request;

    @Context
    public void setServletContext(ServletContext servletContext) {
        if (servletContext != null) {
            try {
                personDAO = DAOFactory.getDAOFactory(servletContext).getDAO(PersonDAO.class);
                doctorDAO = DAOFactory.getDAOFactory(servletContext).getDAO(DoctorDAO.class);
            } catch (DAOFactoryException ex) {
                LOGGER.log(Level.SEVERE, "Impossible to get dao factory for storage system", ex);
            }
        }
    }

    /**
     * Return the {@link Doctor Doctor} as a {@link Person Person} base information as {@link JSON} given its id.
     * The entity returned is Sanitized: {@link EntitySanitizerUtil#sanitizePerson(Person)}
     *
     * @param doctorId The {@link Doctor Doctor} id
     * @return The {@link Doctor Doctor} as {@link JSON}
     */
    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDoctorById(@PathParam("id") Long doctorId) {
        Response.ResponseBuilder response;
        Person doctor;

        if (doctorId == null) {
            // Doctor Id is missing
            response = Response.status(Response.Status.BAD_REQUEST);
        } else {
            try {
                if (doctorDAO.getByPrimaryKey(doctorId) == null
                        || (doctor = personDAO.getByPrimaryKey(doctorId)) == null) {
                    // Doctor Id is invalid
                    response = Response.status(Response.Status.BAD_REQUEST);
                } else {
                    // ALL CORRECT, set the Doctor entity sanitized
                    response = Response
                            .ok()
                            .entity(EntitySanitizerUtil.sanitizePerson(doctor));
                }
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to return the Doctor given its id", ex);
                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            }
        }

        return response.build();
    }
}
