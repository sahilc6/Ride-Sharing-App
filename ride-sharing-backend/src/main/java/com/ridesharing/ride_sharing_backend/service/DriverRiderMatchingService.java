package com.ridesharing.ride_sharing_backend.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ridesharing.ride_sharing_backend.dto.MatchResultDTO;
import com.ridesharing.ride_sharing_backend.dto.MatchingResponseDTO;
import com.ridesharing.ride_sharing_backend.entity.Cost;
import com.ridesharing.ride_sharing_backend.entity.Driver;
import com.ridesharing.ride_sharing_backend.entity.Rider;
import com.ridesharing.ride_sharing_backend.repository.CostRepository;
import com.ridesharing.ride_sharing_backend.repository.DriverRepository;
import com.ridesharing.ride_sharing_backend.repository.RiderRepository;

/**
 * Service class responsible for driver-rider matching business logic using DB entities;
 * Location and Cost data are static, Driver and Rider data are dynamic.
 * Uses Hungarian Algorithm to find optimal assignments.
 */
@Service
public class DriverRiderMatchingService {

    private static final Logger logger = LoggerFactory.getLogger(DriverRiderMatchingService.class);

    private final HungarianAlgorithmService hungarianAlgorithmService;
    private final DriverRepository driverRepository;
    private final RiderRepository riderRepository;
    private final CostRepository costRepository;

    @Autowired
    public DriverRiderMatchingService(HungarianAlgorithmService hungarianAlgorithmService,
                                     DriverRepository driverRepository,
                                     RiderRepository riderRepository,
                                     CostRepository costRepository) {
        this.hungarianAlgorithmService = hungarianAlgorithmService;
        this.driverRepository = driverRepository;
        this.riderRepository = riderRepository;
        this.costRepository = costRepository;
    }

    /**
     * Matches available drivers to requested riders fetched from the database using stored location and cost data.
     * @return MatchingResponseDTO with optimal assignments
     */
    public MatchingResponseDTO matchDriversToRidersFromDB() {
        try {
            List<Driver> drivers = driverRepository.findByAvailable(true);
            List<Rider> riders = riderRepository.findByRequested(true);
            List<Cost> costs = costRepository.findAll();

            logger.info("Fetched {} available drivers and {} requested riders from DB", drivers.size(), riders.size());

            int[][] driverArray = convertDriversToArray(drivers);
            int[][] riderArray = convertRidersToArray(riders);
            int[][] costArray = convertCostsToArray(costs);

            validateInputData(driverArray, riderArray, costArray);

            HungarianAlgorithmService.AssignmentResult result =
                    hungarianAlgorithmService.solveAssignment(driverArray, riderArray, costArray);

            List<MatchResultDTO> matches = convertAssignmentsToDTO(result.getAssignments());

            String message = String.format("Matched %d driver(s) to rider(s) with total cost: %d", matches.size(), result.getTotalCost());

            logger.info(message);

            return new MatchingResponseDTO(matches, Integer.valueOf(result.getTotalCost()), "SUCCESS", message);

        } catch (Exception e) {
            logger.error("Error processing matching request: {}", e.getMessage(), e);
            return new MatchingResponseDTO(new ArrayList<>(), 0, "ERROR",
                    "Failed to process matching request: " + e.getMessage());
        }
    }

    // Convert driver entity list to an int array suitable for Hungarian algorithm
    private int[][] convertDriversToArray(List<Driver> drivers) {
        int[][] result = new int[drivers.size()][2];
        for (int i = 0; i < drivers.size(); i++) {
            Driver d = drivers.get(i);
            result[i][0] = d.getId();
            result[i][1] = d.getLocationId();
        }
        return result;
    }

    // Convert rider entity list to an int array suitable for Hungarian algorithm
    private int[][] convertRidersToArray(List<Rider> riders) {
        int[][] result = new int[riders.size()][3];
        for (int i = 0; i < riders.size(); i++) {
            Rider r = riders.get(i);
            result[i][0] = r.getId();
            result[i][1] = r.getPickupLocationId();
            result[i][2] = r.getDropLocationId();
        }
        return result;
    }

    // Convert cost entity list to an int array suitable for Hungarian algorithm
    private int[][] convertCostsToArray(List<Cost> costs) {
        int[][] result = new int[costs.size()][3];
        for (int i = 0; i < costs.size(); i++) {
            Cost c = costs.get(i);
            result[i][0] = c.getFromLocation();
            result[i][1] = c.getToLocation();
            result[i][2] = c.getCost();
        }
        return result;
    }

    // Convert assignment results to MatchResultDTO list for output
    private List<MatchResultDTO> convertAssignmentsToDTO(List<HungarianAlgorithmService.Assignment> assignments) {
        List<MatchResultDTO> matches = new ArrayList<>();
        for (HungarianAlgorithmService.Assignment assignment : assignments) {
            String description = String.format(
                    "Driver %d at location %d assigned to Rider %d (pickup: %d, drop: %d) with cost: %d",
                    assignment.getDriverId(),
                    assignment.getDriverLocation(),
                    assignment.getRiderId(),
                    assignment.getPickupLocation(),
                    assignment.getDropLocation(),
                    assignment.getCost()
            );
            MatchResultDTO match = new MatchResultDTO(
                    assignment.getDriverId(),
                    assignment.getDriverLocation(),
                    assignment.getRiderId(),
                    assignment.getPickupLocation(),
                    assignment.getDropLocation(),
                    assignment.getCost(),
                    description
            );
            matches.add(match);
        }
        return matches;
    }

    // Validation of input data completeness and sanity
    private void validateInputData(int[][] drivers, int[][] riders, int[][] costs) {
        if (drivers.length == 0) {
            throw new IllegalArgumentException("No drivers provided");
        }
        if (riders.length == 0) {
            throw new IllegalArgumentException("No riders provided");
        }
        if (costs.length == 0) {
            throw new IllegalArgumentException("No cost data provided");
        }
        boolean hasValidCosts = false;
        for (int[] cost : costs) {
            if (cost[2] >= 0) {
                hasValidCosts = true;
                break;
            }
        }
        if (!hasValidCosts) {
            throw new IllegalArgumentException("No valid cost data found");
        }
        logger.debug("Input validation passed - {} drivers, {} riders, {} cost entries",
                drivers.length, riders.length, costs.length);
    }

    // Status endpoint message
    public String getSystemStatus() {
        return "Driver-Rider Matching Service is running. Uses Hungarian Algorithm for optimal assignments.";
    }
}
