package it.unitn.disi.wp.cup.servlet.handler;

import it.unitn.disi.wp.cup.config.EmailConfig;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/email.handler")
public final class EmailServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(EmailServlet.class.getName());
    private Properties properties;

    @Override
    public void init() throws ServletException {
        properties = System.getProperties();
        properties.setProperty("mail.smtp.host", EmailConfig.getSmtpHost());
        properties.setProperty("mail.smtp.port", Integer.toString(EmailConfig.getSmtpPort()));
        properties.setProperty("mail.smtp.socketFactory.port", Integer.toString(EmailConfig.getSmtpPort()));
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.auth", Boolean.toString(EmailConfig.getSmtpAuth()));
        properties.setProperty("mail.smtp.starttls.enable", Boolean.toString(EmailConfig.getSmtpTls()));
        properties.setProperty("mail.debug", Boolean.toString(EmailConfig.getDebug()));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Session session;
        Message message;

        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EmailConfig.getUsername(), EmailConfig.getPassword());
            }
        });

        message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(EmailConfig.getUsername()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("carlo.corradini@studenti.unitn.it"));
            message.setSubject("TESTING EMAIL WITH JAVA");
            message.setText("Corpo della email");
            message.setSentDate(new Date());

            Transport.send(message);
        } catch (MessagingException ex) {
            LOGGER.log(Level.SEVERE, "Unable to send email", ex);
        }
    }
}
