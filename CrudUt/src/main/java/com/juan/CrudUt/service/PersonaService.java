package com.juan.CrudUt.service;

import com.juan.CrudUt.entity.Persona;
import com.juan.CrudUt.entity.Usuario;
import com.juan.CrudUt.repository.PersonaRepository;
import com.juan.CrudUt.repository.UsuarioRepository;
import com.juan.CrudUt.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Persona crearPersona(Persona persona) {
        // Calcular la edad y edad clínica antes de persistir
        LocalDate fechaNacimiento = new java.sql.Date(persona.getFechanacimiento().getTime()).toLocalDate();
        persona.setEdad(calcularEdad(fechaNacimiento));
        persona.setEdadclinica(calcularEdadClinica(fechaNacimiento));

        // Generar y asignar el login automáticamente
        String login = generarLogin(persona);
        Usuario usuario = new Usuario();
        usuario.setLogin(login);

        // Generar password automáticamente
        String password = generarPassword();
        usuario.setPassword(password);

        // Relacionar la persona con el usuario
        usuario.setPersona(persona);
        persona.setUsuario(usuario);

        // Guardar persona y usuario
        return personaRepository.save(persona);
    }
    
    public String registrarPersona(Persona person){
        if(person!=null & person.getId()!=null & personaRepository.findById(person.getId()).isPresent()){
            personaRepository.save(person);;
            return new TokenService().generateToken(person.getUsuario().getApiKey());
        }else{
            return "f no se pudo";
        }
    }

    private String generarLogin(Persona persona) {
        // Generar el login de acuerdo a la nemotecnia: nombre + primera letra del apellido + identificación
        return persona.getPnombre() + persona.getPapellido().charAt(0) + persona.getIdentificacion();
    }

    private String generarPassword() {
        // Generar un password aleatorio
        int length = 10;
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return password.toString();
    }

    private int calcularEdad(LocalDate fechaNacimiento) {
        // Calcular la edad en años
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    private String calcularEdadClinica(LocalDate fechaNacimiento) {
        // Calcular la edad clínica en formato años, meses y días
        Period period = Period.between(fechaNacimiento, LocalDate.now());
        return period.getYears() + " años " + period.getMonths() + " meses " + period.getDays() + " dias";
    }

    public List<Persona> obtenerPersonas() {
        // Obtener todas las personas
        return personaRepository.findAll();
    }

    public Optional<Persona> obtenerPersonaPorId(Long id) {
        // Buscar persona por ID
        return personaRepository.findById(id);
    }

    public Persona actualizarPersona(Persona persona) {
        // Actualizar la persona
        return personaRepository.save(persona);
    }

    public void eliminarPersona(Long id) {
        // Eliminar persona por ID
        personaRepository.deleteById(id);
    }

    public Optional<Persona> buscarPorIdentificacion(Integer identificacion) {
        // Buscar persona por identificación
        return personaRepository.findByIdentificacion(identificacion);
    }

    public List<Persona> buscarPorEdad(Integer edad) {
        // Buscar personas por edad
        return personaRepository.findByEdad(edad);
    }

    public List<Persona> buscarPorPnombre(String pnombre) {
        // Buscar personas por primer nombre
        return personaRepository.findByPnombre(pnombre);
    }

    public List<Persona> buscarPorPapellido(String papellido) {
        // Buscar personas por primer apellido
        return personaRepository.findByPapellido(papellido);
    }
}