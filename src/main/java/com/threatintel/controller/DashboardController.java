package com.threatintel.controller;

import com.threatintel.model.IpLookup;
import com.threatintel.service.IpLookupService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DashboardController {

    private final IpLookupService ipLookupService;

    public DashboardController(IpLookupService ipLookupService) {
        this.ipLookupService = ipLookupService;
    }

    /**
     * GET /... Loads the main dashboard with recent lookup history.
     */
    @GetMapping("/")
    public String dashboard(Model model) {
        List<IpLookup> recentLookups = ipLookupService.getRecentLookups();
        model.addAttribute("recentLookups", recentLookups);
        model.addAttribute("totalChecked", recentLookups.size());
        return "dashboard"; // maps to templates/dashboard.html
    }

    /**
     * POST /lookup...Accepts an IP address from the form, runs the lookup, and returns results
     */
    @PostMapping("/lookup")
    public String lookup(@RequestParam("ipAddress") String ipAddress, Model model) {

        // Basic sanitization for whitespace
        ipAddress = ipAddress.trim();

        // Perform the lookup
        IpLookup result = ipLookupService.lookup(ipAddress);

        if (result == null) {
            // Pass error message to the template
            model.addAttribute("error", "Could not retrieve data for IP: " + ipAddress +
                ". Check your API key or try again.");
        } else {
            model.addAttribute("result", result);

            // Determine risk level label for the UI badge
            String riskLevel;
            if (result.getAbuseConfidenceScore() >= 75) {
                riskLevel = "HIGH";
            } else if (result.getAbuseConfidenceScore() >= 25) {
                riskLevel = "MEDIUM";
            } else {
                riskLevel = "LOW";
            }
            model.addAttribute("riskLevel", riskLevel);
        }

        // Also reload recent history so the table stays current
        model.addAttribute("recentLookups", ipLookupService.getRecentLookups());

        return "dashboard";
    }

    /**
     * GET /history/{ip}...Returns full lookup history for a specific IP address
     */
    @GetMapping("/history/{ip}")
    public String ipHistory(@PathVariable("ip") String ipAddress, Model model) {
        List<IpLookup> history = ipLookupService.getHistoryForIp(ipAddress);
        model.addAttribute("history", history);
        model.addAttribute("ipAddress", ipAddress);
        return "history"; 
    }
}
