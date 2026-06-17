package com.threatintel.repository;

import com.threatintel.model.IpLookup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IpLookupRepository extends JpaRepository<IpLookup, Long> {

    // Returns the 20 most recent lookups for the dashboard history table
    List<IpLookup> findTop20ByOrderByLookedUpAtDesc();

    // Check if we've already looked up this IP before
    boolean existsByIpAddress(String ipAddress);

    // Get all previous lookups for a specific IP address, ordered by most recent first
    List<IpLookup> findByIpAddressOrderByLookedUpAtDesc(String ipAddress);
}
