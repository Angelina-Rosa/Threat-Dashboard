package com.threatintel.service;

import com.threatintel.model.AbuseIpDbResponse;

import java.net.http.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//Handles all communication with the AbuseIPDB external API

@Service
public class AbuseIpDbService {

    private static final Logger log = LoggerFactory.getLogger(AbuseIpDbService.class);

    @Value("${abuseipdb.api.key}")
    private String apiKey;

    @Value("${abuseipdb.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public AbuseIpDbService() {
        this.restTemplate = new RestTemplate();
    }

    public AbuseIpDbResponse checkIp(String ipAddress) {
        try {
            // Build request headers... this requires the API key in the header
            HttpHeaders headers = new HttpHeaders();
            headers.set("Key", apiKey);
            headers.set("Accept", "application/json");

            // Build the full URL with query parameters
            // maxAgeInDays=90 means: include reports from the last 90 days
            String url = apiUrl + "?ipAddress=" + ipAddress + "&maxAgeInDays=90&verbose";

            HttpEntity<String> entity = new HttpEntity<>(headers);

            log.info("Querying AbuseIPDB for IP: {}", ipAddress);

            // Make the GET request + deserialize JSON 
            ResponseEntity<AbuseIpDbResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                AbuseIpDbResponse.class
            );

            log.info("AbuseIPDB response status: {}", response.getStatusCode());
            return response.getBody();

        } catch (Exception e) {
            log.error("Failed to query AbuseIPDB for IP {}: {}", ipAddress, e.getMessage());
            return null;
        }
    }
}
