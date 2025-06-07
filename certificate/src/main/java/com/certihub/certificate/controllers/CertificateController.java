package com.certihub.certificate.controllers;

import com.certihub.certificate.models.Certificate;
import com.certihub.certificate.services.CertificateService;
import com.certihub.certificate.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/certificate")

public class CertificateController {
    @Autowired
    private CertificateService certificateService;

    @Autowired
    private FileStorageService fileStorageService;

    // Upload a certificate with file
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Certificate> addCertificate(
            @RequestParam("certificateName") String certificateName,
            @RequestParam("issuer") String issuer,
            @RequestParam("issueDate") String issueDate,
            @RequestParam("expiryDate") String expiryDate,
            @RequestParam("category") String category,
            @RequestParam("file") MultipartFile file) {

        // Store the file and get the stored filename
        String storedFileName = fileStorageService.storeFile(file);

        // Generate the accessible URL
        String fileUrl = "http://localhost:8081/certificate/download/" + storedFileName;

        Certificate certificate = new Certificate();
        certificate.setCertificateName(certificateName);
        certificate.setIssuer(issuer);
        certificate.setIssueDate(java.time.LocalDate.parse(issueDate));
        certificate.setExpiryDate(java.time.LocalDate.parse(expiryDate));
        certificate.setCategory(category);
        certificate.setFileUrl(fileUrl);           // save download URL

        return ResponseEntity.ok(certificateService.saveCertificate(certificate, file));
    }

    // Download a certificate file
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
