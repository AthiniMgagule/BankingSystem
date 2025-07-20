package BankingSystem.data;

import BankingSystem.BankAccount;
import BankingSystem.Customer;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class DataManager {
	private static final String CUSTOMER_FILE = "customer.ser";
	private static final String ACCOUNTS_FILE = "accounts.ser";
	
	//======== save customers to file
	public static void saveCustomers(ArrayList<Customer> customers) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CUSTOMER_FILE))) {
            oos.writeObject(customers);
            System.out.println("Customer data saved successfully. Saved " + customers.size() + " customers.");
        } catch (IOException e) {
            System.out.println("Error saving customer data: " + e.getMessage());
            e.printStackTrace();
        }
    }
	
	//======== save customers to file
	public static void saveAccounts(HashMap<String, BankAccount> accounts) {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ACCOUNTS_FILE))){
			oos.writeObject(accounts);
			System.out.println("Account data saved successfully. Saved " + accounts.size() + " accounts.");
		}catch(IOException e) {
			System.out.println("Error saving accounts data: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	//======== load customers from file
	@SuppressWarnings("unchecked")
    public static ArrayList<Customer> loadCustomers() {
        File file = new File(CUSTOMER_FILE);
        if (!file.exists()) {
            System.out.println("No existing customer data found. Starting fresh.");
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CUSTOMER_FILE))) {
            ArrayList<Customer> customers = (ArrayList<Customer>) ois.readObject();
            System.out.println("Customer data loaded successfully. Found " + customers.size() + " customers.");
            
            // DEBUG: Print each customer
            for (Customer c : customers) {
                System.out.println("  - " + c.getFullName() + " (ID: " + c.getCustomerID() + ")");
            }
            
            return customers;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading customer data: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
	
	//======== load accounts from file
	@SuppressWarnings("unchecked")
	public static HashMap<String, BankAccount> loadAccounts(){
		File file = new File(ACCOUNTS_FILE);
		if(!file.exists()) {
			System.out.println("No existing account data found. Starting fresh.");
			return new HashMap<>();
		}
		
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ACCOUNTS_FILE))){
			HashMap<String, BankAccount> accounts = (HashMap<String, BankAccount>) ois.readObject();
			System.out.println("Account data loaded successfully. Found " + accounts.size() + " accounts.");
            
            // DEBUG: Print each account
            for (String id : accounts.keySet()) {
                BankAccount acc = accounts.get(id);
                System.out.println("  - Account #" + acc.getAccountNumber() + " (Customer: " + acc.getAccountHolder() + ", Balance: R" + acc.getBalance() + ")");
            }
            
            return accounts;
		}catch(IOException | ClassNotFoundException e) {
			System.out.println("Error loading account data: " + e.getMessage());
            e.printStackTrace();
            return new HashMap<>();
		}
	}
	
	//======== Save all data
    public static void saveAllData(ArrayList<Customer> customers, HashMap<String, BankAccount> accounts) {
        System.out.println("=== SAVING ALL DATA ===");
        System.out.println("Customers to save: " + customers.size());
        System.out.println("Accounts to save: " + accounts.size());
        
        saveCustomers(customers);
        saveAccounts(accounts);
        
        System.out.println("All data saved successfully!");
    }
	
	//======== load method
    public static void loadAllData(ArrayList<Customer> customers, HashMap<String, BankAccount> accounts) {
        System.out.println("=== LOADING ALL DATA ===");
        
        //======= Load fresh data
        ArrayList<Customer> loadedCustomers = loadCustomers();
        HashMap<String, BankAccount> loadedAccounts = loadAccounts();
        
        //======= Clear and repopulate
        customers.clear();
        accounts.clear();
        customers.addAll(loadedCustomers);
        accounts.putAll(loadedAccounts);
        
        System.out.println("Final counts after loading:");
        System.out.println("  Customers: " + customers.size());
        System.out.println("  Accounts: " + accounts.size());
    }
    
    //======== Check if data files exist
    public static boolean hasExistingData() {
        File customersFile = new File(CUSTOMER_FILE);
        File accountsFile = new File(ACCOUNTS_FILE);
        boolean hasData = customersFile.exists() || accountsFile.exists();
        
        System.out.println("Checking for existing data:");
        System.out.println("  customers.ser exists: " + customersFile.exists());
        System.out.println("  accounts.ser exists: " + accountsFile.exists());
        System.out.println("  Has existing data: " + hasData);
        
        return hasData;
    }
    
    //======== Delete all data files (for testing purposes)
    public static void clearAllData() {
        File customersFile = new File(CUSTOMER_FILE);
        File accountsFile = new File(ACCOUNTS_FILE);
        
        if (customersFile.exists()) {
            customersFile.delete();
            System.out.println("Customer data file deleted.");
        }
        
        if (accountsFile.exists()) {
            accountsFile.delete();
            System.out.println("Account data file deleted.");
        }
        
        System.out.println("All data cleared!");
    }
}
