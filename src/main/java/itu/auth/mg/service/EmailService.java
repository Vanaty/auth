package itu.auth.mg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import itu.auth.mg.model.User;
import itu.auth.mg.util.Constants;
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
        double timer  = 90; //Fixena ito;
        context.setVariable("verification", url);
        context.setVariable("timer", timer);
        context.setVariable("pin_code", pin);

        String htmlContent = templateEngine.process("email-verification", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(pin + " | Email Verification");
        helper.setText(htmlContent, true); // true indicates HTML content

        mailSender.send(message);
    }

    public void sendOtpEmail(String to, String pin) throws MessagingException {
        Context context = new Context();

        double timer  = Constants.getTimerPin();
        context.setVariable("timer", timer);
        context.setVariable("pin_code", pin);

        String htmlContent = templateEngine.process("email-otp", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(pin + " | Email Verification");
        helper.setText(htmlContent, true); // true indicates HTML content

        mailSender.send(message);
    }

    public void sendReinitTentativeE(String to, String pin) throws MessagingException {
        Context context = new Context();

        double timer  = Constants.getTimerPin();
        context.setVariable("timer", timer);
        context.setVariable("pin_code", pin);

        String htmlContent = templateEngine.process("email-tentative", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(pin + " | Renitilisation Tentative");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
