package it.polimi.geodesicwarehousemanager.utils;

import java.util.ArrayList;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailHandler {

    final private static String hostname = "smtp.cloudmta.net";
    final private static String username = "4861b2f948c506de";
    final private static String password = "k5e3HSDPgz5RMGwvRx3nbu57";

    final static private Properties props = new Properties();

    static {
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", hostname);
        props.put("mail.smtp.port", "587");
        props.put("mail.debug", "false");
    }

    final static private Authenticator auth = new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    };

    final static private Session session = Session.getInstance(props, auth);
    final static private MimeMessage msg = new MimeMessage(session);

    public static void sendMail(String recipient, String subject, String message) {
        Session session = Session.getInstance(props, auth);
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("geodesic.WareHouse@polimi.it"));
            InternetAddress[] address = {new InternetAddress(recipient)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setText(message);

            Transport.send(msg);
        } catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void sendMail(ArrayList<String> recipients, String subject, String message){
        for (String receipient : recipients) {
            sendMail(receipient, subject, message);
        }
    }
}

