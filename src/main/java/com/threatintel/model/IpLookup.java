package com.threatintel.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a single IP lookup result stored in the database.
 * Each time a user checks an IP, a record is saved here.
 */
@Entity
@Table(name = "ip_lookups")
@Data                   // Lombok: generates getters, setters, toString, equals, hashCode
@NoArgsConstructor      // Lombok: generates no-args constructor (required by JPA)
public class IpLookup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The IP address that was looked up
    @Column(nullable = false)
    private String ipAddress;

    // AbuseIPDB confidence score: 0 (clean) to 100 (definitely malicious)
    private int abuseConfidenceScore;

    // Country code where the IP is registered (e.g. "US", "CN")
    private String countryCode;

    // Internet Service Provider name
    private String isp;

    // Domain associated with the IP
    private String domain;

    // Total number of times this IP has been reported on AbuseIPDB
    private int totalReports;

    // Whether AbuseIPDB has this IP whitelisted
    private boolean isWhitelisted;

    // When this lookup was performed
    @Column(nullable = false)
    private LocalDateTime lookedUpAt;

    // Convenience constructor for creating a new lookup record
    public IpLookup(String ipAddress, int abuseConfidenceScore, String countryCode,
                    String isp, String domain, int totalReports, boolean isWhitelisted) {
        this.ipAddress = ipAddress;
        this.abuseConfidenceScore = abuseConfidenceScore;
        this.countryCode = countryCode;
        this.isp = isp;
        this.domain = domain;
        this.totalReports = totalReports;
        this.isWhitelisted = isWhitelisted;
        this.lookedUpAt = LocalDateTime.now();
    }
}
