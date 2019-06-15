package it.unitn.disi.wp.cup.service;

import com.alibaba.fastjson.JSON;
import it.unitn.disi.wp.cup.config.AppConfig;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.util.CryptUtil;
import it.unitn.disi.wp.cup.util.EmailUtil;
import it.unitn.disi.wp.cup.util.RandomStringUtil;
import it.unitn.disi.wp.cup.util.entity.JsonMessage;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service for Password Recovery
 *
 * @author Carlo Corradini
 */
@Path("recover")
public class RecoverService {

    private static final Logger LOGGER = Logger.getLogger(RecoverService.class.getName());
    private static final int PASSWORD_LENGTH = 20;

    @Context
    private HttpServletResponse response;

    private PersonDAO personDAO;

    @Context
    public void setServletContext(ServletContext servletContext) {
        if (servletContext != null) {
            DAOFactory daoFactory = (DAOFactory) servletContext.getAttribute(DAOFactory.DAO_FACTORY);
            if (daoFactory == null) {
                throw new RuntimeException(new ServletException("Impossible to get dao factory for storage system"));
            }
            try {
                personDAO = daoFactory.getDAO(PersonDAO.class);
            } catch (DAOFactoryException ex) {
                throw new RuntimeException(new ServletException("Impossible to get dao factory for user storage system", ex));
            }
        }
    }

    /**
     * Given an email, set a new password and send it via email
     *
     * @param email The email ro recover the password
     * @return A JSON @{code {@link JsonMessage message}}
     */
    @GET
    @Path("{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public String recoverPersonPassword(@PathParam("email") String email) {
        JsonMessage message = new JsonMessage(true, -1, "");
        Person person;

        if (email != null) {
            try {
                person = personDAO.getByEmail(email);
                if (person == null) {
                    message = new JsonMessage(true, 1, "1");
                } else {
                    String newPassword = RandomStringUtil.generate(PASSWORD_LENGTH);
                    person.setPassword(CryptUtil.hashPassword(newPassword));
                    if (personDAO.update(person)) {
                        EmailUtil.send(person.getEmail(),
                                AppConfig.getName().toUpperCase() + " Password Recovery",
                                "Your new password is:\n" + newPassword);
                        message = new JsonMessage(false, 0, "0");
                    }
                }
            } catch (DAOException ex) {
                try {
                    response.sendError(500, "Unable to access the persistence layer: " + ex.getMessage());
                } catch (IOException ioex) {
                    LOGGER.log(Level.SEVERE, "Unable to access the persistence layer", ioex);
                }
            }
        }

        return JSON.toJSONString(message);
    }
}
