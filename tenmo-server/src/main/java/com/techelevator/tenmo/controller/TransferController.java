package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private TransferDao transferDao;

    public TransferController(JdbcTemplate jdbcTemplate) {
        this.transferDao = new JdbcTransferDao(jdbcTemplate);
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public ResponseEntity<String> sendTransfer(@RequestBody Transfer transfer) {
        try {
            int senderId = transfer.getAccountFrom();
            int receiverId = transfer.getAccountTo();
            BigDecimal amount = transfer.getAmount();

            // Check if transfer is allowed based on criteria
            if (!transferDao.isTransferAllowed(senderId, receiverId, amount)) {
                return ResponseEntity.badRequest().body("Invalid transfer request.");
            }

            // Update sender's balance
            BigDecimal senderBalance = transferDao.getAccountBalance(senderId);
            BigDecimal newSenderBalance = senderBalance.subtract(amount);
            transferDao.updateAccountBalance(senderId, newSenderBalance);

            // Update receiver's balance
            BigDecimal receiverBalance = transferDao.getAccountBalance(receiverId);
            BigDecimal newReceiverBalance = receiverBalance.add(amount);
            transferDao.updateAccountBalance(receiverId, newReceiverBalance);

            // Save the transfer details
            transferDao.sendTransfer(transfer);

            return ResponseEntity.ok("Transfer successful.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}