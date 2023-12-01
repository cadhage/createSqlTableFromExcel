package com.excel.to.sql.convertor.exceltosqlconvertor.Service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

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

}
