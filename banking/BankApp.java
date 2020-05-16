package banking;

import java.util.Scanner;

public class BankApp {
    private Bank bank;
    Scanner scanner = new Scanner(System.in);

    BankApp(String dataBaseName) {
        bank = new Bank(dataBaseName);
    }


    void openMainMenu() {

        int command;
        do {
            System.out.println("1. Create account\n" +
                    "2. Log into account\n" +
                    "0. Exit\n");
            command = scanner.nextInt();
            scanner.nextLine();
            switch (command) {
                case 1:
                    bank.createNewAccount();
                    break;
                case 2:
                    openLogInDialog();
                    break;
                default:
                    System.out.println("Wrong option");
                case 0:
                    exit();
                    break;
            }
        } while (command != 0);
    }

    void openLogInDialog() {
        System.out.println("Enter your card number:");
        Scanner scanner = new Scanner(System.in);
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();
        if (bank.tryLogIntoAccount(cardNumber, pin)) {
            openAccountMenu();
        } else System.out.println("Wrong card number or PIN!\n");
    }

    int openAccountMenu() {
        System.out.println("You have successfully logged in!\n");
        int command;
        do {
            System.out.println("1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit\n");
            command = scanner.nextInt();
            switch (command) {
                case 1:
                    showBalance();
                    break;
                case 2:
                    openAddIncomeDialog();
                    break;
                case 3:
                    openTransferDialog();
                    break;
                case 4:
                    closeAccount();
                    break;
                case 5:
                    logOut();
                    break;
                case 0:
                    exit();
                default:
                    System.out.println("Wrong option");
                    break;
            }
        } while (!(command == 5));
        return command;
    }

    void openAddIncomeDialog() {
        System.out.println("How much money do you want to deposit?");
        int income = scanner.nextInt();
        bank.depositToCurrentCard(income);

    }

    void openTransferDialog() {
        scanner.nextLine();
        System.out.println("What account do you want to transfer to?");
        String accountNumber = scanner.nextLine();
        if (canTransferTo(accountNumber)) {
            System.out.println("How much money do you want to transfer?");
            int money = scanner.nextInt();
            transfer(accountNumber, money);
        }
    }

    void transfer(String to, int amount) {
        if (bank.canTransfer(amount)) {
            bank.transfer(to, amount);
        } else System.out.println("Insufficient funds.");
    }

    void closeAccount() {
        bank.closeAccount();
        System.out.println("Your account has been closed.");
    }

    boolean canTransferTo(String number) {
        boolean result = false;
        if (bank.currentAccountIsNot(number)) {
            if (CreditCard.validate(number)) {
                if (bank.cardExists(number)) {
                    result = true;
                } else System.out.println("Such a card does not exist.");
            } else System.out.println("Probably you made mistake in card number. Please try again!");
        } else System.out.println("You can't transfer money to the same account!");
        return result;
    }

    void showBalance() {
        System.out.println("Balance: " + bank.getBalance());
    }

    void exit() {
        System.out.println("Bye!");
        System.exit(0);
    }

    void logOut() {
        bank.logOutFromCurrentAccount();
        System.out.println("You have successfully logged out!\n");
    }
}
