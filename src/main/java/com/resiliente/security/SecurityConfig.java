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

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtRequestFilter jwtRequestFilter) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
    }

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

        // Configuración de orígenes permitidos
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://proyectoresiliente.org",
                "https://www.proyectoresiliente.org"
        ));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
        ));

        // Headers permitidos
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With",
                "Cache-Control"
        ));

        // Headers expuestos
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Disposition"
        ));

        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // 1 hora

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Configuración CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Deshabilitar CSRF (no necesario para APIs stateless con JWT)
                .csrf(csrf -> csrf.disable())

                // Configuración de autorización
                .authorizeHttpRequests(authz -> authz
                        // Endpoints públicos
                        .requestMatchers(
                                "/",
                                "/health",
                                "/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Todos los GET son públicos
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()

                        // Endpoints de administración
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Endpoints de gestión (requieren autenticación)
                        .requestMatchers(
                                "/usuarios/**",
                                "/roles/**"
                        ).hasRole("ADMIN")

                        // Operaciones de escritura para empleados y admin
                        .requestMatchers(
                                HttpMethod.POST, "/**"
                        ).hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(
                                HttpMethod.PUT, "/**"
                        ).hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(
                                HttpMethod.PATCH, "/**"
                        ).hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(
                                HttpMethod.DELETE, "/**"
                        ).hasAnyRole("EMPLEADO", "ADMIN")

                        // Todas las demás solicitudes requieren autenticación
                        .anyRequest().authenticated()
                )

                // Manejo de excepciones
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )

                // Configuración de sesión (stateless)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // Añadir filtro JWT
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}