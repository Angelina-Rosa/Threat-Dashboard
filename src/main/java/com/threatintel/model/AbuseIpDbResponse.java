package com.threatintel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore any extra fields AbuseIPDB returns
public class AbuseIpDbResponse {

    @JsonProperty("data")
    private IpData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IpData {

        @JsonProperty("ipAddress")
        private String ipAddress;

        @JsonProperty("abuseConfidenceScore")
        private int abuseConfidenceScore;

        @JsonProperty("countryCode")
        private String countryCode;

        @JsonProperty("isp")
        private String isp;

        @JsonProperty("domain")
        private String domain;

        @JsonProperty("totalReports")
        private int totalReports;

        @JsonProperty("isWhitelisted")
        private boolean isWhitelisted;
    }
}
