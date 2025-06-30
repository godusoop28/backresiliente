package com.resiliente.controller;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class WasabiController {

    private AmazonS3 s3Client;

    @Value("${wasabi.accessKey}")
    private String accessKey;

    @Value("${wasabi.secretKey}")
    private String secretKey;

    @Value("${wasabi.region}")
    private String region;

    @Value("${wasabi.endpoint}")
    private String endpoint;

    @Value("${wasabi.bucketName}")
    private String bucketName;

    // ✅ CORREGIDO: URL del CDN de Cloudflare
    private static final String CDN_BASE_URL = "https://cdn.proyectoresiliente.org";

    @PostConstruct
    public void init() {
        try {
            System.out.println("=== CONFIGURACIÓN WASABI ===");
            System.out.println("Access Key: " + accessKey);
            System.out.println("Secret Key: " + (secretKey != null ? secretKey.substring(0, 4) + "****" : "null"));
            System.out.println("Region: " + region);
            System.out.println("Endpoint: " + endpoint);
            System.out.println("Bucket: " + bucketName);
            System.out.println("CDN URL: " + CDN_BASE_URL);

            this.s3Client = AmazonS3ClientBuilder.standard()
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                            endpoint, region))
                    .withCredentials(new AWSStaticCredentialsProvider(
                            new BasicAWSCredentials(accessKey, secretKey)))
                    .withPathStyleAccessEnabled(true)
                    .build();

            System.out.println("Cliente S3 inicializado correctamente");

            if (s3Client.doesBucketExistV2(bucketName)) {
                System.out.println("✅ Bucket '" + bucketName + "' existe y es accesible");
            } else {
                System.out.println("❌ Bucket '" + bucketName + "' no existe o no es accesible");
            }

        } catch (Exception e) {
            System.err.println("❌ Error al inicializar cliente Wasabi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ✅ ENDPOINT CORREGIDO: Cambiado de /upload-to-wasabi a /upload
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "productos") String folder) {

        System.out.println("=== INICIANDO SUBIDA DE ARCHIVO ===");
        System.out.println("Archivo: " + file.getOriginalFilename());
        System.out.println("Tamaño: " + file.getSize() + " bytes");
        System.out.println("Tipo: " + file.getContentType());
        System.out.println("Carpeta: " + folder);

        try {
            if (file.isEmpty()) {
                throw new RuntimeException("El archivo está vacío");
            }

            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            String fullPath = folder + "/" + uniqueFileName;

            System.out.println("Ruta completa: " + fullPath);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            metadata.setCacheControl("public, max-age=31536000");

            System.out.println("Subiendo archivo a Wasabi...");

            PutObjectRequest putRequest = new PutObjectRequest(
                    bucketName,
                    fullPath,
                    file.getInputStream(),
                    metadata
            );

            s3Client.putObject(putRequest);
            System.out.println("✅ Archivo subido exitosamente");

            // ✅ IMPORTANTE: Devolver URL del CDN
            String cdnUrl = CDN_BASE_URL + "/" + fullPath;

            System.out.println("URL CDN: " + cdnUrl);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("fileUrl", cdnUrl); // ✅ Para compatibilidad con React Native
            response.put("cdnUrl", cdnUrl);
            response.put("fileName", uniqueFileName);
            response.put("originalName", originalFileName);
            response.put("folder", folder);
            response.put("size", file.getSize());
            response.put("contentType", file.getContentType());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Error al subir archivo: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            errorResponse.put("details", e.getClass().getSimpleName());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{folder}/{fileName}")
    public ResponseEntity<Map<String, Object>> deleteFile(
            @PathVariable String folder,
            @PathVariable String fileName) {

        try {
            String fullPath = folder + "/" + fileName;
            System.out.println("Eliminando archivo: " + fullPath);

            s3Client.deleteObject(bucketName, fullPath);
            System.out.println("✅ Archivo eliminado exitosamente");

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Archivo eliminado correctamente");
            response.put("deletedFile", fullPath);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error al eliminar archivo: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/test-connection")
    public ResponseEntity<Map<String, Object>> testConnection() {
        Map<String, Object> response = new HashMap<>();

        try {
            var objects = s3Client.listObjects(bucketName);

            response.put("success", true);
            response.put("message", "Conexión exitosa con Wasabi");
            response.put("bucketName", bucketName);
            response.put("objectCount", objects.getObjectSummaries().size());
            response.put("region", region);
            response.put("cdnUrl", CDN_BASE_URL);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("details", e.getClass().getSimpleName());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
