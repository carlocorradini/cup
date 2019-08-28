package it.unitn.disi.wp.cup.util;

import it.unitn.disi.wp.cup.config.EmailConfig;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(EmailConfig.getUsername()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(text);
            message.setSentDate(new Date());

            Transport.send(message);
        } catch (MessagingException ex) {
            LOGGER.log(Level.SEVERE, "Unable to send email", ex);
        }
    }
}
