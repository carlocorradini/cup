package it.unitn.disi.wp.cup.util;

import it.unitn.disi.wp.cup.config.EmailConfig;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Email Utility class for Sending email
 *
 * @author Carlo Corradini
 */
public final class EmailUtil {

    private static final Logger LOGGER = Logger.getLogger(EmailUtil.class.getName());
    private static Session session;

    /**
     * Configure the Utility with the loaded Property
     */
    public static void configure() {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", EmailConfig.getSmtpHost());
        properties.setProperty("mail.smtp.port", Integer.toString(EmailConfig.getSmtpPort()));
        properties.setProperty("mail.smtp.socketFactory.port", Integer.toString(EmailConfig.getSmtpPort()));
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.auth", Boolean.toString(EmailConfig.getSmtpAuth()));
        properties.setProperty("mail.smtp.starttls.enable", Boolean.toString(EmailConfig.getSmtpTls()));
        properties.setProperty("mail.debug", Boolean.toString(EmailConfig.getDebug()));

        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EmailConfig.getUsername(), EmailConfig.getPassword());
            }
        });
    }

    /**
     * Send a simple mail
     *
     * @param recipient The email of the recipient
     * @param subject   The subject of the email to send
     * @param text      The body of the email to send
     */
    public static void send(String recipient, String subject, String text) {
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(EmailConfig.getUsername()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject, "UTF-8");
            message.setText(text, "UTF-8");
            message.setSentDate(new Date());

            Transport.send(message);
        } catch (MessagingException ex) {
            LOGGER.log(Level.SEVERE, "Unable to send email", ex);
        }
    }

    /**
     * Send an HTML mail
     *
     * @param recipient The email of the recipient
     * @param subject   The subject of the email to send
     * @param text      The html code of the mail
     */
    public static void sendHTML(String recipient, String subject, String text) {
        MimeMessage message = new MimeMessage(session);

        /* EMAIL DI PROVA:
            <h1 style="color: #5e9ca0;">Nuova prescrizione <span style="color: #2b2301;">medicinali</span>!</h1>
            <p>Buongiorno sig. <b>Pippo</b>,<br /> la informiamo che il medico <strong>Gino</strong> ha aggiunto una prescrizione per lei.</p>
            <h2 style="color: #2e6c80;">Medicinali:</h2>
            <ul>
            <li style="clear: both;">Protein C</li>
            <li style="clear: both;">Ranexa</li>
            <li style="clear: both;">Propecia</li>
            </ul>
         */

        try {
            message.setFrom(new InternetAddress(EmailConfig.getUsername()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject, "UTF-8");
            message.setSentDate(new Date());

            // contenuto mail
            Multipart multipart = new MimeMultipart();          // insieme delle partizioni
            MimeBodyPart htmlPart = new MimeBodyPart();         // testo
            //MimeBodyPart avatarPart;                          // immagine

            // testo
            htmlPart.setContent(text, "text/html; charset=UTF-8");

            /* avatar
            avatarPart = new MimeBodyPart();
            DataSource fds = new FileDataSource(
                    ImageUtil.getImagePath() + user.getAvatar().getNameAsImage());

            avatarPart.setDataHandler(new DataHandler(fds));
            avatarPart.setHeader("Content-ID", "<image>");*/    // tutto immagine


            multipart.addBodyPart(htmlPart);
            // multipart.addBodyPart(avatarPart);               // immagine
            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException ex) {
            LOGGER.log(Level.SEVERE, "Unable to send email", ex);
        }
    }
}