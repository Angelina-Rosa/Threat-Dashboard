package com.threatintel.service;

import com.threatintel.model.AbuseIpDbResponse;
import com.threatintel.model.IpLookup;
import com.threatintel.repository.IpLookupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Orchestrates the IP lookup workflow:
 * 1. Calls AbuseIPDB via AbuseIpDbService
 * 2. Saves the result to PostgreSQL via IpLookupRepository
 * 3. Provides lookup history to the controller
 */
@Service
public class IpLookupService {

    private static final Logger log = LoggerFactory.getLogger(IpLookupService.class);

    private final AbuseIpDbService abuseIpDbService;
    private final IpLookupRepository repository;

    public IpLookupService(AbuseIpDbService abuseIpDbService, IpLookupRepository repository) {
        this.abuseIpDbService = abuseIpDbService;
        this.repository = repository;
    }

    /**
     * Performs a full IP lookup: queries AbuseIPDB, saves to DB, returns result.
     *
     * @param ipAddress the IP to check
     * @return the saved IpLookup entity, or null if the API call failed
     */
    public IpLookup lookup(String ipAddress) {
        // Call the external API
        AbuseIpDbResponse response = abuseIpDbService.checkIp(ipAddress);

        if (response == null || response.getData() == null) {
            log.warn("No data returned from AbuseIPDB for IP: {}", ipAddress);
            return null;
        }

        AbuseIpDbResponse.IpData data = response.getData();

        // Map API response to our database entity
        IpLookup lookup = new IpLookup(
            data.getIpAddress(),
            data.getAbuseConfidenceScore(),
            data.getCountryCode(),
            data.getIsp(),
            data.getDomain(),
            data.getTotalReports(),
            data.isWhitelisted()
        );

        // Persist to PostgreSQL
        IpLookup saved = repository.save(lookup);
        log.info("Saved lookup for IP {} with abuse score {}", ipAddress, data.getAbuseConfidenceScore());

        return saved;
    }

    /**
     * Returns the 20 most recent IP lookups for the dashboard history table.
     */
    public List<IpLookup> getRecentLookups() {
        return repository.findTop20ByOrderByLookedUpAtDesc();
    }

    /**
     * Returns all previous lookups for a specific IP address.
     */
    public List<IpLookup> getHistoryForIp(String ipAddress) {
        return repository.findByIpAddressOrderByLookedUpAtDesc(ipAddress);
    }
}
