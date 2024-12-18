package itu.auth.mg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import itu.auth.mg.service.EmailService;


@SpringBootApplication
public class AuthApplication {
	// @Autowired
	// private EmailService email;
	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

	// @EventListener(ApplicationReadyEvent.class)
	// public void sendMail() {
	// 	email.sendSimpleMessage("finaritraantsa@gmail.com", "<Test web exam", "Azertuioopiapzoeip  dsf");
	// }
}
