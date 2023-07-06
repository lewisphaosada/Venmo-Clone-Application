package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountByUserId(int userId){

        Account account = null;
        String sql = "select account_id, user_id, balance from account where user_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            Long tempAcctId = results.getLong("account_id");
            int tempUserId = results.getInt("user_id");
            double tempBalance = results.getDouble("balance");

            account = new Account(tempAcctId, tempUserId, tempBalance);

        }
        return account;
    }

}
