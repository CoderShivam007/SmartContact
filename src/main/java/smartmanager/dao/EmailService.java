package smartmanager.dao;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import org.springframework.stereotype.Service;

@Service
public class EmailService 
{
    public void sendEmail(String tom,String otp) 
    {
        String from = "sg1680634@gmail.com";
        String password = "bbdzzocydtwtemgz";
        String host = "smtp.gmail.com";
        String to = tom;
        String subject = "Smart Contact Manager - Verify your Account";
        String msg = "Verify Your account, Otp:" + otp;
        // Email configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host); // Replace with the SMTP server address
        props.put("mail.smtp.port", "587"); // Replace with the SMTP server port

        // Create a session with authentication credentials
        Session session = Session.getInstance(props, new Authenticator() 
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() 
            {
                return new PasswordAuthentication(from, password);
            }
        });

        try 
        {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            message.setText(msg);
            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully.");

            
        } 
        
        catch (MessagingException e) 
        {
            e.printStackTrace();
        }
        

        
    }

}

