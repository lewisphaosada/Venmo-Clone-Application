package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            Long tempAcctId = results.getLong("account_id");
            int tempUserId = results.getInt("user_id");
            BigDecimal tempBalance = results.getBigDecimal("balance");

            account = new Account(tempAcctId, tempUserId, tempBalance);
        }
        return account;
    }
    @Override
    public Account getAccountById(long accountId) {
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            long accountFrom = results.getLong("account_id");
            int userFrom = results.getInt("user_id");
            BigDecimal balance = results.getBigDecimal("balance");
            return new Account(accountFrom, userFrom, balance);
        } else {
            return null;
        }
    }

}
