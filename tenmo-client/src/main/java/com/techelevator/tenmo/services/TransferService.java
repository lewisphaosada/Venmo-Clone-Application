package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class TransferService {
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
    private String baseUrl;

    public TransferService() {
        this.restTemplate = new RestTemplate();
        this.baseUrl = "http://localhost:8080/";
    }

    public ResponseEntity<String> sendTransfer(Transfer transfer) {
        try {


            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authToken);
            HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "transfers", entity, String.class);


            if (response.getStatusCode() == HttpStatus.BAD_REQUEST && response.getBody().contains("Cannot transfer funds to yourself.")) {
                return ResponseEntity.badRequest().body("Cannot transfer funds to yourself.");
            }


            return response;
        } catch (HttpClientErrorException e) {
            // Handle exception (e.g., log, display error message, etc.)
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            // Handle exception (e.g., log, display error message, etc.)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
