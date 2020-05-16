package banking;


class Bank {
    private DataBase dataBase;
    private static final String TABLE_NAME = "card";
    private String currentAccount;

    public Bank(String dataBaseName) {
        setupDataBase(dataBaseName);
    }

    public void setupDataBase(String fileName) {
        dataBase = new DataBase(fileName);
        dataBase.createTableIfNotExists(TABLE_NAME, "id INTEGER," +
                "number TEXT," +
                "pin TEXT," +
                "balance INTEGER DEFAULT 0");
        dataBase.currentTable = TABLE_NAME;
    }


    void createNewAccount() {
        int id = dataBase.getMaxId() + 1;
        CreditCard creditCard = new CreditCard(id);
        creditCard.printCredentials();
        dataBase.insert(creditCard);
    }


    boolean tryLogIntoAccount(String cardNumber, String pin) {
        boolean loggedIn = false;
        if (canLogIn(cardNumber, pin)) {
            currentAccount = cardNumber;
            loggedIn = true;
        }
        return loggedIn;
    }

    boolean canLogIn(String cardNumber, String pin) {
        boolean result = false;
        if (CreditCard.validate(cardNumber)) {
            String pinFromDb = dataBase.getPin(cardNumber);
            if (pin.equals(pinFromDb)
                    && !pin.equals("not_found"))
                result = true;
        }
        return result;
    }

    int getBalance() {
        return dataBase.getBalance(currentAccount);
    }

    public void logOutFromCurrentAccount() {
        currentAccount = "";
    }

    void depositToCurrentCard(int income) {
        dataBase.addToBalance(currentAccount, income);
    }

    boolean currentAccountIsNot(String number) {
        return !number.equals(currentAccount);
    }

    void closeAccount() {
        dataBase.delete(currentAccount);
    }

    public boolean canTransfer(int amount) {
        return getBalance() > amount;
    }

    public boolean cardExists(String number) {
        String pin = dataBase.getPin(number);
        return !pin.equals("not_found");
    }

    public void transfer(String to, int amount) {
        dataBase.addToBalance(currentAccount, -amount);
        dataBase.addToBalance(to, amount);
    }
}