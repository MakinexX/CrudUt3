package com.juan.CrudUt.service;

import com.juan.CrudUt.entity.Usuario;
import com.juan.CrudUt.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> obtenerUsuarioPorIdPersona(Long idPersona) {
        return usuarioRepository.findById(idPersona);
    }

    public Usuario actualizarPassword(Long idPersona, String nuevoPassword) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idPersona);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setPassword(nuevoPassword);
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    public Usuario obtenerDetallesUsuario(Long idPersona) {
        return usuarioRepository.findById(idPersona).orElse(null);
    }

    // Método para generar una APIKey única para el usuario
    public String generarApiKey(Long idPersona) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idPersona);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String apiKey = UUID.randomUUID().toString();
            usuario.setApiKey(apiKey);
            usuarioRepository.save(usuario);
            return apiKey;
        }
        return null;
    }

    // Validar el APIKey usando JPQL
    public boolean isValidApiKey(String apiKey) {
        Optional<Usuario> usuario = usuarioRepository.findByApiKey(apiKey);
        return usuario.isPresent();
    }

    public Usuario findByLoginAndPassword(String login, String password) {
        // Buscar el usuario por el login
        Optional<Usuario> optionalUsuario = usuarioRepository.findByLogin(login);

        // Verificar si existe el usuario y si la contraseña coincide
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            // Comparar la contraseña (asegúrate de que estés usando el mismo método de comparación)
            if (usuario.getPassword().equals(password)) {
                return usuario; // Retornar el usuario si la contraseña es correcta
            }
        }
        return null; // Retornar null si no se encontró el usuario o la contraseña es incorrecta
    }
}
