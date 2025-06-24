package BankingSystem;

import java.util.concurrent.ThreadLocalRandom;
import java.io.Serializable;

public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private String name;
    private String phoneNumber;
    private String email;
    private String customerID;
    
    public Customer(String name, String phoneNumber, String email) {
        this.customerID = generateCustomerID();
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    
    public void displayCustomerInfo() {
        System.out.println("Customer ID: " + customerID);
        System.out.println("Name: " + name);
        System.out.println("Phone: " + phoneNumber);
        System.out.println("Email: " + email);
    }
    
    private String generateCustomerID() {
        // Generate a 9-digit customer ID similar to account number format
        // Customer IDs will start with 'C' followed by 8 digits
        long randomNumber = ThreadLocalRandom.current().nextLong(100000L, 999999L);
        return "C" + randomNumber;
    }
    
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCustomerID() { return customerID; }
}



