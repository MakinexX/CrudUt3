package com.juan.CrudUt.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.security.SecureRandom;
import java.util.Base64;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false) // updatable = false para evitar modificaciones
    private String login;

    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true, updatable = false) // updatable = false para evitar modificaciones
    private String apiKey;

    @OneToOne
    @JoinColumn(name = "persona_id", referencedColumnName = "id")
    @JsonBackReference
    private Persona persona;

    @PrePersist
    private void generateApiKey() {
        SecureRandom random = new SecureRandom();
        byte[] apiKeyBytes = new byte[24];
        random.nextBytes(apiKeyBytes);
        this.apiKey = Base64.getUrlEncoder().withoutPadding().encodeToString(apiKeyBytes);
        System.out.println("API Key generado: " + this.apiKey);
    }

}
