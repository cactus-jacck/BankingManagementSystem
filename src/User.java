import java.sql.*;
import java.util.Scanner;

public class User
{
    private Connection connection;
    private Scanner sc;

    public User(Scanner sc, Connection connection)
    {
        this.sc = sc;
        this.connection = connection;
    }

    public void register()
    {
        sc.nextLine();
        System.out.println("Enter your full name");
        String fullName = sc.nextLine();
        System.out.println("Enter your email");
        String email = sc.nextLine();

        if(userExists(email))
        {
            System.out.println("User already exists for this email");
            return;
        }

        System.out.println("Enter your password");
        String password = sc.nextLine();

        String query = "INSERT INTO user (full_name, email, password) VALUES (?, ?, ?)";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0)
            {
                System.out.println("Registration Successful");
            }
            else
            {
                System.out.println("Registration Failed");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public String login()
    {
        sc.nextLine();
        System.out.println("Enter your email id");
        String email = sc.nextLine();
        System.out.println("Enter your password");
        String password = sc.nextLine();

        String query = "SELECT * FROM user WHERE email = ? AND password = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                return resultSet.getString("email");
            }
            else
            {
                return null;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean userExists(String email)
    {
        String query = "SELECT * FROM user WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

}
