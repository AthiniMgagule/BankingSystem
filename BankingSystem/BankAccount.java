package BankingSystem;

import java.io.Serializable;
import java.time.LocalDate;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import BankingSystem.Main.TransferResult;

public class BankAccount implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String accountType;
	private int accountNumber;
	private double balance;
	private String accountHolder;
	private LocalDate dateOpened;
	private boolean isActive;
	private String pin;
	
	private List<String> transactionHistory = new ArrayList<>();
	
	//====== full constructor
	public BankAccount(String accountType, int accountNumber, String accountHolder, LocalDate dateOpened,  boolean isActive, String pin) {
		this.accountType = accountType;
		this.accountNumber = accountNumber;
		this.accountHolder = accountHolder;
		this.balance = 0.0;
		this.dateOpened = dateOpened;
		this.isActive = isActive;
		this.pin = hashPin(pin);
	}
	
	//======= getters
	public String getAccountType() {
		return accountType;
	}


	public int getAccountNumber() {
		return accountNumber;
	}


	public double getBalance() {
		return balance;
	}


	public String getAccountHolder() {
		return accountHolder;
	}

	public LocalDate getDateOpened() {
		return dateOpened;
	}


	public boolean isActive() {
		return isActive;
	}


	public String getPin() {
		return pin;
	}


	//======= setters
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}


	public boolean validatePin(String pin) {
		return hashPin(pin).equals(this.pin);
	}
	
	private String hashPin(String pin) {
	    try {
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] encodedHash = digest.digest(pin.getBytes());
	        StringBuilder hexString = new StringBuilder();

	        for (byte b : encodedHash) {
	            String hex = Integer.toHexString(0xff & b);
	            if (hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }

	        return hexString.toString();
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("SHA-256 algorithm not available", e);
	    }
	}

	
	public boolean depositMoney(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive.");
            return false;
        }
        balance += amount;
        transactionHistory.add("Deposited R" + amount + " on " + LocalDate.now());
        System.out.println("Successfully deposited R" + amount);
        return true;
    }
	
	public boolean withdrawMoney(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return false;
        }
        if (amount > balance) {
            System.out.println("Insufficient funds. Current balance: R" + balance);
            return false;
        }
        balance -= amount;
        transactionHistory.add("Withdrew R" + amount + " on " + LocalDate.now());
        System.out.println("Successfully withdrew R" + amount);
        return true;
    }
	/*
	public void transferMoney(BankAccount targetAccount, double amount) {
		if(amount > balance) {
			System.out.println("Insufficient funds. Current balance: R" + balance);
			return;
		}
		
		balance -= amount;
		targetAccount.balance += amount;
		transactionHistory.add("Transferred R" + amount + " to Account " + targetAccount.getAccountNumber() + " on " + LocalDate.now());
		targetAccount.transactionHistory.add("Received R" + amount + " from Account " + this.accountNumber + " on " + LocalDate.now());
		System.out.println("Transferred R" + amount + " to account " + targetAccount);
	}
	*/
	
	public TransferResult transferMoneyTo(BankAccount targetAccount, double amount, String description) {
	    // Input validation
	    if (amount <= 0) {
	        return new TransferResult(false, "Transfer amount must be positive.");
	    }
	    
	    if (amount > this.balance) {
	        return new TransferResult(false, "Insufficient funds. Current balance: R" + String.format("%.2f", this.balance));
	    }
	    
	    if (targetAccount == null) {
	        return new TransferResult(false, "Target account is invalid.");
	    }
	    
	    if (this.accountNumber == targetAccount.getAccountNumber()) {
	        return new TransferResult(false, "Cannot transfer money to the same account.");
	    }
	    
	    if (!targetAccount.isActive()) {
	        return new TransferResult(false, "Target account is inactive.");
	    }
	    
	    try {
	        // Perform the transfer
	        this.balance -= amount;
	        targetAccount.balance += amount;
	        
	        // Create transaction records
	        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	        String descriptionText = (description != null && !description.trim().isEmpty()) ? " - " + description.trim() : "";
	        
	        // Add to transaction histories
	        this.transactionHistory.add("Transferred R" + String.format("%.2f", amount) + 
	                                   " to Account " + targetAccount.getAccountNumber() + descriptionText + " on " + timestamp);
	        
	        targetAccount.transactionHistory.add("Received R" + String.format("%.2f", amount) + 
	                                           " from Account " + this.accountNumber + descriptionText + " on " + timestamp);
	        
	        System.out.println("Successfully transferred R" + String.format("%.2f", amount) + 
	                          " to Account " + targetAccount.getAccountNumber());
	        
	        return new TransferResult(true, "Transfer completed successfully.");
	        
	    } catch (Exception e) {
	        return new TransferResult(false, "Transfer failed: " + e.getMessage());
	    }
	}
	
	public void displayTransactionHistory() {
	    if (transactionHistory.isEmpty()) {
	        System.out.println("No transactions found.");
	        return;
	    }

	    System.out.println("Transaction History for Account " + accountNumber + ":");
	    for (String record : transactionHistory) {
	        System.out.println("- " + record);
	    }
	}
	
	public boolean changePin(String oldPin, String newPin) {
	    if (validatePin(oldPin)) {
	        this.pin = hashPin(newPin);
	        transactionHistory.add("PIN changed on " + LocalDate.now());
	        return true;
	    }
	    return false;
	}
    
    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber +
        		"\nAccount Holder: " + accountHolder + 
        		"\nAccount Type: " + accountType + 
        		"\nDate Opened: " + dateOpened);  
    }
    
    public static class TransferResult {
        private final boolean success;
        private final String message;
        
        public TransferResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
    }
    
    @Override
	public String toString() {
	    return "Account #" + accountNumber + " (" + accountType + ")";
	}
}


