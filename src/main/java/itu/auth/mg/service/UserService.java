package itu.auth.mg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itu.auth.mg.model.Token;
import itu.auth.mg.model.User;
import itu.auth.mg.repositories.TokenRepository;
import itu.auth.mg.repositories.UserRepository;
import itu.auth.mg.util.PasswordUtil;
import itu.auth.mg.util.Utilitaire;
import jakarta.mail.MessagingException;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordUtil passwordUtil;

    public void registerUser(User user) throws MessagingException {
        
        user.setPassword(passwordUtil.hashPassword(user.getPassword()));
        // userRepository.save(user);

        
        Token verificationToken = new Token();
        // verificationToken.setToken(token);
        String pin = Utilitaire.generatePin();
        verificationToken.setPin(pin);
        verificationToken.setExpiration(LocalDateTime.now().plusHours(24));
        verificationToken.setUser(user);

        // tokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(user.getEmail(), Utilitaire.encodePin(pin), pin);
    }

    public boolean loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.isVerified() && passwordUtil.verifyPassword(password, user.getPassword());
        }
        return false;
    }
    

    public boolean verify(String pin) {
        Optional<Token> optionalToken;
        if (pin == null) {
            return false;
        } else if (pin.length() > 6) {
            pin = Utilitaire.decodePin(pin);
        }
        optionalToken = tokenRepository.findByPin(pin);
        return optionalToken.map(this::activateUser).orElse(false);
    }

    private boolean activateUser(Token token) {
        if (token.isActive() && token.getExpiration().isAfter(LocalDateTime.now())) {
            User user = token.getUser();
            user.setVerified(true);
            userRepository.save(user);

            token.setActive(false);
            tokenRepository.save(token);
            return true;
        }
        return false;
    }
}


