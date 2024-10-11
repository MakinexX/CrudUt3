package com.juan.CrudUt.controller;

import com.juan.CrudUt.entity.Persona;
import com.juan.CrudUt.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @PostMapping
    public ResponseEntity<Persona> crearPersona(@RequestBody Persona persona) {
        try {
            Persona nuevaPersona = personaService.crearPersona(persona);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPersona);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Persona>> obtenerPersonas() {
        List<Persona> personas = personaService.obtenerPersonas();
        return ResponseEntity.ok(personas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> obtenerPersonaPorId(@PathVariable Long id) {
        Optional<Persona> personaOpt = personaService.obtenerPersonaPorId(id);
        if (personaOpt.isPresent()) {
            return ResponseEntity.ok(personaOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Persona> actualizarPersona(@PathVariable Long id, @RequestBody Persona persona) {
        Optional<Persona> personaOpt = personaService.obtenerPersonaPorId(id);
        if (personaOpt.isPresent()) {
            Persona personaActualizada = personaService.actualizarPersona(persona);
            return ResponseEntity.ok(personaActualizada);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPersona(@PathVariable Long id) {
        try {
            personaService.eliminarPersona(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/buscar/identificacion/{identificacion}")
    public ResponseEntity<Persona> buscarPorIdentificacion(@PathVariable Integer identificacion) {
        Optional<Persona> personaOpt = personaService.buscarPorIdentificacion(identificacion);
        if (personaOpt.isPresent()) {
            return ResponseEntity.ok(personaOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/buscar/edad/{edad}")
    public ResponseEntity<List<Persona>> buscarPorEdad(@PathVariable Integer edad) {
        List<Persona> personas = personaService.buscarPorEdad(edad);
        return ResponseEntity.ok(personas);
    }

    @GetMapping("/buscar/pnombre/{pnombre}")
    public ResponseEntity<List<Persona>> buscarPorPnombre(@PathVariable String pnombre) {
        List<Persona> personas = personaService.buscarPorPnombre(pnombre);
        return ResponseEntity.ok(personas);
    }

    @GetMapping("/buscar/papellido/{papellido}")
    public ResponseEntity<List<Persona>> buscarPorPapellido(@PathVariable String papellido) {
        List<Persona> personas = personaService.buscarPorPapellido(papellido);
        return ResponseEntity.ok(personas);
    }
}
