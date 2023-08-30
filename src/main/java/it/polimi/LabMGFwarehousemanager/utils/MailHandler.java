package it.polimi.LabMGFwarehousemanager.utils;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import static it.polimi.LabMGFwarehousemanager.utils.Constants.MAIL_CONFIG_FILE_PATH;

public class MailHandler {

    final private static String hostname_bk = "smtp.polimi.it";
    final private static String username_bk = "";
    final private static String password_bk = "";
    final private static String senderMail_bk = "labmgf-dica@polimi.it";

    public static void sendMail(String recipient, String subject, String message) throws MessagingException{

        Properties props = new Properties();

        String username;
        String password;
        String senderMail;

        Map<String, String> mailConfig = FileHandler.getConfig(MAIL_CONFIG_FILE_PATH);

        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", hostname_bk);
        props.put("mail.smtp.port", "25");
        username = username_bk;
        password = password_bk;
        senderMail = senderMail_bk;

        if(mailConfig != null){
                props.put("mail.smtp.auth", mailConfig.get("mail.smtp.auth"));
                props.put("mail.smtp.starttls.enable", mailConfig.get("mail.smtp.starttls.enable"));
                props.put("mail.smtp.host", mailConfig.get("mail.smtp.host"));
                props.put("mail.smtp.port", mailConfig.get("mail.smtp.port"));
                username = mailConfig.get("mail.smtp.username");
                password = mailConfig.get("mail.smtp.password");
                senderMail = mailConfig.get("senderMail");
        }

        String finalUsername = username;
        String finalPassword = password;

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(finalUsername, finalPassword);
            }
        };

        Session session = Session.getInstance(props, auth);

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(senderMail));
            InternetAddress[] address = {new InternetAddress(recipient)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setText(message);

            Transport.send(msg);
    }

    public static void sendMail(ArrayList<String> recipients, String subject, String message) throws MessagingException{
        for (String receipient : recipients) {
            sendMail(receipient, subject, message);
        }
    }
}

