package com.resiliente.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.HashMap;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "API Resiliente funcionando correctamente");
        response.put("timestamp", java.time.Instant.now().toString());
        response.put("version", "1.0.0");
        response.put("environment", System.getenv("RAILWAY_ENVIRONMENT"));
        return response;
    }

    // Endpoint adicional para Railway
    @GetMapping("/")
    public Map<String, Object> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "API Resiliente");
        response.put("status", "Running");
        response.put("docs", "/health para health check");
        return response;
    }
}
