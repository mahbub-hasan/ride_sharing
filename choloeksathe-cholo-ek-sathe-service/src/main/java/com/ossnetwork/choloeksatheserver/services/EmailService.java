package com.ossnetwork.choloeksatheserver.services;

import com.ossnetwork.choloeksatheserver.utils.CommonConstant;
import com.ossnetwork.choloeksatheserver.utils.SocialAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@Service
@Component
public class EmailService{

    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(CommonConstant.FROM_ADDRESS);
        mailSender.setPassword(CommonConstant.PASSWORD);

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol","smtp");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.bebug","true");

        return mailSender;
    }




    public void sendEmail(String to, String subject, String password, String fullName, String mobileNo){
        try {
            /*Properties properties = new Properties();
            properties.put("mail.smtp.user", CommonConstant.FROM_ADDRESS);
            properties.put("mail.smtp.host", CommonConstant.SMTP_HOST);
            properties.put("mail.smtp.password", CommonConstant.PASSWORD);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.debug", "false");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.port", CommonConstant.SMTP_PORT);
            Session session = Session.getInstance(properties, new SocialAuth());
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(CommonConstant.FROM_ADDRESS, CommonConstant.FROM_NAME));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            String body = "Hello "+fullName+",\n" +
                    "Greetings from Cholo Ek Sathe apps!\n" +
                    "Following password is use for log into your account\n" +
                    "      Mobile Number : "+mobileNo+"\n" +
                    "      Password : "+password+"\n" +
                    "For any queries, please contact with (http://choloeksathe.com).\n" +
                    "Thank you for choosing this apps.";
            message.setText(body);
            Transport.send(message);*/

            String body = "Hello "+fullName+",\n" +
                    "Greetings from Cholo Ek Sathe apps!\n" +
                    "Following password is use for log into your account\n" +
                    "      Mobile Number : "+mobileNo+"\n" +
                    "      Password : "+password+"\n" +
                    "For any queries, please contact with (http://choloeksathe.com).\n" +
                    "Thank you for choosing this apps.";

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            getJavaMailSender().send(message);

        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
