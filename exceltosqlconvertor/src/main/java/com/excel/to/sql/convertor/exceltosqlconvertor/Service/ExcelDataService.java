package com.excel.to.sql.convertor.exceltosqlconvertor.Service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONObject;
import org.json.JSONString;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelDataService {
    public boolean createSchemaFromExcelFiles() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdatabase", "root", "sweety44");
            Statement statement = connection.createStatement();
            String createAccountsTableSQL = "CREATE TABLE Accounts (" +
                    "AccountID VARCHAR(50) PRIMARY KEY, " +
                    "CustomerID INT, " +
                    "AccountType VARCHAR(50), " +
                    "AccountBalance DECIMAL(10,2), " +
                    "AccountStatus VARCHAR(20), " +
                    "InterestRate DECIMAL(5,2), " +
                    "OpeningDate DATE, " +
                    "LastTransactionDate DATE"+")" ;
            String createCustomerTableSQL = "CREATE TABLE Customers ( CustomerID INT PRIMARY KEY, FirstName VARCHAR(50), LastName VARCHAR(50), DateOfBirth DATE, Gender VARCHAR(10), Address VARCHAR(255), ContactInformation VARCHAR(100), KYCInformation VARCHAR(255))";
            // Execute SQL statement
            String createLoansTableSQL = "CREATE TABLE Loans (LoanID VARCHAR(50) PRIMARY KEY,CustomerID INT,LoanType VARCHAR(50),LoanAmount DECIMAL(10,2),InterestRate DECIMAL(5,2),LoanStatus VARCHAR(20),EMI DECIMAL(10,2),LoanTerm INT,DisbursementDate DATE,RepaymentSchedule VARCHAR(100),FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID))";
            String createInvestmentAccountsTableSQL="CREATE TABLE InvestmentAccounts (InvestmentAccountID VARCHAR(50) PRIMARY KEY,CustomerID INT,AccountType VARCHAR(50),InvestmentAmount DECIMAL(10,2),InvestmentStatus VARCHAR(20),InvestmentStartDate DATE,InvestmentEndDate DATE,Returns DECIMAL(10,2),InvestmentPortfolio VARCHAR(100),FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID))";
            String createMutualFundsTableSQL="CREATE TABLE MutualFunds (MutualFundID INT PRIMARY KEY,FundName VARCHAR(100),FundManager VARCHAR(100),FundType VARCHAR(50),NAV DECIMAL(10,2),InvestmentAmount DECIMAL(10,2),InvestmentDate DATE,InvestmentAccountID VARCHAR(50),FOREIGN KEY (InvestmentAccountID) REFERENCES InvestmentAccounts(InvestmentAccountID))";
            String createFixedDepositsTableSQL="CREATE TABLE FixedDeposits (FixedDepositID INT PRIMARY KEY,InvestmentAccountID VARCHAR(50),PrincipalAmount DECIMAL(10,2),InterestRate DECIMAL(5,2),MaturityDate DATE,InterestPaymentFrequency VARCHAR(50),MaturityAmount DECIMAL(10,2),FOREIGN KEY (InvestmentAccountID) REFERENCES InvestmentAccounts(InvestmentAccountID))";
            String createStocksTableSQL="CREATE TABLE Stocks (StockID INT PRIMARY KEY,StockSymbol VARCHAR(100),StockName VARCHAR(100),StockExchange VARCHAR(50),PurchasePrice DECIMAL(10,2),PurchaseDate DATE,Quantity INT,InvestmentAccountID VARCHAR(50),CurrentPrice DECIMAL(10,2),FOREIGN KEY (InvestmentAccountID) REFERENCES InvestmentAccounts(InvestmentAccountID))";
            String createTransactionsTableSQL="CREATE TABLE Transactions (TransactionID INT PRIMARY KEY,AccountID VARCHAR(50),TransactionType VARCHAR(50),Amount DECIMAL(10, 2),TransactionDate DATE,TransactionStatus VARCHAR(50),Remarks VARCHAR(255),FOREIGN KEY (AccountID) REFERENCES Accounts(AccountID))";
            statement.executeUpdate(createAccountsTableSQL);
            statement.executeUpdate(createCustomerTableSQL);
            statement.executeUpdate(createInvestmentAccountsTableSQL);
            statement.executeUpdate(createMutualFundsTableSQL);
            statement.executeUpdate(createFixedDepositsTableSQL);
            statement.executeUpdate(createLoansTableSQL);
            statement.executeUpdate(createStocksTableSQL);
            statement.executeUpdate(createTransactionsTableSQL);

            // Add more logic to create other tables

            return true; // Return true if schema creation is successful, false otherwise
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
//    public List<JSONObject> getCustomerTransactions() {
//        Connection connection = null;
//        Statement statement = null;
//        ResultSet resultSet = null;
//        List<JSONObject> jsonObjectList = new ArrayList<>();
//
//        try {
//            // Establish the database connection
//            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdatabase", "root", "sweety44");
//
//            // Create SQL query
//            String sql = "SELECT c.CustomerID, c.FirstName, c.LastName, t.TransactionDate, t.Amount, t.TransactionType " +
//                    "FROM Customers c " +
//                    "JOIN ( " +
//                    "    SELECT c.CustomerID, " +
//                    "    COALESCE(SUM(a.AccountBalance), 0) + COALESCE(SUM(s.Quantity * s.CurrentPrice), 0) " +
//                    "    + COALESCE(SUM(mf.InvestmentAmount), 0) + COALESCE(SUM(fd.MaturityAmount), 0) AS TotalAssets " +
//                    "    FROM Customers c " +
//                    "    LEFT JOIN Accounts a ON c.CustomerID = a.CustomerID " +
//                    "    LEFT JOIN InvestmentAccounts ia ON c.CustomerID = ia.CustomerID " +
//                    "    LEFT JOIN Stocks s ON ia.InvestmentAccountID = s.InvestmentAccountID " +
//                    "    LEFT JOIN MutualFunds mf ON ia.InvestmentAccountID = mf.InvestmentAccountID " +
//                    "    LEFT JOIN FixedDeposits fd ON ia.InvestmentAccountID = fd.InvestmentAccountID " +
//                    "    GROUP BY c.CustomerID " +
//                    "    ORDER BY TotalAssets DESC " +
//                    "    LIMIT 5 " +
//                    ") top_customers ON c.CustomerID = top_customers.CustomerID " +
//                    "LEFT JOIN Accounts a ON c.CustomerID = a.CustomerID " +
//                    "LEFT JOIN Transactions t ON a.AccountID = t.AccountID " +
//                    "WHERE t.TransactionDate <= NOW() " +
//                    "ORDER BY c.CustomerID, t.TransactionDate";
//
//            // Create a statement
//            statement = connection.createStatement();
//
//            // Execute the query
//            resultSet = statement.executeQuery(sql);
//
//            // Process the result set
//            while (resultSet.next()) {
//                int customerId = resultSet.getInt("CustomerID");
//                String firstName = resultSet.getString("FirstName");
//                String lastName = resultSet.getString("LastName");
//                Date transactionDate = resultSet.getDate("TransactionDate");
//                double amount = resultSet.getDouble("Amount");
//                String transactionType = resultSet.getString("TransactionType");
//
//                // Do something with retrieved data
////                JSONObject customerTransactionJson = new JSONObject();
////                customerTransactionJson.put("CustomerID", customerId);
////                customerTransactionJson.put("FirstName", firstName);
////                customerTransactionJson.put("LastName", lastName);
////                customerTransactionJson.put("TransactionDate", transactionDate);
////                customerTransactionJson.put("Amount", amount);
////                customerTransactionJson.put("TransactionType", transactionType);
////                jsonObjectList.add(customerTransactionJson);
//                String ss = "{\"CustomerID\": " + customerId + ", \"FirstName\": \"" + firstName +
//                        "\", \"LastName\": \"" + lastName + "\", \"TransactionDate\": \"" + transactionDate +
//                        "\", \"Amount\": " + amount + ", \"TransactionType\": \"" + transactionType + "\"}";
//
//// Converting the string representation to a JSONObject
//                JSONObject jsonObject = new JSONObject(ss);
//                System.out.println(jsonObject.toString());
//                jsonObjectList.add(jsonObject);
//            }
//            return jsonObjectList;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
////            return jsonObjectList;
//        }
//    }
//    public boolean importDataFromExcelFiles() {
//        // Logic to import data from Excel files to respective tables
//        // Read Excel files and use JDBC to insert data into tables
//
//        // Example: Import data into the Accounts table from the Accounts.xlsx file
////        String importAccountsDataSQL = "INSERT INTO Accounts (AccountID, CustomerID, AccountType, AccountBalance, " +
////                "AccountStatus, InterestRate, OpeningDate, LastTransactionDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
////
////        File accountsExcelFile = new File("E:\\Downloads\\accounts.xlsx");
//        // Read data from the Excel file and execute the SQL INSERT statement using jdbcTemplate
//
//        // Add more logic to import data into other tables
//        try {
//            String excelFilePath = "E:\\Downloads\\accounts.xlsx"; // Replace with your file path
//
//            // Establish database connection
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdatabase", "root", "sweety44");
//
//            FileInputStream excelFile = new FileInputStream(new File(excelFilePath));
//            Workbook workbook = WorkbookFactory.create(excelFile);
//
//            Sheet sheet = workbook.getSheetAt(0); // Assuming data is on the first sheet
//
//            String insertQueryAccounts = "INSERT INTO Accounts (AccountID, CustomerID, AccountType, AccountBalance, AccountStatus, InterestRate, OpeningDate, LastTransactionDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//            PreparedStatement preparedStatement = connection.prepareStatement(insertQueryAccounts);
//            String insertQueryCustomer = "INSERT INTO Customers (CustomerID, FirstName, LastName, DateOfBirth, Gender, Address, ContactInformation, KYCInformation) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//
//
//            for (Row currentRow : sheet) {
//                // Skip header row
//                if (currentRow.getRowNum() == 0) {
//                    continue;
//                }
//
//                // Set values from Excel to PreparedStatement
//                preparedStatement.setString(1, currentRow.getCell(0).getStringCellValue());
//                preparedStatement.setInt(2, (int) currentRow.getCell(1).getNumericCellValue());
//                preparedStatement.setString(3, currentRow.getCell(2).getStringCellValue());
//                preparedStatement.setDouble(4, currentRow.getCell(3).getNumericCellValue());
//                preparedStatement.setString(5, currentRow.getCell(4).getStringCellValue());
//                preparedStatement.setDouble(6, currentRow.getCell(5).getNumericCellValue());
//                preparedStatement.setDate(7, java.sql.Date.valueOf(currentRow.getCell(6).getLocalDateTimeCellValue().toLocalDate()));
//                preparedStatement.setDate(8, java.sql.Date.valueOf(currentRow.getCell(7).getLocalDateTimeCellValue().toLocalDate()));
//
//                // Execute the query
//                preparedStatement.executeUpdate();
//            }
//
//            // Close resources
//            preparedStatement.close();
//            connection.close();
//            workbook.close();
//            excelFile.close();
//
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
    public boolean importDataFromExcelFiles() {
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdatabase", "root", "sweety44");

            // Import data into Accounts table
            String excelFilePathAccounts = "E:\\Downloads\\accounts.xlsx"; // Replace with your Accounts file path
            importDataForTable(connection, excelFilePathAccounts, "Accounts", "INSERT INTO Accounts (AccountID, CustomerID, AccountType, AccountBalance, AccountStatus, InterestRate, OpeningDate, LastTransactionDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            // Import data into Customers table
            String excelFilePathCustomers = "E:\\Downloads\\customers.xlsx"; // Replace with your Customers file path
            importDataForTable(connection, excelFilePathCustomers, "Customers", "INSERT INTO Customers (CustomerID, FirstName, LastName, DateOfBirth, Gender, Address, ContactInformation, KYCInformation) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

//             Import data into InvestmentAccounts table
            String excelFilePathInvestmentAccounts = "E:\\Downloads\\investment_accounts.xlsx"; // Replace with your InvestmentAccounts file path
            importDataForTable(connection, excelFilePathInvestmentAccounts, "InvestmentAccounts", "INSERT INTO InvestmentAccounts (InvestmentAccountID, CustomerID, AccountType, InvestmentAmount, InvestmentStatus, InvestmentStartDate, InvestmentEndDate, Returns, InvestmentPortfolio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

//             Import data into Loans table
            String excelFilePathLoans = "E:\\Downloads\\loans.xlsx"; // Replace with your Loans file path
            importDataForTable(connection, excelFilePathLoans, "Loans", "INSERT INTO Loans (LoanID, CustomerID, LoanType, LoanAmount, InterestRate, LoanStatus, EMI, LoanTerm, DisbursementDate, RepaymentSchedule) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            // Import data into Stocks table
            String excelFilePathStocks = "E:\\Downloads\\stocks.xlsx"; // Replace with your Stocks file path
            importDataForTable(connection, excelFilePathStocks, "Stocks", "INSERT INTO Stocks (StockID, StockSymbol, StockName, StockExchange, PurchasePrice, PurchaseDate, Quantity, InvestmentAccountID,CurrentPrice) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)");

//             Import data into FixedDeposits table
            String excelFilePathFixedDeposits = "E:\\Downloads\\fixed_deposits.xlsx"; // Replace with your FixedDeposits file path
            importDataForTable(connection, excelFilePathFixedDeposits, "FixedDeposits", "INSERT INTO FixedDeposits (FixedDepositID, InvestmentAccountID, PrincipalAmount, InterestRate, MaturityDate, InterestPaymentFrequency, MaturityAmount) VALUES (?, ?, ?, ?, ?, ?, ?)");

            String excelFilePathMutualFunds = "E:\\Downloads\\mutual_funds.xlsx"; // Replace with your Mutual Funds file path
            importDataForTable(connection, excelFilePathMutualFunds, "MutualFunds", "INSERT INTO MutualFunds (MutualFundID, FundName, FundManager, FundType, NAV, InvestmentAmount, InvestmentDate, InvestmentAccountID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            String excelFilePathTransactions = "E:\\Downloads\\transactions.xlsx"; // Replace with your Transactions file path
            importDataForTable(connection, excelFilePathTransactions, "Transactions", "INSERT INTO Transactions (TransactionID, AccountID, TransactionType, Amount, TransactionDate, TransactionStatus, Remarks) VALUES (?, ?, ?, ?, ?, ?, ?)");


            // Close connection
            connection.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void importDataForTable(Connection connection, String excelFilePath, String tableName, String insertQuery) throws IOException, SQLException {
        FileInputStream excelFile = new FileInputStream(new File(excelFilePath));
        Workbook workbook = WorkbookFactory.create(excelFile);
        Sheet sheet = workbook.getSheetAt(0); // Assuming data is on the first sheet

        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

        for (Row currentRow : sheet) {
            if (currentRow.getRowNum() == 0) {
                continue; // Skip header row
            }

            // Set values from Excel to PreparedStatement for the respective table
            switch (tableName) {
                case "Accounts":
                    // Set values for Accounts table
                    // Example:
                    preparedStatement.setString(1, currentRow.getCell(0).getStringCellValue());
                    preparedStatement.setInt(2, (int) currentRow.getCell(1).getNumericCellValue());
                    // Set other values accordingly for Accounts table
                    preparedStatement.setString(3, currentRow.getCell(2).getStringCellValue());
                    preparedStatement.setDouble(4, currentRow.getCell(3).getNumericCellValue());
                    preparedStatement.setString(5, currentRow.getCell(4).getStringCellValue());
                    preparedStatement.setDouble(6, currentRow.getCell(5).getNumericCellValue());
                    preparedStatement.setDate(7, java.sql.Date.valueOf(currentRow.getCell(6).getLocalDateTimeCellValue().toLocalDate()));
                    preparedStatement.setDate(8, java.sql.Date.valueOf(currentRow.getCell(7).getLocalDateTimeCellValue().toLocalDate()));
                    break;
                case "Customers":
                    preparedStatement.setInt(1, (int) currentRow.getCell(0).getNumericCellValue());
                    preparedStatement.setString(2, currentRow.getCell(1).getStringCellValue());
                    preparedStatement.setString(3, currentRow.getCell(2).getStringCellValue());
                    preparedStatement.setDate(4, java.sql.Date.valueOf(currentRow.getCell(3).getLocalDateTimeCellValue().toLocalDate()));
                    preparedStatement.setString(5, currentRow.getCell(4).getStringCellValue());
                    preparedStatement.setString(6, currentRow.getCell(5).getStringCellValue());
                    preparedStatement.setString(7, currentRow.getCell(6).getStringCellValue());
                    preparedStatement.setString(8, currentRow.getCell(7).getStringCellValue());
                    break;

                case "InvestmentAccounts":
                    preparedStatement.setString(1, currentRow.getCell(0).getStringCellValue());
                    preparedStatement.setInt(2, (int) currentRow.getCell(1).getNumericCellValue());
                    preparedStatement.setString(3, currentRow.getCell(2).getStringCellValue());
                    preparedStatement.setDouble(4, 1000*Math.random());
                    preparedStatement.setString(5, currentRow.getCell(3).getStringCellValue());
                    preparedStatement.setDate(6, java.sql.Date.valueOf(currentRow.getCell(4).getLocalDateTimeCellValue().toLocalDate()));
                    preparedStatement.setDate(7, java.sql.Date.valueOf(currentRow.getCell(5).getLocalDateTimeCellValue().toLocalDate()));
                    preparedStatement.setDouble(8, currentRow.getCell(6).getNumericCellValue());
                    preparedStatement.setString(9, currentRow.getCell(7).getStringCellValue());
                    break;

                case "Loans":
                    preparedStatement.setString(1, currentRow.getCell(0).getStringCellValue());
                    preparedStatement.setInt(2, (int) currentRow.getCell(1).getNumericCellValue());
                    preparedStatement.setString(3, currentRow.getCell(2).getStringCellValue());
                    preparedStatement.setDouble(4, currentRow.getCell(3).getNumericCellValue());
                    preparedStatement.setDouble(5, currentRow.getCell(4).getNumericCellValue());
                    preparedStatement.setString(6, currentRow.getCell(5).getStringCellValue());
                    preparedStatement.setDouble(7, currentRow.getCell(6).getNumericCellValue());
                    preparedStatement.setInt(8, (int) currentRow.getCell(7).getNumericCellValue());
                    preparedStatement.setDate(9, java.sql.Date.valueOf(currentRow.getCell(8).getLocalDateTimeCellValue().toLocalDate()));
                    preparedStatement.setString(10, currentRow.getCell(9).getStringCellValue());
                    break;

                case "Stocks":
                    preparedStatement.setInt(1, (int) currentRow.getCell(0).getNumericCellValue());
                    preparedStatement.setString(2, currentRow.getCell(1).getStringCellValue());
                    preparedStatement.setString(3, currentRow.getCell(2).getStringCellValue());
                    preparedStatement.setString(4, currentRow.getCell(3).getStringCellValue());
                    preparedStatement.setDouble(5, currentRow.getCell(4).getNumericCellValue());
                    preparedStatement.setDate(6, java.sql.Date.valueOf(currentRow.getCell(5).getLocalDateTimeCellValue().toLocalDate()));
                    preparedStatement.setInt(7, (int) currentRow.getCell(6).getNumericCellValue());
                    preparedStatement.setString(8, currentRow.getCell(7).getStringCellValue());
                    preparedStatement.setDouble(9, currentRow.getCell(8).getNumericCellValue());
                    break;

                case "FixedDeposits":
                    preparedStatement.setInt(1, (int) currentRow.getCell(0).getNumericCellValue());
                    preparedStatement.setString(2, currentRow.getCell(1).getStringCellValue());
                    preparedStatement.setDouble(3, currentRow.getCell(2).getNumericCellValue());
                    preparedStatement.setDouble(4, currentRow.getCell(3).getNumericCellValue());
                    preparedStatement.setDate(5, java.sql.Date.valueOf(currentRow.getCell(4).getLocalDateTimeCellValue().toLocalDate()));
                    preparedStatement.setString(6, currentRow.getCell(5).getStringCellValue());
                    preparedStatement.setDouble(7, currentRow.getCell(6).getNumericCellValue());
                    break;

                case "MutualFunds":
                    // Set values for MutualFunds table
                    preparedStatement.setInt(1, (int) currentRow.getCell(0).getNumericCellValue());
                    preparedStatement.setString(2, currentRow.getCell(1).getStringCellValue());
                    preparedStatement.setString(3, currentRow.getCell(2).getStringCellValue());
                    preparedStatement.setString(4, currentRow.getCell(3).getStringCellValue());
                    preparedStatement.setDouble(5, currentRow.getCell(4).getNumericCellValue());
                    preparedStatement.setDouble(6, currentRow.getCell(5).getNumericCellValue());
                    preparedStatement.setDate(7, java.sql.Date.valueOf(currentRow.getCell(6).getLocalDateTimeCellValue().toLocalDate()));
                    preparedStatement.setString(8, currentRow.getCell(7).getStringCellValue());
                    break;
                case "Transactions":
                    preparedStatement.setInt(1, (int) currentRow.getCell(0).getNumericCellValue());
                    preparedStatement.setString(2, currentRow.getCell(1).getStringCellValue());
                    preparedStatement.setString(3, currentRow.getCell(2).getStringCellValue());
                    preparedStatement.setDouble(4, currentRow.getCell(3).getNumericCellValue());
                    preparedStatement.setDate(5, java.sql.Date.valueOf(currentRow.getCell(4).getLocalDateTimeCellValue().toLocalDate()));
                    preparedStatement.setString(6, currentRow.getCell(5).getStringCellValue());
                    preparedStatement.setString(7, "");
                    break;
                // Add other cases for different tables as needed
            }

            // Execute the query
            preparedStatement.executeUpdate();
        }

        // Close resources
        preparedStatement.close();
        workbook.close();
        excelFile.close();
    }
    public List<JSONObject> getCustomerTransactions() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<JSONObject> jsonObjectList = new ArrayList<>();

        try {
            // Establish the database connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdatabase", "root", "sweety44");

            // Create SQL query
            String sql = "SELECT \n" +
                    "    c.CustomerID,\n" +
                    "    c.FirstName,\n" +
                    "    c.LastName,\n" +
                    "    t.TransactionDate,\n" +
                    "    t.Amount,\n" +
                    "    t.TransactionType\n" +
                    "FROM Customers c\n" +
                    "JOIN (\n" +
                    "    SELECT \n" +
                    "        c.CustomerID,\n" +
                    "        COALESCE(SUM(a.AccountBalance), 0) \n" +
                    "        + COALESCE(SUM(s.Quantity * s.CurrentPrice), 0) \n" +
                    "        + COALESCE(SUM(mf.InvestmentAmount), 0) \n" +
                    "        + COALESCE(SUM(fd.MaturityAmount), 0) AS TotalAssets\n" +
                    "    FROM Customers c\n" +
                    "    LEFT JOIN Accounts a ON c.CustomerID = a.CustomerID\n" +
                    "    LEFT JOIN InvestmentAccounts ia ON c.CustomerID = ia.CustomerID\n" +
                    "    LEFT JOIN Stocks s ON ia.InvestmentAccountID = s.InvestmentAccountID\n" +
                    "    LEFT JOIN MutualFunds mf ON ia.InvestmentAccountID = mf.InvestmentAccountID\n" +
                    "    LEFT JOIN FixedDeposits fd ON ia.InvestmentAccountID = fd.InvestmentAccountID\n" +
                    "    GROUP BY c.CustomerID\n" +
                    "    ORDER BY TotalAssets DESC\n" +
                    "    LIMIT 5\n" +
                    ") top_customers ON c.CustomerID = top_customers.CustomerID\n" +
                    "LEFT JOIN Accounts a ON c.CustomerID = a.CustomerID\n" +
                    "LEFT JOIN Transactions t ON a.AccountID = t.AccountID\n" +
                    "WHERE t.TransactionDate <= NOW() -- Modify this condition according to your database's date column\n" +
                    "ORDER BY c.CustomerID, t.TransactionDate";

            // Create a prepared statement
            preparedStatement = connection.prepareStatement(sql);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                int customerId = resultSet.getInt("CustomerID");
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                Date transactionDate = resultSet.getDate("TransactionDate");
                double amount = resultSet.getDouble("Amount");
                String transactionType = resultSet.getString("TransactionType");

                // Create JSON object
                JSONObject customerTransactionJson = new JSONObject();
                customerTransactionJson.put("CustomerID", customerId);
                customerTransactionJson.put("FirstName", firstName);
                customerTransactionJson.put("LastName", lastName);
                customerTransactionJson.put("TransactionDate", transactionDate.toString());
                customerTransactionJson.put("Amount", amount);
                customerTransactionJson.put("TransactionType", transactionType);

                jsonObjectList.add(customerTransactionJson);
            }
//            writeToJSONFiles(jsonObjectList);
            return jsonObjectList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            // Close connections and statements
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
//    public void writeToJSONFiles(List<JSONObject> customerTransactions) {
//        Map<Integer, FileWriter> fileWriterMap = new HashMap<>();
//
//        // Separate transactions by CustomerID
//        for (JSONObject transaction : customerTransactions) {
//            int customerId = (int) transaction.get("CustomerID");
//            FileWriter fileWriter = fileWriterMap.get(customerId);
//            if (fileWriter == null) {
//                try {
//                    // Create a new file for each CustomerID
//                    String fileName = "Customer_" + customerId + ".json";
//                    fileWriter = new FileWriter(fileName);
//                    fileWriterMap.put(customerId, fileWriter);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            // Write transaction to respective file
//            try {
//                fileWriter.write(transaction.toString() + "\n");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // Close all file writers and print file content
//        for (Map.Entry<Integer, FileWriter> entry : fileWriterMap.entrySet()) {
//            int customerId = entry.getKey();
//            FileWriter fileWriter = entry.getValue();
//            try {
//                fileWriter.close();
//                System.out.println("Contents of Customer_" + customerId + ".json:");
//                System.out.println("-----------------------------");
//                // Read and print the content of the file
//                // (You can also modify this to read the file content and store it in a variable)
//                // Example:
//                 BufferedReader reader = new BufferedReader(new FileReader("Customer_" + customerId + ".json"));
//                 String line;
//                 while ((line = reader.readLine()) != null) {
//                    System.out.println(line);
//
//                 }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    public void writeToJSONFiles(List<JSONObject> customerTransactions) {
//        // A Map to store FileWriter instances for each customer ID
//        // The key is the customer ID, and the value is the FileWriter instance
//        Map<Integer, BufferedWriter> fileWriterMap = new HashMap<>();
//
//        // Separate transactions by CustomerID
//        for (JSONObject transaction : customerTransactions) {
//            int customerId = (int) transaction.get("CustomerID");
//            BufferedWriter fileWriter = fileWriterMap.get(customerId);
//
//            // Create a new file writer if it does not exist for the CustomerID
//            if (fileWriter == null) {
//                try {
//                    String fileName = "Customer_" + customerId + ".json";
//                    fileWriter = new BufferedWriter(new FileWriter(fileName));
//                    fileWriterMap.put(customerId, fileWriter);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            // Write transaction to the respective file
//            try {
//                fileWriter.write(transaction.toString() + "\n");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // Close all file writers and print file content
//        for (Map.Entry<Integer, BufferedWriter> entry : fileWriterMap.entrySet()) {
//            int customerId = entry.getKey();
//            BufferedWriter fileWriter = entry.getValue();
//            try {
//                fileWriter.close();
//                System.out.println("Contents of Customer_" + customerId + ".json:");
//                System.out.println("-----------------------------");
//
//                // Read and print the content of the file
//                // (You can also modify this to read the file content and store it in a variable)
//                // Example:
//                BufferedReader reader = new BufferedReader(new FileReader("Customer_" + customerId + ".json"));
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    System.out.println(line);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    public Map<Integer, String> groupTransactionsByCustomer(List<JSONObject> customerTransactions) {
//        Map<Integer, String> transactionMap = new HashMap<>();
//
//        // Separate transactions by CustomerID
//        for (JSONObject transaction : customerTransactions) {
//            int customerId = (int) transaction.get("CustomerID");
//            String transactionString = transaction.toString();
//
//            if (!transactionMap.containsKey(customerId)) {
//                transactionMap.put(customerId, transactionString+",");
//            } else {
//                transactionMap.put(customerId, transactionMap.get(customerId) + transactionString+",");
//            }
//        }
//        System.out.println(transactionMap);
//        return transactionMap;
//    }
//    public Map<Integer, String> groupTransactionsByCustomer(List<JSONObject> customerTransactions) {
//        Map<Integer, String> transactionMap = new HashMap<>();
//
//        // Separate transactions by CustomerID
//        for (JSONObject transaction : customerTransactions) {
//            int customerId = (int) transaction.get("CustomerID");
//
//            if (!transactionMap.containsKey(customerId)) {
//                List<JSONObject> transactionsList = new ArrayList<>();
//                transactionsList.add(transaction);
//                transactionMap.put(customerId, transactionsList.toString());
//            } else {
////                List<JSONObject> existingCustomerTransactions = transactionMap.get(customerId);
////                existingCustomerTransactions.add(transaction);
//                transactionMap.put(customerId, transactionMap.get(customerId)+transaction);
//            }
//        }
//
//        return transactionMap;
//    }
public List<List<JSONObject>> groupTransactionsByCustomer(List<JSONObject> customerTransactions) {
    List<List<JSONObject>> responseObjectLi=new ArrayList<>();
    List<JSONObject> transactionObject= customerTransactions;
    List<JSONObject> responseObject= new ArrayList<>();
    // Separate transactions by CustomerID
    for (JSONObject transaction : transactionObject) {
        responseObject.add(transaction);
//        System.out.println(transaction);
    }
    responseObjectLi.add(responseObject);
    return responseObjectLi;
}

}
