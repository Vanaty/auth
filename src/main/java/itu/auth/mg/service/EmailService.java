package itu.auth.mg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendVerificationEmail(String to, String token, String pin) throws MessagingException {

        Context context = new Context();

        String url = "http://localhost:8080/api/users/verify?token="+token;
        context.setVariable("verification", url);
        context.setVariable("pin_code", pin);

        String htmlContent = templateEngine.process("email-verification", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Email Verification");
        helper.setText(htmlContent, true); // true indicates HTML content

        mailSender.send(message);
    }
}
