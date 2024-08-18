import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManger
{
    private Scanner sc;
    private Connection connection;

    public AccountManger(Scanner sc, Connection connection)
    {
        this.sc = sc;
        this.connection = connection;
    }

    public void debitMoney(long accountNumber)
    {
        System.out.println("Enter pin");
        int pin = sc.nextInt();

        String query1 = "SELECT security_pin FROM accounts WHERE account_number = ?";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query1);
            preparedStatement.setLong(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                if (pin == resultSet.getLong("security_pin"))
                {
                    connection.setAutoCommit(false);
                    System.out.println("Enter amount to be debited");
                    double amount = sc.nextDouble();
                    String query2 = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                    try
                    {
                        PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                        preparedStatement2.setDouble(1, amount);
                        preparedStatement2.setLong(2, accountNumber);

                        int rowsAffected = preparedStatement2.executeUpdate();
                        if (rowsAffected > 0)
                        {
                            System.out.println("Rs. " + amount + " debited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                        }
                        else
                        {
                            System.out.println("Transaction failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    catch (SQLException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
                else
                {
                    throw new RuntimeException("Incorrect pin");
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void creditMoney(long accountNumber)
    {
        System.out.println("Enter your pin");
        int pin = sc.nextInt();
        String query1 = "SELECT security_pin FROM accounts WHERE account_number = ?";
        try
        {
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.setLong(1, accountNumber);
            ResultSet resultSet = preparedStatement1.executeQuery();
            if (resultSet.next())
            {
                if (resultSet.getInt("security_pin") == pin)
                {
                    System.out.println("Enter the amount to be debited");
                    double amount = sc.nextDouble();

                    String query2 = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                    preparedStatement2.setDouble(1, amount);
                    preparedStatement2.setLong(2, accountNumber);
                    int affectedRows = preparedStatement2.executeUpdate();
                    if (affectedRows > 0)
                    {
                        System.out.println("Rs. " + amount + " credited successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                    }
                    else
                    {
                        System.out.println("Transaction failed");
                        connection.setAutoCommit(true);
                    }
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void transferMoney(long sendersAccountNumber)
    {
        System.out.println("Enter your pin");
        int pin = sc.nextInt();
        String query1 = "SELECT security_pin FROM accounts WHERE account_number = ?";
        try
        {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.setLong(1, sendersAccountNumber);
            ResultSet resultSet = preparedStatement1.executeQuery();
            if (resultSet.next())
            {
                if (resultSet.getInt("security_pin") == pin)
                {
                    System.out.println("Enter the amount to be transferred");
                    double amount = sc.nextDouble();
                    String debitQuery = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                    try
                    {
                        connection.setAutoCommit(false);
                        PreparedStatement preparedStatement2 = connection.prepareStatement(debitQuery);
                        preparedStatement2.setDouble(1, amount);
                        preparedStatement2.setLong(2, sendersAccountNumber);

                        int rowsAffected = preparedStatement2.executeUpdate();
                        if (rowsAffected > 0)
                        {
                            System.out.println("Enter receiver's account number");
                            long receiversAccountNumber = sc.nextLong();
                            String creditQuery = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
                            PreparedStatement preparedStatement3 = connection.prepareStatement(creditQuery);
                            preparedStatement3.setDouble(1, amount);
                            preparedStatement3.setLong(2, receiversAccountNumber);
                            int affectedRows = preparedStatement3.executeUpdate();
                            if (affectedRows > 0)
                            {
                                connection.commit();
                                connection.setAutoCommit(true);
                                System.out.println("Rs. " + amount + " transferred successfully");
                            }
                            else
                            {
                                System.out.println("Transaction failed");
                            }
                        }
                        else
                        {
                            System.out.println("Transaction failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    catch (SQLException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void checkBalance(long accountNumber)
    {
        String query = "SELECT balance FROM accounts WHERE account_number = ?";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) System.out.println(resultSet.getDouble("balance"));
            else System.out.println("Some error occurred");
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
