package com.example.demo.repository;
import com.example.demo.entity.Users;
import com.example.demo.entity.VendorProfile;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface VendorProfileRepository extends JpaRepository<VendorProfile, Long>
{
    Optional<VendorProfile> findByVendor(Users vendor);
    Optional<VendorProfile> findByVendorId(Long vendorId);
}