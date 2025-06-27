package com.resiliente.controller;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
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

    // URL de tu CDN de Cloudflare
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

            this.s3Client = AmazonS3ClientBuilder.standard()
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                            endpoint, region))
                    .withCredentials(new AWSStaticCredentialsProvider(
                            new BasicAWSCredentials(accessKey, secretKey)))
                    .withPathStyleAccessEnabled(true) // Importante para Wasabi
                    .build();

            // Probar conexión
            System.out.println("Cliente S3 inicializado correctamente");

            // Verificar si el bucket existe
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

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "general") String folder) {

        System.out.println("=== INICIANDO SUBIDA DE ARCHIVO ===");
        System.out.println("Archivo: " + file.getOriginalFilename());
        System.out.println("Tamaño: " + file.getSize() + " bytes");
        System.out.println("Tipo: " + file.getContentType());
        System.out.println("Carpeta: " + folder);

        try {
            // Validaciones
            if (file.isEmpty()) {
                throw new RuntimeException("El archivo está vacío");
            }

            // Generar nombre único para el archivo
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            // Crear la ruta completa con carpeta
            String fullPath = folder + "/" + uniqueFileName;
            System.out.println("Ruta completa: " + fullPath);

            // Configurar metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            metadata.setCacheControl("public, max-age=31536000"); // 1 año

            System.out.println("Subiendo archivo a Wasabi...");

            // Subir archivo a Wasabi
            PutObjectRequest putRequest = new PutObjectRequest(
                    bucketName,
                    fullPath,
                    file.getInputStream(),
                    metadata
            );

            s3Client.putObject(putRequest);
            System.out.println("✅ Archivo subido exitosamente");

            // Generar URLs
            String cdnUrl = CDN_BASE_URL + "/" + fullPath;
            String wasabiUrl = "https://" + bucketName + ".s3." + region + ".wasabisys.com/" + fullPath;

            System.out.println("URL CDN: " + cdnUrl);
            System.out.println("URL Wasabi: " + wasabiUrl);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("cdnUrl", cdnUrl);
            response.put("wasabiUrl", wasabiUrl);
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

    @GetMapping("/file-info/{folder}/{fileName}")
    public ResponseEntity<Map<String, Object>> getFileInfo(
            @PathVariable String folder,
            @PathVariable String fileName) {

        try {
            String fullPath = folder + "/" + fileName;

            // Verificar si el archivo existe
            boolean exists = s3Client.doesObjectExist(bucketName, fullPath);

            if (!exists) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Archivo no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            // Obtener metadata del archivo
            ObjectMetadata metadata = s3Client.getObjectMetadata(bucketName, fullPath);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("cdnUrl", CDN_BASE_URL + "/" + fullPath);
            response.put("fileName", fileName);
            response.put("folder", folder);
            response.put("size", metadata.getContentLength());
            response.put("contentType", metadata.getContentType());
            response.put("lastModified", metadata.getLastModified());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error al obtener info del archivo: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Endpoint de prueba para verificar conectividad
    @GetMapping("/test-connection")
    public ResponseEntity<Map<String, Object>> testConnection() {
        Map<String, Object> response = new HashMap<>();

        try {
            // Probar listado de objetos (solo los primeros 5)
            var objects = s3Client.listObjects(bucketName);

            response.put("success", true);
            response.put("message", "Conexión exitosa con Wasabi");
            response.put("bucketName", bucketName);
            response.put("objectCount", objects.getObjectSummaries().size());
            response.put("region", region);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("details", e.getClass().getSimpleName());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
