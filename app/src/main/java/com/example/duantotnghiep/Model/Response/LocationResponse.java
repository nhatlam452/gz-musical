package com.example.duantotnghiep.Model.Response;

import com.example.duantotnghiep.Model.Location;

import java.util.List;

public class LocationResponse {
    private String status;
    private String message;
    private List<Location> results;

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

    public List<Location> getResults() {
        return results;
    }

    public void setResults(List<Location> results) {
        this.results = results;
    }

    public LocationResponse(String status, String message, List<Location> results) {
        this.status = status;
        this.message = message;
        this.results = results;

    }
}
