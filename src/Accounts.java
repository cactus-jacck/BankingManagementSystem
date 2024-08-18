import java.sql.*;
import java.util.Scanner;

public class Accounts
{
    private Connection connection;
    private Scanner sc;

    public Accounts(Scanner sc, Connection connection)
    {
        this.connection = connection;
        this.sc = sc;
    }

    public boolean accountExists(String email)
    {
        String query = "SELECT * FROM accounts WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return !resultSet.next();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public long openAccount(String email)
    {
        if(accountExists(email))
        {
            sc.nextLine();
            System.out.println("Enter your full name");
            String fullName = sc.nextLine();
            System.out.println("Enter your initial balance");
            double initialBalance = sc.nextDouble();
            System.out.println("Enter you pin");
            int pin = sc.nextInt();
            long accountNumber = generateAccountNumber();

            String query = "INSERT INTO accounts (account_number, full_name, email, balance, security_pin) VALUES(?, ?, ?, ?, ?)";

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, accountNumber);
                preparedStatement.setString(2, fullName);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, initialBalance);
                preparedStatement.setInt(5, pin);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0)
                {
                    System.out.println("Account created successfully");
                    return accountNumber;
                }
                else
                    throw new RuntimeException("Account creation failed");
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            throw new RuntimeException("Account already exists");
        }
    }

    public long getAccountNumber(String email)
    {
        String query = "SELECT account_number FROM accounts WHERE email = ?";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                return resultSet.getLong("account_number");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return 1L;
    }

    private long generateAccountNumber()
    {
        try
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT account_number from Accounts ORDER BY account_number DESC LIMIT 1");
            if (resultSet.next())
            {
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number+1;
            }
            else
            {
                return 10000100;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 10000100;
    }
}
