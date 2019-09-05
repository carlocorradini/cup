package it.unitn.disi.wp.cup.service.open;

import it.unitn.disi.wp.cup.persistence.dao.MedicineDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Medicine;

import com.alibaba.fastjson.JSON;

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
 * Services for {@link Medicine}
 *
 * @author Carlo Corradini
 */
@Path("medicine")
public class MedicineService {

    private static final Logger LOGGER = Logger.getLogger(MedicineService.class.getName());
    private MedicineDAO medicineDAO = null;

    @Context
    private HttpServletRequest request;

    @Context
    public void setServletContext(ServletContext servletContext) {
        if (servletContext != null) {
            try {
                medicineDAO = DAOFactory.getDAOFactory(servletContext).getDAO(MedicineDAO.class);
            } catch (DAOFactoryException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get DAOs", ex);
            }
        }
    }

    /**
     * Return the {@link Medicine Exam} as {@link JSON} given it's {@code medicineId}
     *
     * @param medicineId The {@link Medicine} id
     * @return The {@link Medicine Medicine} given it's {@code medicineId}
     */
    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExamById(@PathParam("id") Long medicineId) {
        Response.ResponseBuilder response;
        Medicine medicine;

        if (medicineId == null) {
            // Medicine Id is missing
            response = Response.status(Response.Status.BAD_REQUEST);
        } else {
            try {
                if ((medicine = medicineDAO.getByPrimaryKey(medicineId)) == null) {
                    // Medicine Id is invalid
                    response = Response.status(Response.Status.BAD_REQUEST);
                } else {
                    // ALL CORRECT
                    response = Response
                            .ok()
                            .entity(medicine);
                }
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to return the Medicine given its id", ex);
                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            }
        }

        return response.build();
    }
}
