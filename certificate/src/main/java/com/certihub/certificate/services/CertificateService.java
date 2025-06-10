package com.certihub.certificate.services;

import com.certihub.certificate.models.Certificate;
import com.certihub.certificate.repositories.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CertificateService {
    @Autowired
    private CertificateRepository certificateRepository;
    
    @Autowired
    private FileStorageService fileStorageService;

    // Add a new certificate
    public Certificate saveCertificate(Certificate certificate, MultipartFile file){
        String fileName = fileStorageService.storeFile(file);
        certificate.setCertificateName(fileName);
        return certificateRepository.save(certificate);
    }

    // Retrieve all certificates
    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }

    // Retrieve a certificate by ID
    public Optional<Certificate> getCertificateById(Long id) {
        return certificateRepository.findById(id);
    }

    // Retrieve expired certificates
    public Optional<Certificate> getExpiredCertificates() {
        return certificateRepository.findByExpiryDateBefore(LocalDate.now());
    }

}
