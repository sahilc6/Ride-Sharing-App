package com.ridesharing.ride_sharing_backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ridesharing.ride_sharing_backend.dto.MatchingResponseDTO;
import com.ridesharing.ride_sharing_backend.service.DriverRiderMatchingService;

/**
 * REST Controller for Driver-Rider Matching API
 * Provides endpoints for optimal driver-rider assignment using Hungarian Algorithm
 */
@RestController
@RequestMapping("/api/v1/matching")
@CrossOrigin(origins = "*")
public class DriverRiderMatchingController {

    private static final Logger logger = LoggerFactory.getLogger(DriverRiderMatchingController.class);

    private final DriverRiderMatchingService matchingService;

    @Autowired
    public DriverRiderMatchingController(DriverRiderMatchingService matchingService) {
        this.matchingService = matchingService;
    }

    /**
     * Matches drivers to riders for optimal cost assignment (data fetched from DB)
     *
     * @return MatchingResponseDTO with optimal assignments and total cost
     */
    @GetMapping("/solve")
    public ResponseEntity<MatchingResponseDTO> solveMatching() {
        try {
            MatchingResponseDTO response = matchingService.matchDriversToRidersFromDB();

            if ("SUCCESS".equals(response.getStatus())) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        } catch (Exception e) {
            logger.error("Unexpected error in controller: {}", e.getMessage(), e);

            MatchingResponseDTO errorResponse = new MatchingResponseDTO(
                    null, 0, "ERROR",
                    "Internal server error: " + e.getMessage()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Health check endpoint
     *
     * @return System status information
     */
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        try {
            String status = matchingService.getSystemStatus();
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            logger.error("Error getting system status: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Service status check failed: " + e.getMessage());
        }
    }

    /**
     * Get API information
     *
     * @return API documentation and usage information
     */
    @GetMapping("/info")
    public ResponseEntity<ApiInfo> getApiInfo() {
        ApiInfo info = new ApiInfo(
                "Driver-Rider Matching API",
                "1.0.0",
                "This API provides optimal driver-rider assignment using the Hungarian Algorithm. " +
                        "Call GET /solve to run matching based on current DB data.",
                "GET /api/v1/matching/solve - Solve assignment problem",
                "GET /api/v1/matching/status - Check service status",
                "GET /api/v1/matching/info - Get API information"
        );

        return ResponseEntity.ok(info);
    }

    /**
     * Data class for API information response
     */
    public static class ApiInfo {
        private final String name;
        private final String version;
        private final String description;
        private final String solveEndpoint;
        private final String statusEndpoint;
        private final String infoEndpoint;

        public ApiInfo(String name, String version, String description,
                       String solveEndpoint, String statusEndpoint, String infoEndpoint) {
            this.name = name;
            this.version = version;
            this.description = description;
            this.solveEndpoint = solveEndpoint;
            this.statusEndpoint = statusEndpoint;
            this.infoEndpoint = infoEndpoint;
        }

        // Getters
        public String getName() {
            return name;
        }

        public String getVersion() {
            return version;
        }

        public String getDescription() {
            return description;
        }

        public String getSolveEndpoint() {
            return solveEndpoint;
        }

        public String getStatusEndpoint() {
            return statusEndpoint;
        }

        public String getInfoEndpoint() {
            return infoEndpoint;
        }
    }
}
