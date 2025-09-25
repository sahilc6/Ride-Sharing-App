package com.ridesharing.ride_sharing_backend.dto;

import java.util.List;

public class MatchingResponseDTO {
    private List<MatchResultDTO> matches;
    private Integer totalCost;
    private String status;
    private String message;
    
    // Constructors
    public MatchingResponseDTO() {}
    
    public MatchingResponseDTO(List<MatchResultDTO> matches, Integer totalCost, String status, String message) {
        this.matches = matches;
        this.totalCost = totalCost;
        this.status = status;
        this.message = message;
    }
    
    // Getters and Setters
    public List<MatchResultDTO> getMatches() {
        return matches;
    }
    
    public void setMatches(List<MatchResultDTO> matches) {
        this.matches = matches;
    }
    
    public Integer getTotalCost() {
        return totalCost;
    }
    
    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "MatchingResponseDTO{" +
                "matches=" + matches +
                ", totalCost=" + totalCost +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
