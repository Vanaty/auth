package itu.auth.mg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import itu.auth.mg.args.ApiResponse;
import itu.auth.mg.model.Setting;
import itu.auth.mg.service.SettingService;
import itu.auth.mg.args.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/setting")
public class SettingControlleur {

    @Autowired
    private SettingService settingService;

    // Création d'une nouvelle setting
    @PostMapping
    public ResponseEntity<ApiResponse> createSetting(@RequestBody Setting setting) {
        try {
            Setting createdSetting = settingService.createSetting(setting);
            return ResponseEntity.ok(new ApiResponse(true, "Setting créée avec succès", createdSetting));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse(false, "Erreur lors de la création de la setting", null));
        }
    }

    // Récupérer toutes les settings
    @GetMapping
    public ResponseEntity<ApiResponse> getAllSettings() {
        try {
            List<Setting> settings = settingService.getAllSettings();
            return ResponseEntity.ok(new ApiResponse(true, "Settings récupérées avec succès", settings));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(false, "Erreur lors de la récupération des settings", null));
        }
    }

    // Récupérer une setting par son ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getSettingById(@PathVariable Long id) {
        try {
            Optional<Setting> setting = settingService.getSettingById(id);
            if (setting.isPresent()) {
                return ResponseEntity.ok(new ApiResponse(true, "Setting trouvée", setting.get()));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse(false, "Setting non trouvée", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(false, "Erreur lors de la récupération de la setting", null));
        }
    }

    // Mettre à jour une setting
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateSetting(@PathVariable Long id, @RequestBody Setting setting) {
        try {
            Optional<Setting> updatedSetting = settingService.updateSetting(id, setting);
            if (updatedSetting.isPresent()) {
                return ResponseEntity.ok(new ApiResponse(true, "Setting mise à jour avec succès", updatedSetting.get()));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse(false, "Setting non trouvée pour mise à jour", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(false, "Erreur lors de la mise à jour de la setting", null));
        }
    }

    // Supprimer une setting
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSetting(@PathVariable Long id) {
        try {
            boolean isDeleted = settingService.deleteSetting(id);
            if (isDeleted) {
                return ResponseEntity.ok(new ApiResponse(true, "Setting supprimée avec succès", null));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse(false, "Setting non trouvée pour suppression", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(false, "Erreur lors de la suppression de la setting", null));
        }
    }
}
