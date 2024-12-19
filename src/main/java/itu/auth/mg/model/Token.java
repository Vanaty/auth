package itu.auth.mg.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_token")
    private Long id;

    private String token;
    private String pin;   // Code PIN

    private LocalDateTime expiration;

    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
}

