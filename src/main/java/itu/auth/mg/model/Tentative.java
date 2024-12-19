package itu.auth.mg.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Tentative")
public class Tentative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tentative")
    private Long id;

    private double compteur = 0;
    @Column(nullable = false)
    private LocalDateTime daty;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
}
