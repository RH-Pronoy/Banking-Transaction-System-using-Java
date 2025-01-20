package bank;

//Banking Project with MySQL Integration
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingProject {

private static final Scanner input = new Scanner(System.in);
private static Connection connection;

// Establish Database Connection
static void connectDatabase() {
   try {
       connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_db", "root", "");
   } catch (SQLException e) {
       System.out.println("Database connection failed: " + e.getMessage());
       System.exit(1);
   }
}

// User Signup
static void signup() {
   try {
       System.out.println("Enter your phone number: ");
       String phone = input.nextLine();

       System.out.println("Enter your name: ");
       String name = input.nextLine();

       System.out.println("Enter your address: ");
       String address = input.nextLine();

       System.out.println("Enter your PIN: ");
       String pin = input.nextLine();

       System.out.println("Enter your Bkash number: ");
       String bkash = input.nextLine();

       System.out.println("Enter your Nagad number: ");
       String nagad = input.nextLine();

       System.out.println("Select your bank (1. Bank A, 2. Bank B, 3. Bank C, 4. Bank D): ");
       int bankChoice = input.nextInt();
       input.nextLine(); // Consume newline
       String bankName = switch (bankChoice) {
           case 1 -> "Bank A";
           case 2 -> "Bank B";
           case 3 -> "Bank C";
           case 4 -> "Bank D";
           default -> null;
       };

       if (bankName == null) {
           System.out.println("Invalid bank selection.");
           return;
       }

       long randomBalance = (long) (Math.random() * 100000);

       String query = "INSERT INTO users (phone, name, address, pin, bkash, nagad, bank, balance) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
       PreparedStatement stmt = connection.prepareStatement(query);
       stmt.setString(1, phone);
       stmt.setString(2, name);
       stmt.setString(3, address);
       stmt.setString(4, pin);
       stmt.setString(5, bkash);
       stmt.setString(6, nagad);
       stmt.setString(7, bankName);
       stmt.setLong(8, randomBalance);

       stmt.executeUpdate();
       System.out.println("Signup successful! Initial balance: " + randomBalance);

   } catch (SQLException e) {
       System.out.println("Signup failed: " + e.getMessage());
   }
}

// User Login
static void login() {
   try {
       System.out.println("Enter your phone number: ");
       String phone = input.nextLine();

       System.out.println("Enter your PIN: ");
       String pin = input.nextLine();

       String query = "SELECT * FROM users WHERE phone = ? AND pin = ?;";
       PreparedStatement stmt = connection.prepareStatement(query);
       stmt.setString(1, phone);
       stmt.setString(2, pin);

       ResultSet rs = stmt.executeQuery();

       if (rs.next()) {
           System.out.println("Login successful! Welcome, " + rs.getString("name") + ".");
           mainMenu(rs.getString("phone"));
       } else {
           System.out.println("Invalid credentials.");
       }

   } catch (SQLException e) {
       System.out.println("Login failed: " + e.getMessage());
   }
}

// Main Menu
static void mainMenu(String phone) {
   while (true) {
       System.out.println("\nMain Menu");
       System.out.println("1. Check Balance");
       System.out.println("2. Mobile Recharge");
       System.out.println("3. Bank Transfer");
       System.out.println("4. Logout");
       System.out.print("Select an option: ");
       
       int choice = input.nextInt();
       input.nextLine(); // Consume newline

       switch (choice) {
           case 1 -> checkBalance(phone);
           case 2 -> mobileRecharge(phone);
           case 3 -> bankTransfer(phone);
           case 4 -> {
               System.out.println("Logged out successfully.");
               return;
           }
           default -> System.out.println("Invalid choice.");
       }
   }
}

// Check Balance
static void checkBalance(String phone) {
   try {
       String query = "SELECT balance FROM users WHERE phone = ?;";
       PreparedStatement stmt = connection.prepareStatement(query);
       stmt.setString(1, phone);

       ResultSet rs = stmt.executeQuery();
       if (rs.next()) {
           System.out.println("Your balance: " + rs.getLong("balance"));
       }

   } catch (SQLException e) {
       System.out.println("Error fetching balance: " + e.getMessage());
   }
}

// Mobile Recharge
static void mobileRecharge(String phone) {
   try {
       System.out.println("Enter the amount to recharge: ");
       long amount = input.nextLong();

       String query = "SELECT balance FROM users WHERE phone = ?;";
       PreparedStatement stmt = connection.prepareStatement(query);
       stmt.setString(1, phone);

       ResultSet rs = stmt.executeQuery();
       if (rs.next()) {
           long balance = rs.getLong("balance");

           if (balance >= amount) {
               String updateQuery = "UPDATE users SET balance = balance - ? WHERE phone = ?;";
               PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
               updateStmt.setLong(1, amount);
               updateStmt.setString(2, phone);

               updateStmt.executeUpdate();
               System.out.println("Recharge successful. Remaining balance: " + (balance - amount));
           } else {
               System.out.println("Insufficient balance.");
           }
       }

   } catch (SQLException e) {
       System.out.println("Recharge failed: " + e.getMessage());
   }
}

// Bank Transfer
static void bankTransfer(String phone) {
   try {
       System.out.println("Enter recipient phone number: ");
       String recipientPhone = input.nextLine();

       System.out.println("Enter the amount to transfer: ");
       long amount = input.nextLong();

       // Fetch sender and recipient balances
       String query = "SELECT balance FROM users WHERE phone = ?;";
       PreparedStatement stmt = connection.prepareStatement(query);
       stmt.setString(1, phone);

       ResultSet rs = stmt.executeQuery();
       if (rs.next()) {
           long senderBalance = rs.getLong("balance");

           if (senderBalance >= amount) {
               // Update sender balance
               String updateSenderQuery = "UPDATE users SET balance = balance - ? WHERE phone = ?;";
               PreparedStatement updateSenderStmt = connection.prepareStatement(updateSenderQuery);
               updateSenderStmt.setLong(1, amount);
               updateSenderStmt.setString(2, phone);
               updateSenderStmt.executeUpdate();

               // Update recipient balance
               String updateRecipientQuery = "UPDATE users SET balance = balance + ? WHERE phone = ?;";
               PreparedStatement updateRecipientStmt = connection.prepareStatement(updateRecipientQuery);
               updateRecipientStmt.setLong(1, amount);
               updateRecipientStmt.setString(2, recipientPhone);
               updateRecipientStmt.executeUpdate();

               System.out.println("Transfer successful. Amount sent: " + amount);
           } else {
               System.out.println("Insufficient balance.");
           }
       }

   } catch (SQLException e) {
       System.out.println("Transfer failed: " + e.getMessage());
   }
}
public static void main(String[] args) {
	    connectDatabase();

	    while (true) {
	        System.out.println("Welcome to the Banking System");
	        System.out.println("1. Signup");
	        System.out.println("2. Login");
	        System.out.println("3. Exit");
	        System.out.print("Select an option: ");

	        int choice = input.nextInt();
	        input.nextLine(); // Consume newline

	        switch (choice) {
	            case 1 -> signup();
	            case 2 -> login();
	            case 3 -> {
	                System.out.println("Exiting the application. Goodbye!");
	                System.exit(0);
	            }
	            default -> System.out.println("Invalid choice. Please try again.");
	        }
	    }
	}
}
