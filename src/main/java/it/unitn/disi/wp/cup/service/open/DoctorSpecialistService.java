package it.unitn.disi.wp.cup.service.open;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import it.unitn.disi.wp.cup.persistence.dao.*;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.*;

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
 * Services for Authenticated {@link DoctorSpecialist Doctor Specialist}
 *
 * @author Carlo Corradini
 */
@Path("specialist")
public class DoctorSpecialistService {

    private static final Logger LOGGER = Logger.getLogger(DoctorSpecialistService.class.getName());
    private PersonDAO personDAO = null;
    private DoctorSpecialistDAO doctorSpecialistDAO = null;

    @Context
    private HttpServletRequest request;

    @Context
    public void setServletContext(ServletContext servletContext) {
        if (servletContext != null) {
            try {
                personDAO = DAOFactory.getDAOFactory(servletContext).getDAO(PersonDAO.class);
                doctorSpecialistDAO = DAOFactory.getDAOFactory(servletContext).getDAO(DoctorSpecialistDAO.class);
            } catch (DAOFactoryException ex) {
                LOGGER.log(Level.SEVERE, "Impossible to get dao factory for storage system", ex);
            }
        }
    }

    /**
     * Return the {@link DoctorSpecialist Doctor Specialist} as a {@link Person Person} base information as {@link JSON} given its id.
     * The password & email & more is removed for security.
     *
     * @param doctorSpecialistId The {@link DoctorSpecialist Doctor Specialist} id
     * @return The {@link HealthService Health Service} as {@link JSON}
     */
    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHealthServiceById(@PathParam("id") Long doctorSpecialistId) {
        Response.ResponseBuilder response;
        Person doctorSpecialist;
        JSONObject o;

        if (doctorSpecialistId == null) {
            // Doctor Specialist Id is missing
            response = Response.status(Response.Status.BAD_REQUEST);
        } else {
            try {
                if (doctorSpecialistDAO.getByPrimaryKey(doctorSpecialistId) == null) {
                    // Doctor Specialist Id is invalid
                    response = Response.status(Response.Status.BAD_REQUEST);
                } else {
                    // ALL CORRECT, set the Doctor Specialist entity
                    o = (JSONObject) JSON.toJSON(personDAO.getByPrimaryKey(doctorSpecialistId));
                    o.remove("password");
                    o.remove("email");
                    o.remove("medicines");
                    o.remove("exams");
                    o.remove("avatarHistory");

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
