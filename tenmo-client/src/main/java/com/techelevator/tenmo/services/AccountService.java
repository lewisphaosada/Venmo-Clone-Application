package com.techelevator.tenmo.services;

import io.cucumber.java.eo.Do;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AccountService {

    String baseUrl = "http://localhost:8080";

    RestTemplate restTemplate = new RestTemplate();
    String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    public double getBalace() {
        double balance = 0;

        ResponseEntity<Double> response = restTemplate.exchange(baseUrl + "/balance", HttpMethod.GET,
                makeAuthEntity(),Double.class);

        Double tmpNum = response.getBody();
        balance = tmpNum.doubleValue();
        return balance;

    }

}
