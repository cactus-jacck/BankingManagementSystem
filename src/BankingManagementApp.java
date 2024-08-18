import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingManagementApp
{
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String password = "qwerty";

    public static void main(String[] args) throws ClassNotFoundException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");

        try
        {
            String email;
            long account_number;

            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner sc = new Scanner(System.in);

            User user = new User(sc, connection);
            Accounts accounts = new Accounts(sc, connection);
            AccountManger accountManger = new AccountManger(sc, connection);

            while (true)
            {
//              main menu
                System.out.println("*** Welcome to Gen Z Bank ***");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                int choice = sc.nextInt();
                if (choice == 1)
                {
                    user.register();
//                  to send the user back to the main menu
                }
                else if (choice == 2)
                {
                    email = user.login();

                    if (email != null)
                    {
                        System.out.println();
                        System.out.println("User successfully logged in");
//                        if user does not have an account
                        if (accounts.accountExists(email))
                        {
                            System.out.println();
                            System.out.println("1. Open Account");
                            System.out.println("2. Exit");
                            System.out.println("Enter your choice");
                            int choice2 = sc.nextInt();
                            if (choice2 == 1)
                            {
                                account_number = accounts.openAccount(email);
                                System.out.println("Account successfully created");
                                System.out.println("Your account number is " + account_number);
                            }
                            else
                            {
                                return;
                            }
                        }
                        account_number = accounts.getAccountNumber(email);
                        int choice3 = 0;
                        while (choice3 != 5)
                        {
                            System.out.println();
                            System.out.println("1. Debit Money");
                            System.out.println("2. Credit Money");
                            System.out.println("3. Transfer Money");
                            System.out.println("4. Check Balance");
                            System.out.println("5. Log Out");
                            System.out.println("Enter your choice: ");
                            choice3 = sc.nextInt();

                            if (choice3 == 1)
                            {
                                accountManger.debitMoney(account_number);
                            }
                            else if (choice3 == 2)
                            {
                                accountManger.creditMoney(account_number);
                            }
                            else if (choice3 == 3)
                            {
                                accountManger.transferMoney(account_number);
                            }
                            else if (choice3 == 4)
                            {
                                accountManger.checkBalance(account_number);
                            }
                            else if (choice3 == 5)
                            {
                                break;
                            }
                            else
                            {
                                System.out.println("Enter a valid choice!!");
                            }
                        }
                    }
                    else
                    {
                        System.out.println("Incorrect email or password");
                    }
                }
                else if (choice == 3)
                {
                    System.out.println("Thank you for using Banking System");
                    System.out.println("Exiting");
                    //exit the main menu
                    break;
                }
                else
                {
                    System.out.println("Enter a valid choice");
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}