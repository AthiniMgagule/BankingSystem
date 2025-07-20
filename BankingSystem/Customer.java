package BankingSystem;

import java.util.concurrent.ThreadLocalRandom;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.io.Serializable;

public class Customer implements Serializable {
	//======= identifier used to serialize/deserialize an object
	private static final long serialVersionUID = 1L;
	
	//======= basic information
	private String firstName;
	private String lastName;
	private String gender;
	private String phoneNumber;
	private String idNumber;
	private String email; //===== optional
	private LocalDate dateOfBirth;
	private String nationality;
	private String customerID;
	
	//======= address
	private String streetAddress;
	private String city;
	private int postalCode;
	private String country;
	
	//======= occupation
	private String occupation; 
	private String employerName; 
	private double monthlyIncome; //===== optional
	
	//======= relationships
	private List<BankAccount> accounts;
	
	//======= full constructor
	public Customer(String firstName, String lastName, String gender, String phoneNumber, String idNumber, String email, LocalDate dateOfBirth, String nationality, String streetAddress, String city, int postalCode, String country, String occupation, String employerName, double monthlyIncome) {
		this.customerID = "C" + ThreadLocalRandom.current().nextLong(100000L, 999999L);
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.phoneNumber = phoneNumber;
		this.idNumber = idNumber;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.nationality = nationality;
		this.streetAddress = streetAddress;
		this.city = city;
		this.postalCode = postalCode;
		this.country = country;
		this.occupation = occupation;
		this.employerName = employerName;
		this.monthlyIncome = monthlyIncome;
		this.accounts = new ArrayList<>();
	}
	
	//======= constructor without email parameter (email is optional) 
	public Customer(String firstName, String lastName, String gender, String phoneNumber, String idNumber, LocalDate dateOfBirth, String nationality, String streetAddress, String city, int postalCode, String country, String occupation, String employerName, double monthlyIncome) {
		this(firstName, lastName, gender, phoneNumber, idNumber, null, dateOfBirth, nationality, streetAddress, city, postalCode, country, occupation, employerName, monthlyIncome);
	}
	
	//======= display customer info
	//======= don't display monthly income (POPI)
	public void displayCustomerInfo() {
		System.out.println("Full Name: " + firstName + " " + lastName +
				"\nGender: " + gender +
				"\nPhone Number: " + phoneNumber +
				"\nID Number: " + idNumber +
				"\nEmail: " + email + 
				"\nDate Of Birth: " + dateOfBirth + 
				"\nNationality: " + nationality +
				"\n\nAddress: " + streetAddress + 
				"\nCity: " + city +
				"\nCountry: " + country + 
				"\nPostal Code: " + postalCode + 
				"\n\nOccupation: " + occupation +
				"\nEmployer Name: " + employerName );
	}
	
	//======= to add the accounts a customer is associated with
	public void addAccount(BankAccount account) {
		accounts.add(account);
	}

	//======= getters
	public String getFullName() { return firstName + " " + lastName; }

	public String getGender() { return gender; }

	public String getPhoneNumber() { return phoneNumber; }

	public String getIdNumber() { return idNumber; }

	public String getEmail() { return email; }

	public LocalDate getDateOfBirth() {return dateOfBirth; }

	public String getNationality() {return nationality;}

	public String getCustomerID() {return customerID;}

	public String getStreetAddress() {return streetAddress;}

	public String getCity() {return city;}

	public int getPostalCode() {return postalCode;}

	public String getCountry() {return country;}

	public String getOccupation() {return occupation;}

	public String getEmployerName() {return employerName;}

	public double getMonthlyIncome() {return monthlyIncome;}

	public List<BankAccount> getAccounts() { return accounts; }

}
