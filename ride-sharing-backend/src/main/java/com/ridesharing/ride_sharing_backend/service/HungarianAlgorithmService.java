package com.ridesharing.ride_sharing_backend.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * Hungarian Algorithm implementation for optimal driver-rider matching
 * This service implements the Hungarian algorithm to solve the assignment problem
 * for matching drivers to riders with minimum total cost.
 */
@Service
public class HungarianAlgorithmService {
    
    /**
     * Inner class implementing the Hungarian Algorithm for assignment problems
     */
    private static class Hungarian {
        private final int n;
        private final int[][] cost;
        private final int[] u, v, p, way;
        
        Hungarian(int[][] costMatrix) {
            n = Math.max(costMatrix.length, costMatrix[0].length);
            cost = new int[n][n];
            
            // Pad to square matrix if necessary
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    cost[i][j] = (i < costMatrix.length && j < costMatrix[0].length)
                            ? costMatrix[i][j]
                            : 0;
                }
            }
            
            u = new int[n + 1];
            v = new int[n + 1];
            p = new int[n + 1];
            way = new int[n + 1];
        }
        
        /**
         * Computes the optimal assignment using Hungarian algorithm
         * @return array where assignment[i] gives the assigned task for worker i
         */
        int[] assignment() {
            for (int i = 1; i <= n; i++) {
                p[0] = i;
                int j0 = 0;
                int[] minv = new int[n + 1];
                Arrays.fill(minv, Integer.MAX_VALUE);
                boolean[] used = new boolean[n + 1];
                
                do {
                    used[j0] = true;
                    int i0 = p[j0], delta = Integer.MAX_VALUE, j1 = 0;
                    
                    for (int j = 1; j <= n; j++) {
                        if (!used[j]) {
                            int cur = cost[i0 - 1][j - 1] - u[i0] - v[j];
                            if (cur < minv[j]) {
                                minv[j] = cur;
                                way[j] = j0;
                            }
                            if (minv[j] < delta) {
                                delta = minv[j];
                                j1 = j;
                            }
                        }
                    }
                    
                    for (int j = 0; j <= n; j++) {
                        if (used[j]) {
                            u[p[j]] += delta;
                            v[j] -= delta;
                        } else {
                            minv[j] -= delta;
                        }
                    }
                    j0 = j1;
                } while (p[j0] != 0);
                
                do {
                    int j1 = way[j0];
                    p[j0] = p[j1];
                    j0 = j1;
                } while (j0 != 0);
            }
            
            int[] ans = new int[n];
            Arrays.fill(ans, -1);
            for (int j = 1; j <= n; j++) {
                if (p[j] != 0 && p[j] - 1 < ans.length) {
                    ans[p[j] - 1] = j - 1;
                }
            }
            return ans;
        }
        
        /**
         * Calculates the minimum cost for the given assignment
         * @param assign the assignment array
         * @return total minimum cost
         */
        int minCost(int[] assign) {
            int res = 0;
            for (int i = 0; i < assign.length; i++) {
                if (assign[i] >= 0 && cost[i][assign[i]] < Integer.MAX_VALUE / 2) {
                    res += cost[i][assign[i]];
                }
            }
            return res;
        }
    }
    
    /**
     * Solves the driver-rider assignment problem using Hungarian algorithm
     * @param drivers array of drivers with their locations
     * @param riders array of riders with pickup and drop locations
     * @param costList list of travel costs between locations
     * @return AssignmentResult containing optimal assignments and total cost
     */
    public AssignmentResult solveAssignment(int[][] drivers, int[][] riders, int[][] costList) {
        // Build cost map for quick lookup
        Map<String, Integer> costMap = new HashMap<>();
        for (int[] c : costList) {
            costMap.put(c[0] + "-" + c[1], c[2]);
        }
        
        int D = drivers.length;
        int R = riders.length;
        int[][] matrix = new int[D][R];
        int INF = Integer.MAX_VALUE / 2;
        
        // Fill cost matrix
        for (int i = 0; i < D; i++) {
            int driverLoc = drivers[i][1];
            for (int j = 0; j < R; j++) {
                int pickup = riders[j][0];
                int drop = riders[j][1];
                int toPickup = costMap.getOrDefault(driverLoc + "-" + pickup, INF);
                int trip = costMap.getOrDefault(pickup + "-" + drop, INF);
                matrix[i][j] = (toPickup == INF || trip == INF) ? INF : toPickup + trip;
            }
        }
        
        // Apply Hungarian algorithm
        Hungarian hungarian = new Hungarian(matrix);
        int[] assignment = hungarian.assignment();
        int totalCost = hungarian.minCost(assignment);
        
        // Build result
        List<Assignment> assignments = new ArrayList<>();
        for (int i = 0; i < D; i++) {
            if (assignment[i] >= 0 && assignment[i] < R && matrix[i][assignment[i]] < INF) {
                int driverId = drivers[i][0];
                int driverLoc = drivers[i][1];
                int riderId = riders[assignment[i]][0];
                int pickup = riders[assignment[i]][0];
                int drop = riders[assignment[i]][1];
                int cost = matrix[i][assignment[i]];
                
                assignments.add(new Assignment(driverId, driverLoc, riderId, pickup, drop, cost));
            }
        }
        
        return new AssignmentResult(assignments, totalCost);
    }
    
    /**
     * Data class representing a single driver-rider assignment
     */
    public static class Assignment {
        private final int driverId;
        private final int driverLocation;
        private final int riderId;
        private final int pickupLocation;
        private final int dropLocation;
        private final int cost;
        
        public Assignment(int driverId, int driverLocation, int riderId, 
                         int pickupLocation, int dropLocation, int cost) {
            this.driverId = driverId;
            this.driverLocation = driverLocation;
            this.riderId = riderId;
            this.pickupLocation = pickupLocation;
            this.dropLocation = dropLocation;
            this.cost = cost;
        }
        
        // Getters
        public int getDriverId() { return driverId; }
        public int getDriverLocation() { return driverLocation; }
        public int getRiderId() { return riderId; }
        public int getPickupLocation() { return pickupLocation; }
        public int getDropLocation() { return dropLocation; }
        public int getCost() { return cost; }
    }
    
    /**
     * Data class representing the complete assignment result
     */
    public static class AssignmentResult {
        private final List<Assignment> assignments;
        private final int totalCost;
        
        public AssignmentResult(List<Assignment> assignments, int totalCost) {
            this.assignments = assignments;
            this.totalCost = totalCost;
        }
        
        // Getters
        public List<Assignment> getAssignments() { return assignments; }
        public int getTotalCost() { return totalCost; }
    }
}
