package banking;


public class Main {
    public static void main(String[] args) {
        if (args[0].equals("-fileName")) {
            new BankApp(args[1]).openMainMenu();
        }
    }
}

