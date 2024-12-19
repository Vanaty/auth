package itu.auth.mg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import itu.auth.mg.model.Session;
import itu.auth.mg.repositories.SessionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    // Créer une nouvelle session
    public Session createSession(Session session) {
        session.setDaty(LocalDateTime.now());
        return sessionRepository.save(session);
    }

    // Récupérer toutes les sessions
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    // Récupérer une session par son ID
    public Optional<Session> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }

    // Mettre à jour une session
    public Optional<Session> updateSession(Long id, Session session) {
        if (sessionRepository.existsById(id)) {
            session.setId(id); // S'assurer que l'ID est correctement défini
            session.setDaty(LocalDateTime.now());
            return Optional.of(sessionRepository.save(session));
        }
        return Optional.empty();
    }

    // Supprimer une session
    public boolean deleteSession(Long id) {
        if (sessionRepository.existsById(id)) {
            sessionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
