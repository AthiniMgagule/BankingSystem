package BankingSystem;

import java.io.Serializable;

public class BankAccount implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private int accountNo;
    private double balance;
    private String accountHolder;
    private String customerID;
    
    public BankAccount(int accountNo, String accountHolder, String customerID) {
        this.accountNo = accountNo;
        this.balance = 0.0; // New accounts start with 0 balance
        this.accountHolder = accountHolder;
        this.customerID = customerID;
    }
    
    public boolean depositMoney(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive.");
            return false;
        }
        balance += amount;
        System.out.println("Successfully deposited $" + amount);
        return true;
    }
    
    public boolean withdrawMoney(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return false;
        }
        if (amount > balance) {
            System.out.println("Insufficient funds. Current balance: $" + balance);
            return false;
        }
        balance -= amount;
        System.out.println("Successfully withdrew $" + amount);
        return true;
    }
    
    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNo);
        System.out.println("Account Holder: " + accountHolder);
        System.out.println("Current Balance: $" + balance);
        System.out.println("Customer ID: " + customerID);
    }
    
    // Getters and Setters
    public int getAccountNo() { return accountNo; }
    public double getBalance() { return balance; }
    public String getAccountHolder() { return accountHolder; }
    public String getCustomerID() { return customerID; }
    
    public void setAccountHolder(String accountHolder) { this.accountHolder = accountHolder; }
}

