package itu.auth.mg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import itu.auth.mg.args.ApiResponse;
import itu.auth.mg.args.LoginData;
import itu.auth.mg.service.TokenService;

@Controller
@RequestMapping("/api/token")
public class TokenController {
    @Autowired
    TokenService tk;

    @PostMapping("/checked/{token}")
    public ResponseEntity<ApiResponse<String>> isLoged(@PathVariable String token) throws Exception {
        if (tk.isActive(token)) {
            return ResponseEntity.ok(new ApiResponse<>(
                true, "Token valide !", null
            ));
        } else {
            return ResponseEntity.status(401).body(new ApiResponse<>(
                false, "Expired.", null
            ));
        }
    }
}
