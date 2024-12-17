package itu.auth.mg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itu.auth.mg.args.ApiResponse;
import itu.auth.mg.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestParam String email, @RequestParam String password) {
        userService.registerUser(email, password);
        return ResponseEntity.ok(new ApiResponse<>(
            true, "Inscription réussie ! Vérifiez votre e-mail pour activer votre compte.", null
        ));
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verify(@RequestParam String token) {
        if (userService.verifyToken(token)) {
            return ResponseEntity.ok(new ApiResponse<>(
                true, "Compte activé avec succès !", null
            ));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                false, "Token invalide ou expiré.", null
            ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestParam String email, @RequestParam String password) {
        if (userService.loginUser(email, password)) {
            return ResponseEntity.ok(new ApiResponse<>(
                true, "Connexion réussie !", null
            ));
        } else {
            return ResponseEntity.status(401).body(new ApiResponse<>(
                false, "Identifiants incorrects ou compte non activé.", null
            ));
        }
    }
}

