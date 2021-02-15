package co.ufps.edu.util;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Clase que permite usar servicios para el envio de correos a los usuarios.
 * 
 * @author UFPS
 */
public class MailUtil {

    private String USER_NAME;
    private String PASSWORD;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public MailUtil() {
        USER_NAME = "recuperacion.webapp@gmail.com";  // GMail user name (just the part before "@gmail.com")
        PASSWORD = "flnmrxkrwejtziut"; // GMail password
    }

    /**
     * Método en el que se arma el correo y se envia por medio del correo configurado para este 
     * servicio.
     * 
     * @param to Para quien va el correo.
     * @param subject El tema o encabezado del correo.
     * @param body Cuerpo del correo.
     * 
     * @return El resultado de la acción.
     */
    public String sendFromGMail(String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", USER_NAME);
        props.put("mail.smtp.password", PASSWORD);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for (int i = 0; i < to.length; i++) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for (int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, USER_NAME, PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            return "Actualizacion";
        } catch (AddressException ae) {
            ae.printStackTrace();
            return "no envio";
        } catch (MessagingException me) {
            me.printStackTrace();
            return "no envio";
        }
    }
}
