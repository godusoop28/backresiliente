package com.resiliente.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Value("${cors.allowed.origins:http://localhost:3000}")
    private String allowedOrigins;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // ==================== ENDPOINTS PÚBLICOS ====================
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/test-connection").permitAll() // Para health check
                        .requestMatchers(HttpMethod.GET, "/productos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/productos-tienda/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/meseros/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/talleres/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/publicaciones/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/senas/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/condiciones/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/candidatos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/juegos/**").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/debug/**").permitAll()
                        // ==================== RESTO DE TU CONFIGURACIÓN ====================
                        .requestMatchers("/usuarios/**").hasRole("ADMIN")
                        .requestMatchers("/roles/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/productos/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/productos/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/productos/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/productos/**").hasAnyRole("EMPLEADO", "ADMIN")
                        // ... resto de tu configuración igual
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
