package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private TransferDao transferDao;
    private AccountDao accountDao;

    public TransferController(TransferDao transferDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.POST)
    public ResponseEntity<String> sendTransfer(@RequestBody Transfer transfer) {
        try {
            int senderId = transfer.getAccountFrom();
            int receiverId = transfer.getAccountTo();
            BigDecimal amount = transfer.getAmount();

            // Retrieve sender and receiver details from the database
            Account senderAccount = accountDao.getAccountById(senderId);
            Account receiverAccount = accountDao.getAccountById(receiverId);

            if (senderAccount == null || receiverAccount == null) {
                return ResponseEntity.badRequest().body(null);
            }

            if (senderAccount.getUserId() == receiverAccount.getUserId()) {
                return ResponseEntity.badRequest().body(null);
            }

            if (!transferDao.isTransferAllowed(senderId, receiverId, amount)) {
                return ResponseEntity.badRequest().body(null);
            }

            // Update sender's balance
            BigDecimal senderBalance = transferDao.getAccountBalance(senderId);
            BigDecimal newSenderBalance = senderBalance.subtract(amount);
            transferDao.updateAccountBalance(senderId, newSenderBalance);

            // Update receiver's balance
            BigDecimal receiverBalance = transferDao.getAccountBalance(receiverId);
            BigDecimal newReceiverBalance = receiverBalance.add(amount);
            transferDao.updateAccountBalance(receiverId, newReceiverBalance);

            transferDao.sendTransfer(transfer);

            return ResponseEntity.ok("Transfer successful.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(path = "/transfers/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<Transfer>> getTransfersForUser(@PathVariable int userId) {
        List<Transfer> transfers = transferDao.getTransfersForUser(userId);

        if (transfers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(transfers);
    }
}
