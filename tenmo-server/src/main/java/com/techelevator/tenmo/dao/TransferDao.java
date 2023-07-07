package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    void sendTransfer(Transfer transfer);
    BigDecimal getAccountBalance(int userId);
    List<User> getAvailableUsers(int senderId);
    boolean isTransferAllowed(int senderId, int receiverId, BigDecimal amount);
    void updateAccountBalance(int userId, BigDecimal newBalance);

}


