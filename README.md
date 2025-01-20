# Banking Project

This is a simple banking system implemented in Java, integrated with a MySQL database. It allows users to sign up, log in, perform balance checks, recharge their mobile accounts, and transfer money between accounts.

## Features
- **User Signup**: Allows users to register with their details, including phone number, name, address, PIN, preferred bank, and e-wallet numbers (Bkash and Nagad).
- **User Login**: Enables users to log in using their phone number and PIN.
- **Check Balance**: Users can view their current balance.
- **Mobile Recharge**: Allows users to recharge their mobile accounts.
- **Bank Transfer**: Enables users to transfer money to another registered user.

---

## Installation

### Prerequisites
1. **Java Development Kit (JDK)**: Version 8 or higher
2. **MySQL Database**: Ensure MySQL is installed and running.
3. **IDE (Optional)**: IntelliJ IDEA, Eclipse, or any other IDE.

### Steps
1. Clone the repository or download the project files.
2. Configure the database:
   - Create a database named `banking_db`.
   - Create a `users` table with the following schema:
     ```sql
     CREATE TABLE users (
         id INT AUTO_INCREMENT PRIMARY KEY,
         phone VARCHAR(15) NOT NULL UNIQUE,
         name VARCHAR(100),
         address VARCHAR(255),
         pin VARCHAR(10),
         bkash VARCHAR(15),
         nagad VARCHAR(15),
         bank VARCHAR(50),
         balance BIGINT
     );
     ```
3. Update database credentials in the `connectDatabase` method:
   ```java
   connection = DriverManager.getConnection(
       "jdbc:mysql://localhost:3306/banking_db", "root", ""
   );
Replace "root" and "" with your MySQL username and password.

4. Compile and run the project:
javac BankingProject.java
java BankingProject
