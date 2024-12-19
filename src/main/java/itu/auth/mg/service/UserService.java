package itu.auth.mg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itu.auth.mg.args.LoginData;
import itu.auth.mg.args.Otp;
import itu.auth.mg.model.Token;
import itu.auth.mg.model.User;
import itu.auth.mg.repositories.TokenRepository;
import itu.auth.mg.repositories.UserRepository;
import itu.auth.mg.util.Constants;
import itu.auth.mg.util.PasswordUtil;
import itu.auth.mg.util.Utilitaire;
import jakarta.mail.MessagingException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private TokenService tokenService;

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
        userRepository.save(user);

        
        Token verificationToken = new Token();
        // verificationToken.setToken(token);
        String pin = Utilitaire.generatePin();
        verificationToken.setPin(pin);
        verificationToken.setExpiration(LocalDateTime.now().plusHours(24));
        verificationToken.setUser(user);

        tokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(user.getEmail(), Utilitaire.encodePin(pin), pin);
    }

    public boolean loginUser(LoginData data) throws MessagingException {
        Optional<User> userOptional = userRepository.findByEmail(data.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.isVerified() && passwordUtil.verifyPassword(data.getPassword(), user.getPassword())) {
                String pin = Utilitaire.generatePin();
                Token verificationToken = new Token();

                verificationToken.setUser(user);
                verificationToken.setActive(true);
                verificationToken.setPin(pin);
                verificationToken.setExpiration(LocalDateTime.now().plusSeconds((long) Constants.getTimerPin()));
                
                emailService.sendOtpEmail(user.getEmail(), pin);
                
                tokenRepository.save(verificationToken);
                return true;
            }
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

    public boolean verifyOtp(Otp otp) {
        return tokenService.checkByPin(otp.getPin());
    }

    public Token genererToken(String pin) {
        Optional<Token> temps =  tokenRepository.findByPin(pin);
        if (temps.isPresent()) {
            User user = temps.get().getUser();
            return tokenService.generateToken(user);            
        }
        return null;
    }
}


