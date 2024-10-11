package com.juan.CrudUt.repository;

import com.juan.CrudUt.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByApiKey(String apiKey); // Este ya está
    Optional<Usuario> findById(Long id); // Este ya está
    Optional<Usuario> findByLogin(String login);
}
