package itu.auth.mg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itu.auth.mg.model.Token;
import itu.auth.mg.model.User;
import itu.auth.mg.repositories.TokenRepository;
import itu.auth.mg.repositories.UserRepository;
import itu.auth.mg.util.PasswordUtil;
import jakarta.mail.MessagingException;

import java.time.LocalDateTime;
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

        String token = UUID.randomUUID().toString();
        String pin = String.format("%06d", new Random().nextInt(999999));

        Token verificationToken = new Token();
        verificationToken.setToken(token);
        verificationToken.setPin(pin);
        verificationToken.setExpiration(LocalDateTime.now().plusHours(24));
        verificationToken.setUser(user);

        // tokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(user.getEmail(), token, pin);
    }

    public boolean verifyByToken(String token) {
        Optional<Token> optionalToken = tokenRepository.findByToken(token);
        return optionalToken.map(this::activateUser).orElse(false);
    }

    public boolean verifyByPin(String pin) {
        Optional<Token> optionalToken = tokenRepository.findByPin(pin);
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

    public boolean updateUserInfo(Long userId, User updatedUser) {
        Optional<User> existingUserOpt = userRepository.findById(userId);
        
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            if (updatedUser.getNom() != null && !updatedUser.getNom().isEmpty()) {
                existingUser.setNom(updatedUser.getNom());
            }

            if (updatedUser.getPrenom() != null && !updatedUser.getPrenom().isEmpty()) {
                existingUser.setPrenom(updatedUser.getPrenom());
            }

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordUtil.hashPassword(updatedUser.getPassword()));
            }

            userRepository.save(existingUser);
            return true;
        }
        return false; 
    }
}
}


