package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    private String API_BASE_URL = "http://localhost:8080/";

    public TransferService() {
        this.restTemplate = new RestTemplate();
        this.API_BASE_URL = "http://localhost:8080/";
    }

    public ResponseEntity<String> sendTransfer(Transfer transfer) {
        try {
            HttpEntity<Transfer> entity = new HttpEntity<>(transfer);
            ResponseEntity<String> response = restTemplate.postForEntity(API_BASE_URL + "transfers", entity, String.class);

            if (response.getStatusCode() == HttpStatus.BAD_REQUEST && response.getBody().contains("Cannot transfer funds to yourself.")) {
                return ResponseEntity.badRequest().body("Cannot transfer funds to yourself.");
            }


            return response;
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

<<<<<<< HEAD
    public List<Transfer> getTransfersForUser(String authToken, int userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Transfer[]> response = restTemplate.exchange(
                API_BASE_URL + "transfers/{userId}", HttpMethod.GET, entity, Transfer[].class, userId);

        if (response.getStatusCode() == HttpStatus.OK) {
            Transfer[] transfers = response.getBody();
            if (transfers != null) {
                return Arrays.asList(transfers);
            }
        }
        return Collections.emptyList();
=======
    public Transfer getTransferByTransferId(int transferId, String token) {
        try {
            String url = baseUrl + "transfers/" + transferId;
            ResponseEntity<Transfer> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            // Handle specific exception (e.g., log, display error message, etc.)
            return null;
        } catch (HttpServerErrorException e) {
            // Handle specific exception (e.g., log, display error message, etc.)
            return null;
        } catch (Exception e) {
            // Handle other exceptions (e.g., log, display error message, etc.)
            return null;
        }
    }

    public List<Transfer> getUserTransfers(int accountId) {
        try {
            String url = baseUrl + "accounts/" + accountId + "/transfers";
            ResponseEntity<Transfer[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer[].class
            );
            Transfer[] transfersArray = response.getBody();
            return Arrays.asList(transfersArray);
        } catch (HttpClientErrorException e) {
            // Handle specific exception (e.g., log, display error message, etc.)
            return Collections.emptyList();
        } catch (HttpServerErrorException e) {
            // Handle specific exception (e.g., log, display error message, etc.)
            return Collections.emptyList();
        } catch (Exception e) {
            // Handle other exceptions (e.g., log, display error message, etc.)
            return Collections.emptyList();
        }
>>>>>>> 2d54a17966f741717ff1e597455b8ce0be189d84
    }
}
