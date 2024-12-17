package itu.auth.mg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itu.auth.mg.DAO.TokenRepository;
import itu.auth.mg.DAO.UserRepository;
import itu.auth.mg.model.Token;
import itu.auth.mg.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    public void registerUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        userRepository.save(user);

        String generatedToken = UUID.randomUUID().toString();
        Token token = new Token();
        token.setToken(generatedToken);
        token.setExpiration(LocalDateTime.now().plusHours(24));
        token.setUser(user);

        tokenRepository.save(token);
        emailService.sendVerificationEmail(email, generatedToken);
    }

    public boolean verifyToken(String tokenString) {
        Token token = tokenRepository.findByToken(tokenString).orElse(null);

        if (token != null && token.isActive() && token.getExpiration().isAfter(LocalDateTime.now())) {
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

