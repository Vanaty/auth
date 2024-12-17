package itu.auth.mg.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import itu.auth.mg.model.Token;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
}

