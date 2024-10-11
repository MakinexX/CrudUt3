package com.juan.CrudUt.repository;

import com.juan.CrudUt.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    List<Persona> findByEdad(Integer edad);
    List<Persona> findByPnombre(String pnombre);
    List<Persona> findByPapellido(String papellido);
    Optional<Persona> findByIdentificacion(Integer identificacion);
}
