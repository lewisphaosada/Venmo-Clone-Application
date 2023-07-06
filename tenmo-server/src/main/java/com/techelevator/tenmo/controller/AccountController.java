package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao){
        this.accountDao = accountDao;
        this.userDao = userDao;

    }

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public Double getBalance(Principal principal) {
        int userId = userDao.findIdByUsername(principal.getName());
        Account account = accountDao.getAccountByUserId(userId);

        Double balance = null;
        if (account != null) {
            balance = account.getBalance();
        }

        return balance;
    }
}
