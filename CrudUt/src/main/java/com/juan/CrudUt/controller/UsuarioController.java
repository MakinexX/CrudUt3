package com.juan.CrudUt.controller;

import com.juan.CrudUt.entity.Usuario;
import com.juan.CrudUt.security.TokenService;
import com.juan.CrudUt.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private TokenService tokenService;

    @PutMapping("/cambiar-password/{idPersona}")
    public ResponseEntity<Usuario> cambiarPassword(@PathVariable Long idPersona, @RequestBody String nuevoPassword) {
        // Verificar si el usuario existe antes de cambiar la contraseña
        Usuario usuarioActualizado = usuarioService.actualizarPassword(idPersona, nuevoPassword);
        if (usuarioActualizado != null) {
            return ResponseEntity.ok(usuarioActualizado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{idPersona}")
    public ResponseEntity<Optional<Usuario>> obtenerUsuario(@PathVariable Long idPersona) {
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorIdPersona(idPersona);
        if (usuarioOpt.isPresent()) {
            return ResponseEntity.ok(usuarioOpt);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/detalles/{idPersona}")
    public ResponseEntity<Usuario> obtenerDetallesUsuario(@PathVariable Long idPersona) {
        Usuario usuario = usuarioService.obtenerDetallesUsuario(idPersona);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        String login = request.get("login");
        String password = request.get("password");

        // Buscar al usuario por login y password
        Usuario usuario = usuarioService.findByLoginAndPassword(login, password);

        if (usuario != null) {
            // Generar un token usando el apiKey del usuario
            String token = tokenService.generateToken(usuario.getApiKey());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login o contraseña incorrectos");
        }
    }


}
