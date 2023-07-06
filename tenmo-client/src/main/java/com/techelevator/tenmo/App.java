package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AccountService accountService = new AccountService();

    private TransferService transferService = new TransferService();

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
            // your code


        } else {
            String token = currentUser.getToken();
            accountService.setAuthToken(token);
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        try {
            Double balance = accountService.updateBalance();
            System.out.println("Your current balance is: " + balance);
        } catch (Exception e) {
            System.out.println("Failed to retrieve the current balance. Please try again later.");
        }
    }

    private void viewTransferHistory() {
        // TODO Auto-generated method stub

    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub

    }
    private void requestBucks() {
        // TODO Auto-generated method stub

    }

    private void sendBucks() {
        if (currentUser == null) {
            System.out.println("User not authenticated.");
            return;
        }

        UserService userService = new UserService();
        List<User> accounts = userService.getUserAccounts(currentUser);
        if (accounts.isEmpty()) {
            System.out.println("No user accounts found.");
            return;
        }

        System.out.println("Available user accounts:");
        for (int i = 0; i < accounts.size(); i++) {
            User account = accounts.get(i);
            System.out.println((i + 1) + ". " + account.getUsername());
        }

        int receiverIndex = consoleService.promptForMenuSelection("Enter the index of the user to send TE Bucks to: ");
        if (receiverIndex < 0 || receiverIndex >= accounts.size()) {
            System.out.println("Invalid user index.");
            return;
        }

        BigDecimal amount = consoleService.promptForBigDecimal("Enter the amount to send: ");

        User receiver = accounts.get(receiverIndex - 1); // Adjust index by subtracting 1

        Transfer transfer = new Transfer();
        transfer.setAccountFrom(currentUser.getUser().getId());
        transfer.setAccountTo(receiver.getId());
        transfer.setAmount(amount);

        boolean success = transferService.sendTransfer(transfer).hasBody();
        if (success) {
            System.out.println("Transfer successful.");
        } else {
            System.out.println("Transfer failed.");
        }
    }
}

