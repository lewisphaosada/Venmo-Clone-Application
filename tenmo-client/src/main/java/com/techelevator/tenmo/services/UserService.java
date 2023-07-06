package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserService {
    String authToken = null;


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
    private RestTemplate restTemplate;
    private String API_BASE_URL = "http://localhost:8080/";

    public UserService() {
        this.restTemplate = new RestTemplate();
        this.API_BASE_URL = "http://localhost:8080/";
    }
    public List<User> getUserAccounts(AuthenticatedUser authenticatedUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<User[]> response = restTemplate.exchange(
                API_BASE_URL + "users", HttpMethod.GET, entity, User[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            User[] accounts = response.getBody();
            if (accounts != null) {
                return Arrays.asList(accounts);
            }
        }
        return Collections.emptyList();
    }
}