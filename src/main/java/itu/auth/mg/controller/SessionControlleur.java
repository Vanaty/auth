package itu.auth.mg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import itu.auth.mg.args.ApiResponse;
import itu.auth.mg.model.Session;
import itu.auth.mg.service.SessionService;
import itu.auth.mg.args.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/session")
public class SessionControlleur {

    @Autowired
    private SessionService sessionService;

    // Création d'une nouvelle session
    @PostMapping
    public ResponseEntity<ApiResponse> createSession(@RequestBody Session session) {
        try {
            Session createdSession = sessionService.createSession(session);
            return ResponseEntity.ok(new ApiResponse(true, "Session créée avec succès", createdSession));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse(false, "Erreur lors de la création de la session", null));
        }
    }

    // Récupérer toutes les sessions
    @GetMapping
    public ResponseEntity<ApiResponse> getAllSessions() {
        try {
            List<Session> sessions = sessionService.getAllSessions();
            return ResponseEntity.ok(new ApiResponse(true, "Sessions récupérées avec succès", sessions));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(false, "Erreur lors de la récupération des sessions", null));
        }
    }

    // Récupérer une session par son ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getSessionById(@PathVariable Long id) {
        try {
            Optional<Session> session = sessionService.getSessionById(id);
            if (session.isPresent()) {
                return ResponseEntity.ok(new ApiResponse(true, "Session trouvée", session.get()));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse(false, "Session non trouvée", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(false, "Erreur lors de la récupération de la session", null));
        }
    }

    // Mettre à jour une session
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateSession(@PathVariable Long id, @RequestBody Session session) {
        try {
            Optional<Session> updatedSession = sessionService.updateSession(id, session);
            if (updatedSession.isPresent()) {
                return ResponseEntity.ok(new ApiResponse(true, "Session mise à jour avec succès", updatedSession.get()));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse(false, "Session non trouvée pour mise à jour", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(false, "Erreur lors de la mise à jour de la session", null));
        }
    }

    // Supprimer une session
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSession(@PathVariable Long id) {
        try {
            boolean isDeleted = sessionService.deleteSession(id);
            if (isDeleted) {
                return ResponseEntity.ok(new ApiResponse(true, "Session supprimée avec succès", null));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse(false, "Session non trouvée pour suppression", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(false, "Erreur lors de la suppression de la session", null));
        }
    }
}
