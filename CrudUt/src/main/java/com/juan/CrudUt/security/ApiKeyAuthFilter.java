package com.juan.CrudUt.security;

import com.juan.CrudUt.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey = request.getHeader("x-api-key");
        String authorizationHeader = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();

        // Omitir validación para las rutas POST a /personas y /usuarios/login
        if ("POST".equals(request.getMethod()) && (requestURI.equals("/personas") || requestURI.equals("/usuarios/login"))) {
            filterChain.doFilter(request, response);
            return;
        }

        // Validar el API Key
        if (apiKey == null || !usuarioService.isValidApiKey(apiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid API Key");
            return;
        }

        // Validar que el token sea enviado en el formato adecuado (Bearer <token>)
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Missing or Malformed Token");
            return;
        }

        String token = authorizationHeader.substring(7); // Quitar el "Bearer " del token

        // Validar el token
        if (!tokenService.validateToken(token, apiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid Token");
            return;
        }

        // Si todo es válido, continuar con la cadena de filtros
        filterChain.doFilter(request, response);

        // Debugging
        System.out.println("API Key recibido: " + apiKey);
        System.out.println("Token recibido: " + token);
    }
}
