package itu.auth.mg.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import itu.auth.mg.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

