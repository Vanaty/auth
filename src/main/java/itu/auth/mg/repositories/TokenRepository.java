package itu.auth.mg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import itu.auth.mg.model.Token;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
    Optional<Token> findByPin(String pin);
}


