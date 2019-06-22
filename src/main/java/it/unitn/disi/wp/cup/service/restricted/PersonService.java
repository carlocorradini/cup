package it.unitn.disi.wp.cup.service.restricted;

import it.unitn.disi.wp.cup.config.AppConfig;
import it.unitn.disi.wp.cup.persistence.dao.PersonAvatarDAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.persistence.entity.PersonAvatar;
import it.unitn.disi.wp.cup.util.AuthUtil;
import it.unitn.disi.wp.cup.util.obj.JsonMessage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Services for Authenticated Person
 *
 * @author Carlo Corradini
 */
@Path("person")
public class PersonService {

    private static final Logger LOGGER = Logger.getLogger(PersonService.class.getName());

    private Person person = null;
    private PersonDAO personDAO = null;
    private PersonAvatarDAO personAvatarDAO = null;

    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext servletContext;

    @Context
    public void setServletContext(ServletContext servletContext) {
        if (servletContext != null) {
            try {
                person = AuthUtil.getAuthPerson(request);
                personDAO = DAOFactory.getDAOFactory(servletContext).getDAO(PersonDAO.class);
                personAvatarDAO = DAOFactory.getDAOFactory(servletContext).getDAO(PersonAvatarDAO.class);
            } catch (DAOFactoryException ex) {
                LOGGER.log(Level.SEVERE, "Impossible to get dao factory for storage system", ex);
            }
        }
    }

    /**
     * Given an {@code avatarInputStream} save it and set as the new Avatar for the Authenticated Person
     * Save a Backup Image
     *
     * @param avatarInputStream The Avatar Image InputStream
     * @param avatarDetail      The Avatar Image Details
     * @return A JSON @{code {@link JsonMessage message}}
     */
    @POST
    @Path("avatar")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String changeAvatar(
            @DefaultValue("true") @FormDataParam("enabled") boolean enabled,
            @FormDataParam("avatar") InputStream avatarInputStream,
            @FormDataParam("avatar") FormDataContentDisposition avatarDetail) {

        JsonMessage message = new JsonMessage();
        ByteArrayOutputStream avatar = new ByteArrayOutputStream();

        if (avatarInputStream != null && avatarDetail != null && person != null && personAvatarDAO != null) {
            try {
                BufferedImage bufferedImage = Scalr.resize(ImageIO.read(avatarInputStream), Scalr.Method.ULTRA_QUALITY, AppConfig.getConfigAvatarResizeSize(), Scalr.OP_ANTIALIAS);
                ImageIO.write(bufferedImage, AppConfig.getConfigAvatarExtension().substring(1), avatar);

                PersonAvatar personAvatar = new PersonAvatar();
                File file;
                File file_backup;

                String avatarName = String.format("%d_%s", person.getId(), new SimpleDateFormat("dd-MM-yy_HH-mm-ss").format(new Date()));
                String avatarNameWithExt = avatarName + AppConfig.getConfigAvatarExtension();

                file = new File(FilenameUtils.separatorsToUnix(servletContext.getRealPath("/") + "assets/default/1_0" + AppConfig.getConfigAvatarPath() + "/"
                        + avatarNameWithExt));
                file_backup = new File(FilenameUtils.separatorsToUnix(AppConfig.getConfigAvatarBackupPath() + "/" + avatarNameWithExt));

                personAvatar.setPersonId(person.getId());
                personAvatar.setName(avatarName);

                FileUtils.copyInputStreamToFile(new ByteArrayInputStream(avatar.toByteArray()), file);
                FileUtils.copyInputStreamToFile(new ByteArrayInputStream(avatar.toByteArray()), file_backup);

                personAvatar.setId(personAvatarDAO.insert(personAvatar));

                if (personAvatar.getId() != null) {
                    person.setAvatar(personAvatar);
                    if (personDAO.update(person)) {
                        // I Need to Update the Person in the Session Scope
                        person.setAvatarHistory(personAvatarDAO.getAllByPersonId(person.getId()));
                        message.setError(JsonMessage.ERROR_NO_ERROR);
                    }
                }
            } catch (IOException | DAOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }

        return message.toJsonString();
    }
}
