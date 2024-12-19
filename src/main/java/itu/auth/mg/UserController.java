package itu.auth.mg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itu.auth.mg.args.ApiResponse;
import itu.auth.mg.model.User;
import itu.auth.mg.service.UserService;
import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody User user) throws MessagingException {
        userService.registerUser(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Inscription réussie ! Vérifiez votre e-mail.", null));
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verifyByToken(@RequestParam String token) {
        if (userService.verifyByToken(token)) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Compte activé avec succès !", null));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Token invalide ou expiré.", null));
    }

    @PostMapping("/verify_pin")
    public ResponseEntity<ApiResponse<String>> verifyByPin(@RequestParam String pin) {
        if (userService.verifyByPin(pin)) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Compte activé avec succès !", null));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "PIN invalide ou expiré.", null));
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<String> updateUserInfo(@PathVariable Long userId, @RequestBody User updatedUser) {
        boolean isUpdated = userService.updateUserInfo(userId, updatedUser);
        
        if (isUpdated) {
            return ResponseEntity.ok("les donnees de l utilisateur ont ete bien mise a jour.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("l'utilisateur est introuvable .");
        }
    }
}
