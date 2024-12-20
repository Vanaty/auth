package itu.auth.mg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itu.auth.mg.args.LoginData;
import itu.auth.mg.args.Otp;
import itu.auth.mg.model.Setting;
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
    private TentativeService tentative;
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

    public boolean loginUser(LoginData data) throws Exception {
        Optional<User> userOptional = userRepository.findByEmail(data.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!tentative.canMake(user)) {
                throw new Exception("Le nbr de tentative atteint");
            } else if (user.isVerified() && passwordUtil.verifyPassword(data.getPassword(), user.getPassword())) {
                String pin = Utilitaire.generatePin();
                Token verificationToken = new Token();

                verificationToken.setUser(user);
                verificationToken.setActive(true);
                verificationToken.setPin(pin);
                verificationToken.setExpiration(LocalDateTime.now().plusSeconds((long) Constants.getTimerPin()));
                
                emailService.sendOtpEmail(user.getEmail(), pin);
                
                tokenRepository.save(verificationToken);
                return true;
            } else {
                tentative.increamentTentative(user);
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

    public boolean verifyOtp(Otp otp) throws Exception {
        boolean succes = tokenService.checkByPin(otp.getPin());
        Optional<Token> temps =  tokenRepository.findByPin(otp.getPin());
        if (temps.isPresent()) {
            User user = temps.get().getUser();
            if (!tentative.canMake(user)) {
                throw new Exception("Nombre de tentative atteint");
            } else if(!succes) {
                tentative.increamentTentative(user);
            } else {
                tentative.reinitialise(user);
            }
        }
        return succes;
    }

    public Token genererToken(String pin) {
        Optional<Token> temps =  tokenRepository.findByPin(pin);
        if (temps.isPresent()) {
            User user = temps.get().getUser();
            return tokenService.generateToken(user);            
        }
        return null;
    }

    public User getUserByOtp(Otp otp) {
        Optional<Token> temps =  tokenRepository.findByPin(otp.getPin());
        if (temps.isPresent()) {
            return temps.get().getUser();
        }
        return null;
    }

    public Optional<User> update(Long id, User session) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            // if(session.getEmail() != null)   u.setEmail(session.getEmail());
            if(session.getPassword() != null)   u.setPassword(passwordUtil.hashPassword(session.getPassword()));
            if(session.getNom() != null)   u.setNom(session.getNom());
            if(session.getPrenom() != null)   u.setPrenom(session.getPrenom());
            session.setId(id);
            return Optional.of(userRepository.save(u));
        }
        return Optional.empty();
    }
}


