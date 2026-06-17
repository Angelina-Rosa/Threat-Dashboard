package com.threatintel.repository;

import com.threatintel.model.IpLookup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for IpLookup entities.
 * JpaRepository provides save(), findById(), findAll(), delete(), etc. for free.
 * Custom query methods are derived automatically from their method names.
 */
@Repository
public interface IpLookupRepository extends JpaRepository<IpLookup, Long> {

    // Returns the 20 most recent lookups for the dashboard history table
    List<IpLookup> findTop20ByOrderByLookedUpAtDesc();

    // Check if we've already looked up this IP before
    boolean existsByIpAddress(String ipAddress);

    // Get all previous lookups for a specific IP (for repeat-check history)
    List<IpLookup> findByIpAddressOrderByLookedUpAtDesc(String ipAddress);
}
