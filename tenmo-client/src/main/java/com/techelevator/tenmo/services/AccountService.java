package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class AccountService {

    String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    private String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    public double updateBalance() {
        String url = API_BASE_URL + "balance/";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Double> response = restTemplate.exchange(url, HttpMethod.GET, entity, Double.class);
        return response.getBody();
    }

    public String getUserName(int accountId) {
        String url = API_BASE_URL + "users/" + accountId + "/username";
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), String.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            // Handle specific exception (e.g., log, display error message, etc.)
            return null; // Return a default value or handle the error case accordingly
        } catch (Exception e) {
            // Handle other exceptions (e.g., log, display error message, etc.)
            return null; // Return a default value or handle the error case accordingly
        }
    }

    public int getAccountNumberByUserId(User user) {
        String url = API_BASE_URL + "users/" + user.getId() + "/account";
        ResponseEntity<Integer> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), Integer.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            // Handle specific exception (e.g., log, display error message, etc.)
            return -1; // Return a default value or handle the error case accordingly
        } catch (Exception e) {
            // Handle other exceptions (e.g., log, display error message, etc.)
            return -1; // Return a default value or handle the error case accordingly
        }
    }
}
