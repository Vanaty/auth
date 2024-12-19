package itu.auth.mg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import itu.auth.mg.model.Session;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    
}


