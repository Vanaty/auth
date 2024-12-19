package itu.auth.mg.controller;

import org.springframework.web.bind.annotation.RestController;

import itu.auth.mg.args.ApiResponse;
import itu.auth.mg.args.Otp;
import itu.auth.mg.model.Token;
import itu.auth.mg.model.User;
import itu.auth.mg.repositories.UserRepository;
import itu.auth.mg.service.TentativeService;
import itu.auth.mg.service.TokenService;
import itu.auth.mg.service.UserService;
import jakarta.mail.MessagingException;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/tentative")
public class TentativeController {
    @Autowired
    UserRepository userRep;
    @Autowired
    UserService us;
    @Autowired
    TokenService ts;
    @Autowired
    TentativeService tentativeService;

    @PostMapping("/reinitialise/{id_user}")
    public ResponseEntity<ApiResponse<String>> reinitialise(@PathVariable Long id_user) {
        try {
            Optional<User> user = userRep.findById(id_user);
            if (user.isPresent()) {
                tentativeService.prepareReinitialisation(user.get());
                return ResponseEntity.status(200).body(new ApiResponse(true, "Verifier votre email!", null));
            }
            return ResponseEntity.status(404).body(new ApiResponse(false, "User non trouver", null));
        } catch(Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Token>> doreinit(@RequestBody Otp otp) {
        try {
            if (ts.checkByPin(otp.getPin())) {
                tentativeService.reinitialise(us.getUserByOtp(otp));
                return ResponseEntity.ok(new ApiResponse<>(
                    true, "Tentative reinitialiser avec succes !",null  
                ));
            } else {
                return ResponseEntity.status(401).body(new ApiResponse<>(
                    false, "Pin deja utilise ou a expierer.", null
                ));
            }
        } catch(Exception e) {
            return ResponseEntity.status(401).body(new ApiResponse<>(
                    false, e.getMessage(), null
                ));
        }
    }

}
