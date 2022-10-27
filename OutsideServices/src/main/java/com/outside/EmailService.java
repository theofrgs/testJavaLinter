package com.outside;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.*;
import javax.mail.internet.*;

@org.springframework.stereotype.Service
public class EmailService extends Service {

    protected Parser parser = new Parser();
    private String host = properties.getProperty("smtp.host");
    private String user = properties.getProperty("smtp.user");
    private String password = properties.getProperty("smtp.password");

    public EmailService() {
        this.initLogger(EmailService.class);
    }

    private static interface Bindings {
        public final static String CONTENT_TEXT_HTML = "text/html";
    }

    private Map<String, Object> isEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);

        if (email == null)
            return Map.of("success", false, "errorMsg", "Email cant be null");
        if (!pat.matcher(email).matches())
            return Map.of("success", false, "errorMsg", "Email is not an email");
        return Map.of("success", true);
    }

    private int generateCode() {
        return (int) (Math.random() * (999999 - 99999 + 1) + 99999);
    }

    private Session createSessionSmtp(Properties properties, String user, String password) {
        return Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });
    }

    private Map<String, Object> createMsg(Session session, String email, String subject, String content) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject(subject);
            message.setContent(content,  Bindings.CONTENT_TEXT_HTML);
            return Map.of("success", true, "message", message);
        } catch (Exception e) {
            this.LOGGER.error(ExceptionUtils.getMessage(e));
            return Map.of("success", false, "errorMsg", ExceptionUtils.getMessage(e));
        }
    }

    private Properties createProperties(String host) {
        Properties properties = new Properties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        return properties;
    }

    public Map<String, Object> sendEmail(MimeMessage message) {
        try {
            Transport.send(message);
            LOGGER.debug("message sent successfully...");
            return Map.of("success", true, "data", "message sent successfully");
        } catch (Exception e) {
            this.LOGGER.error(ExceptionUtils.getMessage(e));
            return Map.of("success", false, "errorMsg", ExceptionUtils.getMessage(e));
        }
    }

    private Map<String, Object> createBasicMail(String email, String subject, String content) {
        Properties props = null;
        Session session = null;
        Map<String, Object> error = isEmail(email);

        if (!(boolean) error.get("success")) {
            return Map.of("success", false, "errorMsg", error);
        }
        props = createProperties(this.host);
        session = createSessionSmtp(props, this.user, this.password);
        return createMsg(session, email, subject, content);
    }

    public Map<String, Object> sendVerifEmail(String email) {
        int code = generateCode();
        MimeMessage message = null;
        Map<String, Object> data = createBasicMail(
                email,
                "Verification email",
                "<div>Email de vérification\nVoici votre code vérification: " + code + "</div>");

        if (!(boolean) data.get("success")) {
            return data;
        }
        message = (MimeMessage) data.get("message");
        data = sendEmail(message);
        return (boolean) data.get("success") ? Map.of("success", true, "code", code) : data;
    }
}