package com.resiliente.security;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // aplica a todos los endpoints
                        .allowedOrigins("http://localhost:5173", "https://tusitio.com") // pon aquí tus dominios frontend
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ✅ CONFIGURACIÓN CORS ACTUALIZADA PARA PRODUCCIÓN
        configuration.setAllowedOriginPatterns(Arrays.asList(
                // Desarrollo local
                "*",
                "http://localhost:5173",
                "http://localhost:3000",
                "http://localhost:3001",
                "http://localhost:8080",
                "http://localhost:8081",
                // Tu API en Railway
                "https://backresiliente-production.up.railway.app",
                // Plataformas de despliegue comunes
                "https://*.vercel.app",
                "https://*.netlify.app",
                "https://*.railway.app",
                "https://*.render.com",
                // Dominios personalizados (agregar cuando tengas)
                "https://tu-dominio.com",
                "https://www.tu-dominio.com"
        ));

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
                        // Health check y root - PÚBLICO
                        .requestMatchers("/health", "/").permitAll()

                        // Autenticación - PÚBLICO
                        .requestMatchers("/auth/**").permitAll()

                        // Visualización pública (GET) - TODOS pueden ver
                        .requestMatchers(HttpMethod.GET, "/productos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/productos-tienda/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/meseros/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/talleres/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/publicaciones/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/senas/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/condiciones/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/candidatos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/juegos/**").permitAll()

                        // APIs especiales - PÚBLICO
                        .requestMatchers("/api/**").permitAll() // Para Wasabi
                        .requestMatchers("/debug/**").permitAll() // Para debug

                        // ==================== SOLO ADMIN ====================
                        // Gestión de usuarios y roles - SOLO ADMIN
                        .requestMatchers("/usuarios/**").hasRole("ADMIN")
                        .requestMatchers("/roles/**").hasRole("ADMIN")

                        // ==================== EMPLEADO Y ADMIN ====================
                        // Gestión de productos (CUD) - EMPLEADO y ADMIN
                        .requestMatchers(HttpMethod.POST, "/productos/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/productos/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/productos/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/productos/**").hasAnyRole("EMPLEADO", "ADMIN")

                        // Gestión de productos tienda (CUD) - EMPLEADO y ADMIN
                        .requestMatchers(HttpMethod.POST, "/productos-tienda/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/productos-tienda/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/productos-tienda/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/productos-tienda/**").hasAnyRole("EMPLEADO", "ADMIN")

                        // Gestión de meseros (CUD) - EMPLEADO y ADMIN
                        .requestMatchers(HttpMethod.POST, "/meseros/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/meseros/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/meseros/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/meseros/**").hasAnyRole("EMPLEADO", "ADMIN")

                        // Gestión de talleres (CUD) - EMPLEADO y ADMIN
                        .requestMatchers(HttpMethod.POST, "/talleres/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/talleres/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/talleres/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/talleres/**").hasAnyRole("EMPLEADO", "ADMIN")

                        // Gestión de publicaciones (CUD) - EMPLEADO y ADMIN
                        .requestMatchers(HttpMethod.POST, "/publicaciones/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/publicaciones/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/publicaciones/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/publicaciones/**").hasAnyRole("EMPLEADO", "ADMIN")

                        // Gestión de señas (CUD) - EMPLEADO y ADMIN
                        .requestMatchers(HttpMethod.POST, "/senas/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/senas/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/senas/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/senas/**").hasAnyRole("EMPLEADO", "ADMIN")

                        // Gestión de condiciones (CUD) - EMPLEADO y ADMIN
                        .requestMatchers(HttpMethod.POST, "/condiciones/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/condiciones/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/condiciones/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/condiciones/**").hasAnyRole("EMPLEADO", "ADMIN")

                        // Gestión de candidatos (CUD) - EMPLEADO y ADMIN
                        .requestMatchers(HttpMethod.POST, "/candidatos/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/candidatos/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/candidatos/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/candidatos/**").hasAnyRole("EMPLEADO", "ADMIN")

                        // Gestión de juegos (CUD) - EMPLEADO y ADMIN
                        .requestMatchers(HttpMethod.POST, "/juegos/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/juegos/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/juegos/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/juegos/**").hasAnyRole("EMPLEADO", "ADMIN")

                        // Cualquier otra solicitud requiere autenticación
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
