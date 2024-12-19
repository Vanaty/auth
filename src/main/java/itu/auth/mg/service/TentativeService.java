package itu.auth.mg.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itu.auth.mg.model.Setting;
import itu.auth.mg.model.Tentative;
import itu.auth.mg.model.Token;
import itu.auth.mg.model.User;
import itu.auth.mg.repositories.SettingRepository;
import itu.auth.mg.repositories.TentativeRepository;
import itu.auth.mg.repositories.TokenRepository;
import itu.auth.mg.util.Constants;
import itu.auth.mg.util.Utilitaire;
import jakarta.mail.MessagingException;

@Service
public class TentativeService {
    @Autowired
    TokenRepository tokenRep;

    @Autowired 
    EmailService emailService;

    @Autowired
    TentativeRepository tentativeRepository;

    @Autowired
    SettingRepository setting;

    public void reinitialise(User u) {
        double sum = tentativeRepository.getNbrTentative(u.getId());
        Tentative t = new Tentative();
        t.setUser(u);
        t.setDaty(LocalDateTime.now());
        t.setCompteur(-1 * sum);
        tentativeRepository.save(t);
    }

    public void increamentTentative(User u) {
        Tentative t = new Tentative();
        t.setUser(u);
        t.setDaty(LocalDateTime.now());
        t.setCompteur(1);
        tentativeRepository.save(t);
    }


    public boolean canMake(User u) {
        Setting  s= setting.findByMaxDate();
        int tMax = 3;
        if (s != null) {
            tMax = (int) s.getTentativeMax();
        }
        return (tMax > tentativeRepository.getNbrTentative(u.getId()));
    }

    public void prepareReinitialisation(User user) throws MessagingException {
        String pin = Utilitaire.generatePin();
        Token verificationToken = new Token();

        verificationToken.setUser(user);
        verificationToken.setActive(true);
        verificationToken.setPin(pin);
        verificationToken.setExpiration(LocalDateTime.now().plusSeconds((long) Constants.getTimerPin()));
        
        emailService.sendReinitTentativeE(user.getEmail(), pin);
        
        tokenRep.save(verificationToken);
    }
}
