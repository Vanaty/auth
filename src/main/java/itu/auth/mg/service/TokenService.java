package itu.auth.mg.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itu.auth.mg.model.Token;
import itu.auth.mg.model.User;
import itu.auth.mg.repositories.TokenRepository;
import itu.auth.mg.util.Utilitaire;

@Service
public class TokenService {

    @Autowired
    private SettingService sessionService;
    @Autowired
    TokenRepository tokenRepository;

    public boolean checkByPin(String pin) {
        Optional<Token> optionalToken = tokenRepository.findByPin(pin);
        return optionalToken.map(this::useTokenAndDesactive).orElse(false);
    }

    private boolean useTokenAndDesactive(Token token) {
        if(token.isActive() && token.getExpiration().isAfter(LocalDateTime.now())) {
            token.setActive(false);
            tokenRepository.save(token);
            return true;
        }
        return false;
    }

    public Token generateToken(User user) {
        Token token = new Token();
        double dureer = sessionService.getDuree();
        token.setActive(true);
        token.setExpiration(LocalDateTime.now().plusSeconds((long) dureer));
        token.setUser(user);
        token.setToken(Utilitaire.generateToken());
        tokenRepository.save(token);
        return token;
    }
}
