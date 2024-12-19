package itu.auth.mg.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import itu.auth.mg.model.Tentative;

@Repository
public interface TentativeRepository extends JpaRepository<Tentative, Long> {
    @Query("SELECT COALESCE(SUM(t.compteur), 0) FROM Tentative t WHERE t.user.id = :idUser")
    double getNbrTentative(@Param("idUser") Long idUser);
}
