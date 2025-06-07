package com.certihub.certificate.repositories;

import com.certihub.certificate.models.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate,Long> {
    Optional<Certificate> findById(Long certificateId);
    Optional<Certificate> findByCertificateName(String certificateName);
    Optional<Certificate> findByCategory(String category);
    Optional<Certificate> findByExpiryDateBefore(LocalDate expiryDate);
}
