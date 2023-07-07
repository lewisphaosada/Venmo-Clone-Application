package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    String authToken = null;
    private BigDecimal balance;

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

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal updateBalance() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL + "balance", HttpMethod.GET, entity, BigDecimal.class);
            return response.getBody();
        } catch (Exception e) {
            System.out.println("Failed to retrieve the current balance. Please try again later.");
            return null;
        }
    }

    public void setBalance(BigDecimal newBalance) {
        this.balance = newBalance;
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
