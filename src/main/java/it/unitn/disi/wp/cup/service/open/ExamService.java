package it.unitn.disi.wp.cup.service.open;

import it.unitn.disi.wp.cup.persistence.dao.ExamDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Exam;

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
 * Services for {@link Exam}
 *
 * @author Carlo Corradini
 */
@Path("exam")
public class ExamService {

    private static final Logger LOGGER = Logger.getLogger(ExamService.class.getName());
    private ExamDAO examDAO = null;

    @Context
    private HttpServletRequest request;

    @Context
    public void setServletContext(ServletContext servletContext) {
        if (servletContext != null) {
            try {
                examDAO = DAOFactory.getDAOFactory(servletContext).getDAO(ExamDAO.class);
            } catch (DAOFactoryException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get DAOs", ex);
            }
        }
    }

    /**
     * Return the {@link Exam Exam} as {@link JSON} given it's {@code examId}
     *
     * @param examId The {@link Exam} id
     * @return The {@link Exam Exam} given it's {@code examId}
     */
    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExamById(@PathParam("id") Long examId) {
        Response.ResponseBuilder response;
        Exam exam;

        if (examId == null) {
            // Exam Id is missing
            response = Response.status(Response.Status.BAD_REQUEST);
        } else {
            try {
                if ((exam = examDAO.getByPrimaryKey(examId)) == null) {
                    // Exam Id is invalid
                    response = Response.status(Response.Status.BAD_REQUEST);
                } else {
                    // ALL CORRECT
                    response = Response
                            .ok()
                            .entity(exam);
                }
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to return the Exam given its id", ex);
                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            }
        }

        return response.build();
    }
}
