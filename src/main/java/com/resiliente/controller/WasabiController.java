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

@RestController
@RequestMapping("/api")
public class WasabiController {

    // Quitar la palabra 'final'
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

    @PostConstruct
    public void init() {
        // Configurar cliente de S3 para Wasabi
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        endpoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    @PostMapping("/upload-to-wasabi")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileName") String fileName) {

        try {
            // Subir archivo a Wasabi
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            s3Client.putObject(new PutObjectRequest(
                    bucketName,
                    fileName,
                    file.getInputStream(),
                    metadata
            ));

            // Generar URL del archivo
            String fileUrl = "https://" + bucketName + ".s3." + region + ".wasabisys.com/" + fileName;

            Map<String, String> response = new HashMap<>();
            response.put("fileUrl", fileUrl);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/files/{bucket}/{fileName}")
    public ResponseEntity<byte[]> getFile(
            @PathVariable String bucket,
            @PathVariable String fileName) {

        try {
            S3Object s3Object = s3Client.getObject(bucket, fileName);
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(inputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(s3Object.getObjectMetadata().getContentType()));
            headers.setContentLength(bytes.length);

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/delete-from-wasabi")
    public ResponseEntity<Map<String, String>> deleteFile(
            @RequestBody Map<String, String> request) {

        String fileName = request.get("fileName");

        try {
            s3Client.deleteObject(bucketName, fileName);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Archivo eliminado correctamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}