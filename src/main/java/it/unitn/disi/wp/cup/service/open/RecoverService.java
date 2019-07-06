package it.unitn.disi.wp.cup.service.open;

import it.unitn.disi.wp.cup.config.AppConfig;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.util.CryptUtil;
import it.unitn.disi.wp.cup.util.EmailUtil;
import it.unitn.disi.wp.cup.util.StringUtil;
import it.unitn.disi.wp.cup.util.obj.JsonMessage;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service for Person Password Recovery
 *
 * @author Carlo Corradini
 */
@Path("recover")
public class RecoverService {

    private static final Logger LOGGER = Logger.getLogger(RecoverService.class.getName());
    private static final int PASSWORD_LENGTH = 20;

    private PersonDAO personDAO = null;

    @Context
    public void setServletContext(ServletContext servletContext) {
        if (servletContext != null) {
            try {
                personDAO = DAOFactory.getDAOFactory(servletContext).getDAO(PersonDAO.class);
            } catch (DAOFactoryException ex) {
                LOGGER.log(Level.SEVERE, "Impossible to get dao factory for storage system", ex);
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
        JsonMessage message = new JsonMessage();
        Person person;

        if (email != null) {
            try {
                person = personDAO.getByEmail(email);
                if (person == null) {
                    message.setError(JsonMessage.ERROR_AUTHENTICATION);
                } else {
                    String newPassword = StringUtil.generateRandom(PASSWORD_LENGTH);
                    person.setPassword(CryptUtil.hashPassword(newPassword));

                    if (personDAO.update(person)) {
                        sendRecoverEmail(person, newPassword);
                        message.setError(JsonMessage.ERROR_NO_ERROR);
                    } else {
                        message.setError(JsonMessage.ERROR_UNKNOWN);
                    }
                }
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to access the persistence layer", ex);
            }
        }

        return message.toJsonString();
    }

    /**
     * Send the email with the new password to the {@code person}
     *
     * @param person      The person to send email to
     * @param newPassword The new clear password
     * @throws NullPointerException If {@code person} OR {@code newPassword} are null
     */
    private void sendRecoverEmail(Person person, String newPassword) throws NullPointerException {
        if (person == null | newPassword == null)
            throw new NullPointerException("Person and New Password are mandatory");

        EmailUtil.send(person.getEmail(),
                AppConfig.getName().toUpperCase() + " Password Recovery",
                "Your new password is:\n" + newPassword);
    }
}
