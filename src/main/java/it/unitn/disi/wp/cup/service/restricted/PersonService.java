package it.unitn.disi.wp.cup.service.restricted;

import it.unitn.disi.wp.cup.config.AppConfig;
import it.unitn.disi.wp.cup.config.AuthConfig;
import it.unitn.disi.wp.cup.persistence.dao.DoctorDAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonAvatarDAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Doctor;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.persistence.entity.PersonAvatar;
import it.unitn.disi.wp.cup.util.AuthUtil;
import it.unitn.disi.wp.cup.util.CryptUtil;
import it.unitn.disi.wp.cup.util.EmailUtil;
import it.unitn.disi.wp.cup.util.obj.JsonMessage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.Pair;
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
    private DoctorDAO doctorDAO = null;
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
                doctorDAO = DAOFactory.getDAOFactory(servletContext).getDAO(DoctorDAO.class);
                personAvatarDAO = DAOFactory.getDAOFactory(servletContext).getDAO(PersonAvatarDAO.class);
            } catch (DAOFactoryException ex) {
                LOGGER.log(Level.SEVERE, "Impossible to get dao factory for storage system", ex);
            }
        }
    }

    /**
     * Given an {@code avatarInputStream} save it and set as the new Avatar for the Authenticated Person
     * Save a Backup Image if {@link AppConfig::getConfigAvatarBackupPath Backup Path} is valid
     *
     * @param avatarInputStream The Avatar Image InputStream
     * @param avatarDetail      The Avatar Image Details
     * @return A JSON @{code {@link JsonMessage message}
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

                file = new File(FilenameUtils.separatorsToUnix(servletContext.getRealPath("/") + "assets/_default" + AppConfig.getConfigAvatarPath() + avatarNameWithExt));
                FileUtils.copyInputStreamToFile(new ByteArrayInputStream(avatar.toByteArray()), file);

                // Save a Backup file only if the @{link AppConfig::getConfigAvatarBackupPath Backup Path} is valid
                if (!AppConfig.getConfigAvatarBackupPath().isEmpty() && AppConfig.getConfigAvatarBackupPath() != null) {
                    file_backup = new File(FilenameUtils.separatorsToUnix(AppConfig.getConfigAvatarBackupPath() + avatarNameWithExt));
                    FileUtils.copyInputStreamToFile(new ByteArrayInputStream(avatar.toByteArray()), file_backup);
                }

                personAvatar.setPersonId(person.getId());
                personAvatar.setName(avatarName);
                // Add the new Avatar & set the corresponding id
                personAvatar.setId(personAvatarDAO.add(personAvatar));

                if (personAvatar.getId() != null) {
                    // Added successfully
                    // Update the Person in the Session Scope
                    person.setAvatar(personAvatar);
                    person.setAvatarHistory(personAvatarDAO.getAllByPersonId(person.getId()));
                    message.setError(JsonMessage.ERROR_NO_ERROR);
                }
            } catch (IOException | DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to change Person Avatar", ex);
            }
        }

        return message.toJsonString();
    }

    /**
     * Update the password of the Authenticated {@link Person person}
     * given the {@code oldPassword Old Password} for security check
     * and the {@code newPassword New Password} to change with
     *
     * @param oldPassword The {@link Person person} old password to check for security
     * @param newPassword The {@link Person person} new password to change with
     * @return A JSON @{code {@link JsonMessage message}
     */
    @POST
    @Path("password")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String changePassword(@FormParam("oldPassword") String oldPassword,
                                 @FormParam("newPassword") String newPassword) {
        JsonMessage message = new JsonMessage();

        if (person != null && personDAO != null && oldPassword != null && newPassword != null) {
            // Passwords exists
            try {
                if (oldPassword.length() >= AuthConfig.getPasswordMinLength()
                        && oldPassword.length() <= AuthConfig.getPasswordMaxLength()
                        && newPassword.length() >= AuthConfig.getPasswordMinLength()
                        && newPassword.length() <= AuthConfig.getPasswordMaxLength()) {
                    // Lengths are correct
                    if (CryptUtil.validate(oldPassword, person.getPassword())) {
                        // Old Password match with the current Password
                        // Change the Password with the new one
                        person.setPassword(CryptUtil.hashPassword(newPassword));
                        if (personDAO.update(person)) {
                            message.setError(JsonMessage.ERROR_NO_ERROR);
                            EmailUtil.send(person.getEmail(),
                                    AppConfig.getName().toUpperCase() + " Password Changed",
                                    "Your password has been successfully changed.\nNew Password: " + newPassword);
                        } else {
                            message.setError(JsonMessage.ERROR_UNKNOWN);
                        }
                    } else {
                        message.setError(JsonMessage.ERROR_AUTHENTICATION);
                    }
                } else {
                    message.setError(JsonMessage.ERROR_PASSWORD_LENGTH);
                }
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to change Person Password", ex);
            }
        }

        return message.toJsonString();
    }

    @POST
    @Path("doctor")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String changeDoctor(@FormParam("doctorId") Long doctorId) {
        JsonMessage message = new JsonMessage();
        Doctor doctor;
        Person doctorAsPerson;
        Pair<Long, Long> record;

        if (person != null && doctorDAO != null && doctorId != null) {
            // Doctor Id exists
            try {
                if ((doctor = doctorDAO.getByPrimaryKey(doctorId)) != null
                        && (doctorAsPerson = personDAO.getByPrimaryKey(doctorId)) != null
                        && !person.equals(doctorAsPerson)) {
                    // Doctor Exists
                    if (!doctorDAO.getHistoryByPatientId(person.getId()).contains(doctor)) {
                        // The Doctor has not been already a Doctor of the person
                        if (doctorAsPerson.getCity().getProvince().equals(person.getCity().getProvince())) {
                            // Province is the same as the Authenticated Person
                            record = doctorDAO.addPatient(doctorId, person.getId());
                            if (record != null) {
                                message.setError(JsonMessage.ERROR_NO_ERROR);
                            }
                        } else {
                            message.setError(JsonMessage.ERROR_PROVINCE);
                        }
                    } else {
                        message.setError(JsonMessage.ERROR_HISTORY);
                    }
                } else {
                    message.setError(JsonMessage.ERROR_INVALID_ID);
                }
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to change Person Doctor", ex);
            }
        }

        return message.toJsonString();
    }
}