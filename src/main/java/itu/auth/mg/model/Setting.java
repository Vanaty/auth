package itu.auth.mg.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "setting")
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_setting")
    private Long id;

    private double sessionDuree = 0;
    private double tentativeMax = 3;
    private LocalDateTime daty; 
}