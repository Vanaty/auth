package itu.auth.mg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import itu.auth.mg.model.Setting;
import itu.auth.mg.repositories.SettingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SettingService {

    @Autowired
    private SettingRepository setting;

    // Créer une nouvelle session
    public Setting createSetting(Setting session) {
        session.setDaty(LocalDateTime.now());
        return setting.save(session);
    }

    // Récupérer toutes les sessions
    public List<Setting> getAllSettings() {
        return setting.findAll();
    }

    // Récupérer une session par son ID
    public Optional<Setting> getSettingById(Long id) {
        return setting.findById(id);
    }

    // Mettre à jour une session
    public Optional<Setting> updateSetting(Long id, Setting session) {
        if (setting.existsById(id)) {
            session.setId(id); // S'assurer que l'ID est correctement défini
            session.setDaty(LocalDateTime.now());
            return Optional.of(setting.save(session));
        }
        return Optional.empty();
    }

    // Supprimer une session
    public boolean deleteSetting(Long id) {
        if (setting.existsById(id)) {
            setting.deleteById(id);
            return true;
        }
        return false;
    }

    public double getDuree() {
        Setting temp = setting.findByMaxDate();
        if (temp != null) {
            return temp.getSessionDuree();
        }
        return 60 * 60 * 60;
    }
}
