package banking;

import java.sql.*;


public class DataBase {

    static final String DEFAULT_PATH = "";
    String url;
    String currentTable;

    public DataBase(String dataFile) {
        this.url = "jdbc:sqlite:" + DEFAULT_PATH + dataFile;
    }

    public void createTableIfNotExists(String tableName, String columns) {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
                + columns
                + ");";
        execute(sql);

    }


    void delete(String accountNumber) {
        String sql = "DELETE FROM " + currentTable
                + " WHERE number = " + accountNumber
                + "\n;";
        execute(sql);
    }


    void addToBalance(String accountNumber, int value) {
        String sql = "UPDATE " + currentTable
                + " SET balance = balance +" + value
                + " WHERE number = " + accountNumber
                + "\n;";
        execute(sql);
    }

    void execute(String sql) {
        try {
            DriverManager.getConnection(url)
                    .prepareStatement(sql).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    int getBalance(String accountNumber) {
        int balance = 0;
        String queryResult = select("balance", accountNumber);
        if (!queryResult.equals("")) balance = Integer.parseInt(queryResult);
        return balance;
    }

    public String getPin(String accountNumber) {
        String pin = select("pin", accountNumber);
        return pin;
    }

    String select(String column, String accountNumber) {
        String value = "";
        String sql = "SELECT " + column + " FROM " + currentTable
                + " WHERE number=" + accountNumber
                + "\n;";
        try (
                Connection conn = DriverManager.getConnection(url);
                ResultSet resultSet = conn.prepareStatement(sql).executeQuery();
        ) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int type = metaData.getColumnType(1);
            switch (type) {
                case 4:
                    value += (resultSet.getInt(1));
                    break;
                case 12:
                    value = resultSet.getString(1);
                    break;
                default:
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    public int getMaxId() {
        String sql = "SELECT MAX(id) FROM " + currentTable;
        int maxId = 0;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement statement = conn.prepareStatement(sql)
        ) {
            ResultSet result = statement.executeQuery();
            maxId = result.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxId;
    }

    public void insert(CreditCard card) {
        String sql = "INSERT INTO " + currentTable + "(id,number,pin,balance) " + "VALUES( ?,?,?,?);";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, card.id);
            statement.setString(2, card.getNumber());
            statement.setString(3, card.getPin());
            statement.setInt(4, card.getBalance());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}

