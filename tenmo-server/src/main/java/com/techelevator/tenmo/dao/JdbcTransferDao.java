package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void sendTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (account_from, account_to, amount) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    @Override
    public BigDecimal getAccountBalance(int userId) {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        Double balance = jdbcTemplate.queryForObject(sql, Double.class, userId);
        return BigDecimal.valueOf(balance);
    }

    @Override
    public List<User> getAvailableUsers(int senderId) {
        String sql = "SELECT user_id, username, password_hash, role FROM tenmo_user WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, senderId);
        User user = null;
        List<User> availableUsers = new ArrayList<>();
        while (results.next()) {
            user = new User();
            user.setId(results.getInt("user_id"));
            user.setUsername(results.getString("username"));
            user.setPassword(results.getString("password_hash"));
            availableUsers.add(user);
        }
        return availableUsers;
    }

    @Override
    public boolean isTransferAllowed(int senderId, int receiverId, BigDecimal amount) {
        BigDecimal senderBalance = getAccountBalance(senderId);
        return senderBalance.compareTo(amount) >= 0 && senderId != receiverId && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public void updateAccountBalance(int userId, BigDecimal newBalance) {
        String sql = "UPDATE account SET balance = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, newBalance, userId);
    }

    @Override
    public List<Transfer> getTransfersForUser(int userId) {
        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer WHERE account_from IN (SELECT account_id FROM account WHERE user_id = ?) " +
                "OR account_to IN (SELECT account_id FROM account WHERE user_id = ?)";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId, userId);

        while (rowSet.next()) {
            Transfer transfer = mapRowToTransfer(rowSet);
            transfers.add(transfer);
        }

        return transfers;
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet){
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(rowSet.getInt("account_from"));
        transfer.setAccountTo(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }
}