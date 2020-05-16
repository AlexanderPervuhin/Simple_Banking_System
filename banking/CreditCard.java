package banking;

import java.util.Random;

public class CreditCard {
    public int id;
    static final int BANKING_IIN = 400000;
    private String number;
    private String pin;
    int balance;


    public CreditCard(int id) {
        this.id = id;
        this.number = BANKING_IIN + generateAccountNum(id);
        this.number += generateCheckSumFor(number);
        this.pin = generatePIN();
    }


    static boolean validate(String cardNumber) {
        int checkSum = cardNumber.charAt(cardNumber.length() - 1);
        checkSum = Character.getNumericValue(checkSum);
        String number = cardNumber.substring(0, cardNumber.length() - 1);
        return checkSum == generateCheckSumFor(number);
    }

    static int generateCheckSumFor(String number) {
        int sum = 0;
        for (int i = 1; i <= number.length(); i++) {
            int n = number.charAt(i - 1);
            n = Character.getNumericValue(n);
            if (i % 2 != 0) n = n * 2;
            if (n > 9) n -= 9;
            sum += n;
        }
        sum = (10 - sum % 10) % 10;
        return sum;
    }

    String generateAccountNum(int id) {

        return String.format("%09d", id);
    }

    public int getBalance() {
        return balance;
    }

    void printCredentials() {
        System.out.printf("Your card number:\n%s\nYour card PIN:\n%s\n", number, pin);
    }


    String generatePIN() {
        Random rand = new Random();
        int pin = rand.nextInt(10000);
        return String.format("%04d", pin);
    }

    public String getPin() {
        return pin;
    }

    public String getNumber() {
        return number;
    }
}