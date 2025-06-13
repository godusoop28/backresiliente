package com.resiliente.security;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody AuthRequest authRequest) {
        try {
            // Autenticar usuario
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            // Cargar detalles del usuario
            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
            CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;

            // Generar token
            String token = jwtUtil.generateToken(userDetails);

            // Crear respuesta
            AuthResponse authResponse = new AuthResponse(
                token,
                customUserDetails.getUserId(),
                customUserDetails.getUsername(),
                customUserDetails.getNombre(),
                customUserDetails.getApellido(),
                customUserDetails.getRol()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("datos", authResponse);
            response.put("mensaje", "Autenticación exitosa");
            response.put("tipo", "SUCCESS");

            return ResponseEntity.ok(response);

        } catch (DisabledException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Usuario deshabilitado");
            response.put("tipo", "ERROR");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (BadCredentialsException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Credenciales inválidas");
            response.put("tipo", "ERROR");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error en la autenticación: " + e.getMessage());
            response.put("tipo", "ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<Object> validateToken(@RequestHeader("Authorization") String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                String jwtToken = token.substring(7);
                String username = jwtUtil.extractUsername(jwtToken);
                
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                if (jwtUtil.validateToken(jwtToken, userDetails)) {
                    CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
                    
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("userId", customUserDetails.getUserId());
                    userData.put("email", customUserDetails.getUsername());
                    userData.put("nombre", customUserDetails.getNombre());
                    userData.put("apellido", customUserDetails.getApellido());
                    userData.put("rol", customUserDetails.getRol());
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("datos", userData);
                    response.put("mensaje", "Token válido");
                    response.put("tipo", "SUCCESS");
                    response.put("valido", true);
                    
                    return ResponseEntity.ok(response);
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Token inválido");
            response.put("tipo", "ERROR");
            response.put("valido", false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error validando token: " + e.getMessage());
            response.put("tipo", "ERROR");
            response.put("valido", false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
