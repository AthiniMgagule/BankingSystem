package BankingSystem;

import BankingSystem.data.DataManager;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.ComboBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;


public class Main extends Application {
	private ArrayList<Customer> customers = new ArrayList<>();
	private HashMap<String, BankAccount> accounts = new HashMap<>();
	private Stage primaryStage;
	private BankAccount currentAccount = null;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("BankForge");
		
		//======== load existing data on startup
		DataManager.loadAllData(customers, accounts);
		showLandingPage();
		
		//======== save data when application is closed
		primaryStage.setOnCloseRequest(e -> {
			System.out.println("=== SAVING DATA ON EXIT ===");
			DataManager.saveAllData(customers, accounts);
			System.out.println("Application closed. Data saved successfully.");
		});
		
		primaryStage.show();
	}
	
	//======== landing page
	private void showLandingPage() {
        VBox landingLayout = new VBox(30);
        landingLayout.setAlignment(Pos.CENTER);
        landingLayout.setPadding(new Insets(50));
        landingLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");
        
        // Bank Logo/Title
        Label bankTitle = new Label("BankForge");
        bankTitle.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        bankTitle.setStyle("-fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 2);");
        
        Label welcomeLabel = new Label("Welcome to Your Trusted Banking Partner");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        welcomeLabel.setStyle("-fx-text-fill: #f0f0f0;");
        
        // Main action buttons
        VBox buttonContainer = new VBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setMaxWidth(350);
        
        Button loginBtn = createStyledButton("Login to Account", "#4CAF50", 320, 50);
        Button registerBtn = createStyledButton("Create New Account", "#2196F3", 320, 50);
        Button recoverBtn = createStyledButton("Recover Account Details", "#FF9800", 320, 50);
        
        // Button actions
        loginBtn.setOnAction(e -> showLoginForm());
        registerBtn.setOnAction(e -> showRegisterForm());
        recoverBtn.setOnAction(e -> showRecoverForm());
        
        buttonContainer.getChildren().addAll(loginBtn, registerBtn, recoverBtn);
        
        // Footer info
        Label footerLabel = new Label("Secure • Reliable • 24/7 Access");
        footerLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        footerLabel.setStyle("-fx-text-fill: #d0d0d0;");
        
        landingLayout.getChildren().addAll(bankTitle, welcomeLabel, buttonContainer, footerLabel);
        
        Scene scene = new Scene(landingLayout, 700, 600);
        primaryStage.setScene(scene);
    }
	
	//======== login page
	private void showLoginForm() {
        Stage loginStage = new Stage();
        loginStage.setTitle("Login to Your Account");
        loginStage.initOwner(primaryStage);
        
        VBox loginLayout = new VBox(20);
        loginLayout.setPadding(new Insets(40));
        loginLayout.setStyle("-fx-background-color: #f8f9fa;");
        loginLayout.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Account Login");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #333;");
        
        Label subtitleLabel = new Label("Enter your account details to access your account");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        subtitleLabel.setStyle("-fx-text-fill: #666;");
        
        // Login form
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        formBox.setMaxWidth(350);
        
        TextField accountField = createTextField("Account Number");
        PasswordField pinField = new PasswordField();
        pinField.setPromptText("4-Digit PIN");
        pinField.setPrefHeight(40);
        pinField.setStyle(getTextFieldStyle());
        
        Button loginSubmitBtn = createStyledButton("Login", "#4CAF50", 300, 45);
        Button backBtn = createStyledButton("Back", "#9E9E9E", 300, 45);
        
        Label messageLabel = new Label();
        messageLabel.setWrapText(true);
        messageLabel.setAlignment(Pos.CENTER);
        
        formBox.getChildren().addAll(accountField, pinField, loginSubmitBtn, backBtn, messageLabel);
        
        loginSubmitBtn.setOnAction(e -> {
            String accountStr = accountField.getText().trim();
            String pin = pinField.getText().trim();
            
            if (accountStr.isEmpty() || pin.isEmpty()) {
                showError(messageLabel, "Please fill in all fields.");
                return;
            }
            
            try {
                int accountNumber = Integer.parseInt(accountStr);
                if (authenticateUser(accountNumber, pin)) {
                    loginStage.close();
                    showDashboard();
                } else {
                    showError(messageLabel, "Invalid account number or PIN. Please try again.");
                    pinField.clear();
                }
            } catch (NumberFormatException ex) {
                showError(messageLabel, "Please enter a valid account number.");
            }
        });
        
        backBtn.setOnAction(e -> loginStage.close());
        
        loginLayout.getChildren().addAll(titleLabel, subtitleLabel, formBox);
        
        Scene scene = new Scene(loginLayout, 500, 450);
        loginStage.setScene(scene);
        loginStage.show();
    }
	
	//======== registration page
	private void showRegisterForm() {
	    Stage registerStage = new Stage();
	    registerStage.setTitle("Create New Account");
	    registerStage.initOwner(primaryStage);
	    
	    VBox mainContainer = new VBox(20);
	    mainContainer.setPadding(new Insets(40));
	    mainContainer.setStyle("-fx-background-color: #f8f9fa;");
	    mainContainer.setAlignment(Pos.CENTER);
	    
	    Label titleLabel = new Label("Create New Account");
	    titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	    titleLabel.setStyle("-fx-text-fill: #333;");
	    
	    Label subtitleLabel = new Label("Fill in your details to create a new bank account");
	    subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
	    subtitleLabel.setStyle("-fx-text-fill: #666;");
	    
	    VBox formBox = new VBox(15);
	    formBox.setAlignment(Pos.CENTER);
	    formBox.setMaxWidth(400);
	    formBox.setPadding(new Insets(20));
	    
	    //======== basic info
	    Label basicInfo = new Label("Basic Information");
	    basicInfo.setFont(Font.font("Arial", FontWeight.BOLD, 16));
	    basicInfo.setStyle("-fx-text-fill: #2196F3; -fx-padding: 10 0 5 0;");
	    
	    TextField fnameField = createTextField("First Name *");
	    TextField lnameField = createTextField("Last Name *");
	    
	    // Gender dropdown
	    ComboBox<String> genderComboBox = createComboBox("Select Gender *");
	    genderComboBox.getItems().addAll("Male", "Female", "Other", "Prefer not to say");
	    
	    TextField dateOfBirthField = createTextField("Date Of Birth (yyyy-MM-dd) *");
	    TextField idField = createTextField("ID Number *");
	    TextField phoneField = createTextField("Phone Number *");
	    TextField emailField = createTextField("Email Address (Optional)");
	    TextField nationalityField = createTextField("Nationality *");
	    
	    //======== address info
	    Label addressInfo = new Label("Address Information");
	    addressInfo.setFont(Font.font("Arial", FontWeight.BOLD, 16));
	    addressInfo.setStyle("-fx-text-fill: #2196F3; -fx-padding: 10 0 5 0;");
	    
	    TextField streetAddressField = createTextField("Street Address *");
	    TextField cityField = createTextField("City *");
	    TextField postalCodeField = createTextField("Postal Code *");
	    TextField countryField = createTextField("Country *");
	    
	    //======== occupation info
	    Label occupationInfo = new Label("Occupation Information");
	    occupationInfo.setFont(Font.font("Arial", FontWeight.BOLD, 16));
	    occupationInfo.setStyle("-fx-text-fill: #2196F3; -fx-padding: 10 0 5 0;");
	    
	    TextField occupationField = createTextField("Occupation *");
	    TextField employerNameField = createTextField("Employer name *");
	    TextField monthlyIncomeField = createTextField("Monthly Income *");
	    
	    //========= account information
	    Label accountInfo = new Label("Account Information");
	    accountInfo.setFont(Font.font("Arial", FontWeight.BOLD, 16));
	    accountInfo.setStyle("-fx-text-fill: #2196F3; -fx-padding: 10 0 5 0;");
	    
	    // Account type dropdown
	    ComboBox<String> accountTypeComboBox = createComboBox("Select Account Type *");
	    accountTypeComboBox.getItems().addAll("Savings", "Current", "Fixed Deposit", "Student Account", "Business Account");
	    
	    PasswordField pinField = new PasswordField();
	    pinField.setPromptText("Create 4-Digit PIN *");
	    pinField.setPrefHeight(40);
	    pinField.setStyle(getTextFieldStyle());
	    
	    PasswordField confirmPinField = new PasswordField();
	    confirmPinField.setPromptText("Confirm PIN *");
	    confirmPinField.setPrefHeight(40);
	    confirmPinField.setStyle(getTextFieldStyle());
	    
	    // Buttons
	    Button createAccountBtn = createStyledButton("Create Account", "#4CAF50", 350, 45);
	    Button backBtn = createStyledButton("Back", "#9E9E9E", 350, 45);
	    
	    Label messageLabel = new Label();
	    messageLabel.setWrapText(true);
	    messageLabel.setAlignment(Pos.CENTER);
	    messageLabel.setMaxWidth(350);
	    
	    // Add all form elements to the form container
	    formBox.getChildren().addAll(
	        basicInfo, fnameField, lnameField, genderComboBox, dateOfBirthField, idField, phoneField, emailField,  
	        nationalityField, 
	        addressInfo, streetAddressField, cityField, postalCodeField, countryField, 
	        occupationInfo, occupationField, employerNameField, monthlyIncomeField, 
	        accountInfo, accountTypeComboBox, pinField, confirmPinField, 
	        createAccountBtn, backBtn, messageLabel
	    );
	    
	    // Create ScrollPane for the form
	    ScrollPane scrollPane = new ScrollPane(formBox);
	    scrollPane.setFitToWidth(true);
	    scrollPane.setFitToHeight(false);
	    scrollPane.setPrefHeight(400); // Set preferred height for scroll area
	    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // No horizontal scrollbar
	    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scrollbar as needed
	    scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
	    
	    // Make scrolling smoother
	    scrollPane.setPannable(true);
	    scrollPane.setVvalue(0.0); // Start at top
	    
	    // Button event handlers
	    createAccountBtn.setOnAction(e -> {
	        String fname = fnameField.getText().trim();
	        String lname = lnameField.getText().trim();
	        String gender = genderComboBox.getValue(); // Get value from ComboBox
	        String dof = dateOfBirthField.getText().trim();
	        String idNumber = idField.getText().trim();
	        String phone = phoneField.getText().trim();
	        String email = emailField.getText().trim();
	        String nationality = nationalityField.getText().trim();
	        String streetAddress = streetAddressField.getText().trim();
	        String city = cityField.getText().trim();
	        String postalCode = postalCodeField.getText().trim();
	        String country = countryField.getText().trim();
	        String occupation = occupationField.getText().trim();
	        String employerName = employerNameField.getText().trim();
	        String monthlyIncome = monthlyIncomeField.getText().trim();
	        String pin = pinField.getText().trim();
	        String confirmPin = confirmPinField.getText().trim();
	        String accType = accountTypeComboBox.getValue(); // Get value from ComboBox
	        
	        if (validateRegistration(fname, lname, gender, dof, idNumber, phone, email, nationality, streetAddress, city, postalCode, country, occupation, employerName, monthlyIncome, messageLabel, pin, confirmPin)) {
	            LocalDate dateOfBirth = LocalDate.parse(dof);
	            int code = Integer.parseInt(postalCode);
	            double income = Double.parseDouble(monthlyIncome);
	            
	            // Create customer (email is optional)
	            Customer customer = new Customer(fname, lname, gender, phone, idNumber, email.isEmpty() ? null : email, dateOfBirth, nationality, streetAddress, city, code, country, occupation, employerName, income);
	            customers.add(customer);
	            
	            // Create bank account
	            int accountNo = generateAccountNumber();
	            BankAccount account = new BankAccount(accType, accountNo, customer.getFullName(), LocalDate.now(), true, pin);
	            accounts.put(customer.getCustomerID(), account);
	            
	            showSuccessDialog("Account Created Successfully!", 
	                "Welcome to BankForge!" +
	                "\n\nAccount Number: " + accountNo +
	                "\nAccount Type: " + accType + 
	                "\nCustomer ID: " + customer.getCustomerID() +
	                "\n\nPlease save these details for future reference. " +
	                "\nYou can now login using your Account Number and PIN.");
	            
	            registerStage.close();
	        } else {
	            // Scroll to the message label to show error
	            scrollPane.setVvalue(1.0);
	        }
	    });
	    
	    backBtn.setOnAction(e -> registerStage.close());
	    
	    // Add header and scroll pane to main container
	    mainContainer.getChildren().addAll(titleLabel, subtitleLabel, scrollPane);
	    
	    Scene scene = new Scene(mainContainer, 600, 700); // Increased height
	    registerStage.setScene(scene);
	    registerStage.show();
	}

	private ComboBox<String> createComboBox(String promptText) {
	    ComboBox<String> comboBox = new ComboBox<>();
	    comboBox.setPromptText(promptText);
	    comboBox.setPrefHeight(40);
	    comboBox.setMaxWidth(Double.MAX_VALUE);
	    comboBox.setStyle(getTextFieldStyle());
	    return comboBox;
	}

	private boolean validateRegistration(String fName, String lName, String gender, String dof, String idNumber, String phone, String email, String nationality, String streetAddress, String city, String postalCode, String country, String occupation, String employerName, String monthlyIncome, Label messageLabel, String pin, String confirmPin) {
	    if(fName.isEmpty() || lName.isEmpty() || gender == null || gender.isEmpty() || dof.isEmpty() || idNumber.isEmpty() || phone.isEmpty() || nationality.isEmpty() || streetAddress.isEmpty() || city.isEmpty() || postalCode.isEmpty() || country.isEmpty() || occupation.isEmpty() || employerName.isEmpty() || monthlyIncome.isEmpty() || pin.isEmpty() || confirmPin.isEmpty()) {
	        showError(messageLabel, "Please fill in all required fields (marked with *).");
	        return false;
	    }
	    
	    //======== date of birth validation
	    if(!dof.matches("^((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$")) {
	        showError(messageLabel, "Date of birth must be of format yyyy-MM-dd.");
	        return false;
	    }
	    
	    //======== id number validation
	    if(!idNumber.matches("\\d{13}")) {
	        showError(messageLabel, "ID must be exactly 13 digits.");
	        return false;
	    }
	    
	    //======== phone number validation
	    if(!phone.matches("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$")) {
	        showError(messageLabel, "Phone number must be exactly 10 digits.");
	        return false;
	    }
	    
	    //======== email validation (only if not empty)
	    if(!email.isEmpty() && !email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
	        showError(messageLabel, "Please enter a valid email address.");
	        return false;
	    }
	    
	    if (pin.length() != 4 || !pin.matches("\\d{4}")) {
	        showError(messageLabel, "PIN must be exactly 4 digits.");
	        return false;
	    }
	    
	    if (!pin.equals(confirmPin)) {
	        showError(messageLabel, "PINs do not match. Please try again.");
	        return false;
	    }
	    
	    return true;
	}
	
	private int generateAccountNumber() {
        return (int) ThreadLocalRandom.current().nextLong(100000000L, 999999999L);
    }
	
	private void showDashboard() {
        VBox dashboardLayout = new VBox(25);
        dashboardLayout.setAlignment(Pos.CENTER);
        dashboardLayout.setPadding(new Insets(40));
        dashboardLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");
        
        // Welcome header
        Label welcomeLabel = new Label("Welcome, " + currentAccount.getAccountHolder());
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        welcomeLabel.setStyle("-fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 2, 0, 0, 1);");
        
        Label accountLabel = new Label("Account: " + currentAccount.getAccountNumber());
        accountLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        accountLabel.setStyle("-fx-text-fill: #f0f0f0;");
        
        // Balance display
        VBox balanceBox = new VBox(5);
        balanceBox.setAlignment(Pos.CENTER);
        balanceBox.setStyle("-fx-background-color: rgba(255,255,255,0.15); -fx-background-radius: 10; -fx-padding: 20;");
        
        Label balanceTitle = new Label("Current Balance");
        balanceTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        balanceTitle.setStyle("-fx-text-fill: #e0e0e0;");
        
        Label balanceAmount = new Label("R" + String.format("%.2f", currentAccount.getBalance()));
        balanceAmount.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        balanceAmount.setStyle("-fx-text-fill: white;");
        
        balanceBox.getChildren().addAll(balanceTitle, balanceAmount);
        
        // Action buttons
        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setMaxWidth(300);
        
        Button depositBtn = createStyledButton("Deposit Money", "#4CAF50");
        Button withdrawBtn = createStyledButton("Withdraw Money", "#FF9800");
        Button viewDetailsBtn = createStyledButton("View Account Details", "#2196F3");
        Button viewTransferBtn = createStyledButton("Transfer Money", "#2296F3");
        Button viewTransactionHistoryBtn = createStyledButton("View Transaction History", "#F09800");
        Button logoutBtn = createStyledButton("Logout", "#F44336");
        
        depositBtn.setOnAction(e -> showTransactionForm("Deposit Money", true));
        withdrawBtn.setOnAction(e -> showTransactionForm("Withdraw Money", false));
        viewDetailsBtn.setOnAction(e -> showAccountDetailsDialog());
        viewTransferBtn.setOnAction(e -> showTransferMoneyForm());
        viewTransactionHistoryBtn.setOnAction(e -> showTransactionHistoryDialog());
        logoutBtn.setOnAction(e -> {
            currentAccount = null;
            showLandingPage();
        });
        
        buttonBox.getChildren().addAll(depositBtn, withdrawBtn, viewDetailsBtn, viewTransferBtn, viewTransactionHistoryBtn, logoutBtn);
        
        dashboardLayout.getChildren().addAll(welcomeLabel, accountLabel, balanceBox, buttonBox);
        
        Scene scene = new Scene(dashboardLayout, 600, 650);
        primaryStage.setScene(scene);
    }

	//======== transaction history methods
	//======== can export transaction history to a txt file
	private void showTransactionHistoryDialog() {
	    Stage historyStage = new Stage();
	    historyStage.setTitle("Transaction History");
	    historyStage.initOwner(primaryStage);
	    
	    VBox layout = new VBox(20);
	    layout.setPadding(new Insets(30));
	    layout.setStyle("-fx-background-color: #f8f9fa;");
	    layout.setAlignment(Pos.TOP_CENTER);
	    
	    // Title
	    Label titleLabel = new Label("Transaction History");
	    titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	    titleLabel.setStyle("-fx-text-fill: #333;");
	    
	    // Account info
	    Label accountInfoLabel = new Label("Account: " + currentAccount.getAccountNumber() + 
	                                     " | Balance: R" + String.format("%.2f", currentAccount.getBalance()));
	    accountInfoLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
	    accountInfoLabel.setStyle("-fx-text-fill: #666;");
	    
	    // Transaction display area
	    TextArea transactionArea = new TextArea();
	    transactionArea.setEditable(false);
	    transactionArea.setPrefRowCount(15);
	    transactionArea.setPrefColumnCount(50);
	    transactionArea.setWrapText(true);
	    transactionArea.setStyle(
	        "-fx-background-color: white;" +
	        "-fx-border-color: #ddd;" +
	        "-fx-border-radius: 5;" +
	        "-fx-background-radius: 5;" +
	        "-fx-font-family: 'Courier New';" +
	        "-fx-font-size: 12px;" +
	        "-fx-padding: 10;"
	    );
	    
	    // Get transaction history using reflection to access private field
	    String historyContent = getTransactionHistoryContent();
	    transactionArea.setText(historyContent);
	    
	    // Buttons
	    HBox buttonBox = new HBox(15);
	    buttonBox.setAlignment(Pos.CENTER);
	    
	    Button refreshBtn = createStyledButton("Refresh", "#2196F3", 120, 40);
	    Button exportBtn = createStyledButton("Export", "#4CAF50", 120, 40);
	    Button closeBtn = createStyledButton("Close", "#9E9E9E", 120, 40);
	    
	    refreshBtn.setOnAction(e -> {
	        transactionArea.setText(getTransactionHistoryContent());
	        accountInfoLabel.setText("Account: " + currentAccount.getAccountNumber() + 
	                                " | Balance: R" + String.format("%.2f", currentAccount.getBalance()));
	    });
	    
	    exportBtn.setOnAction(e -> exportTransactionHistory());
	    closeBtn.setOnAction(e -> historyStage.close());
	    
	    buttonBox.getChildren().addAll(refreshBtn, exportBtn, closeBtn);
	    
	    // Add scroll pane for transaction area
	    ScrollPane scrollPane = new ScrollPane(transactionArea);
	    scrollPane.setFitToWidth(true);
	    scrollPane.setFitToHeight(true);
	    scrollPane.setPrefHeight(400);
	    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
	    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
	    
	    layout.getChildren().addAll(titleLabel, accountInfoLabel, scrollPane, buttonBox);
	    
	    Scene scene = new Scene(layout, 600, 550);
	    historyStage.setScene(scene);
	    historyStage.show();
	}

	private String getTransactionHistoryContent() {
	    try {
	        // Use reflection to access the private transactionHistory field
	        java.lang.reflect.Field field = BankAccount.class.getDeclaredField("transactionHistory");
	        field.setAccessible(true);
	        
	        @SuppressWarnings("unchecked")
	        List<String> transactionHistory = (List<String>) field.get(currentAccount);
	        
	        if (transactionHistory == null || transactionHistory.isEmpty()) {
	            return "═══════════════════════════════════════════════════════════\n" +
	                   "                    NO TRANSACTIONS FOUND\n" +
	                   "═══════════════════════════════════════════════════════════\n\n" +
	                   "🔍 No transaction history available for this account.\n" +
	                   "💡 Start by making a deposit or withdrawal to see your\n" +
	                   "   transaction history here.\n\n" +
	                   "═══════════════════════════════════════════════════════════";
	        }
	        
	        StringBuilder content = new StringBuilder();
	        content.append("═══════════════════════════════════════════════════════════\n");
	        content.append("                    TRANSACTION HISTORY\n");
	        content.append("═══════════════════════════════════════════════════════════\n\n");
	        content.append("Account Holder: ").append(currentAccount.getAccountHolder()).append("\n");
	        content.append("Account Number: ").append(currentAccount.getAccountNumber()).append("\n");
	        content.append("Account Type: ").append(currentAccount.getAccountType()).append("\n");
	        content.append("Current Balance: R").append(String.format("%.2f", currentAccount.getBalance())).append("\n");
	        content.append("Total Transactions: ").append(transactionHistory.size()).append("\n\n");
	        content.append("───────────────────────────────────────────────────────────\n");
	        content.append("                      TRANSACTIONS\n");
	        content.append("───────────────────────────────────────────────────────────\n\n");
	        
	        // Display transactions in reverse order (newest first)
	        for (int i = transactionHistory.size() - 1; i >= 0; i--) {
	            String transaction = transactionHistory.get(i);
	            content.append("• ").append(transaction).append("\n");
	            
	            // Add separator line between transactions
	            if (i > 0) {
	                content.append("  ─────────────────────────────────────────────────────\n");
	            }
	        }
	        
	        content.append("\n═══════════════════════════════════════════════════════════\n");
	        content.append("                    END OF HISTORY\n");
	        content.append("═══════════════════════════════════════════════════════════");
	        
	        return content.toString();
	        
	    } catch (Exception e) {
	        return "❌ Error retrieving transaction history: " + e.getMessage();
	    }
	}

	private void exportTransactionHistory() {
	    try {
	        // Create a file chooser
	        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
	        fileChooser.setTitle("Export Transaction History");
	        fileChooser.setInitialFileName("transaction_history_" + currentAccount.getAccountNumber() + "_" + 
	                                     java.time.LocalDate.now().toString() + ".txt");
	        
	        // Set extension filter
	        javafx.stage.FileChooser.ExtensionFilter extFilter = 
	            new javafx.stage.FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
	        fileChooser.getExtensionFilters().add(extFilter);
	        
	        // Show save dialog
	        java.io.File file = fileChooser.showSaveDialog(primaryStage);
	        
	        if (file != null) {
	            // Write transaction history to file
	            try (java.io.PrintWriter writer = new java.io.PrintWriter(file)) {
	                writer.println("BANKFORGE - TRANSACTION HISTORY EXPORT");
	                writer.println("Export Date: " + java.time.LocalDateTime.now());
	                writer.println("======================================");
	                writer.println();
	                writer.println(getTransactionHistoryContent());
	                writer.println();
	                writer.println("======================================");
	                writer.println("This document was automatically generated by BankForge Banking System");
	            }
	            
	            showSuccessDialog("Export Successful", 
	                "Transaction history has been exported to:\n" + file.getAbsolutePath());
	        }
	        
	    } catch (Exception e) {
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Export Error");
	        alert.setHeaderText("Failed to export transaction history");
	        alert.setContentText("Error: " + e.getMessage());
	        alert.initOwner(primaryStage);
	        alert.showAndWait();
	    }
	}
	
	//======== tranfer money methods
	private void showTransferMoneyForm() {
	    Stage transferStage = new Stage();
	    transferStage.setTitle("Transfer Money");
	    transferStage.initOwner(primaryStage);
	    
	    VBox layout = new VBox(20);
	    layout.setPadding(new Insets(30));
	    layout.setStyle("-fx-background-color: #f8f9fa;");
	    layout.setAlignment(Pos.CENTER);
	    
	    // Title
	    Label titleLabel = new Label("Transfer Money");
	    titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	    titleLabel.setStyle("-fx-text-fill: #333;");
	    
	    // Current account info
	    Label currentAccountLabel = new Label("From: Account " + currentAccount.getAccountNumber() + 
	                                        " | Balance: R" + String.format("%.2f", currentAccount.getBalance()));
	    currentAccountLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
	    currentAccountLabel.setStyle("-fx-text-fill: #666; -fx-padding: 0 0 10 0;");
	    
	    // Form fields
	    VBox formBox = new VBox(15);
	    formBox.setAlignment(Pos.CENTER);
	    formBox.setMaxWidth(400);
	    
	    TextField targetAccountField = createTextField("Target Account Number");
	    TextField amountField = createTextField("Transfer Amount");
	    TextField descriptionField = createTextField("Description (Optional)");
	    
	    // Buttons
	    Button transferBtn = createStyledButton("Transfer Money", "#FF9800", 300, 45);
	    Button cancelBtn = createStyledButton("Cancel", "#9E9E9E", 300, 45);
	    
	    Label messageLabel = new Label();
	    messageLabel.setWrapText(true);
	    messageLabel.setAlignment(Pos.CENTER);
	    messageLabel.setMaxWidth(380);
	    
	    formBox.getChildren().addAll(targetAccountField, amountField, descriptionField, transferBtn, cancelBtn, messageLabel);
	    
	    // Transfer button action
	    transferBtn.setOnAction(e -> {
	        String targetAccountStr = targetAccountField.getText().trim();
	        String amountStr = amountField.getText().trim();
	        String description = descriptionField.getText().trim();
	        
	        if (targetAccountStr.isEmpty() || amountStr.isEmpty()) {
	            showError(messageLabel, "Please fill in target account number and amount.");
	            return;
	        }
	        
	        try {
	            int targetAccountNumber = Integer.parseInt(targetAccountStr);
	            double amount = Double.parseDouble(amountStr);
	            
	            // Perform transfer
	            TransferResult result = transferMoney(currentAccount, targetAccountNumber, amount, description);
	            
	            if (result.isSuccess()) {
	                showSuccessDialog("Transfer Successful!", result.getMessage());
	                transferStage.close();
	                showDashboard(); // Refresh dashboard
	            } else {
	                showError(messageLabel, result.getMessage());
	            }
	            
	        } catch (NumberFormatException ex) {
	            showError(messageLabel, "Please enter valid numbers for account number and amount.");
	        }
	    });
	    
	    cancelBtn.setOnAction(e -> transferStage.close());
	    
	    layout.getChildren().addAll(titleLabel, currentAccountLabel, formBox);
	    
	    Scene scene = new Scene(layout, 500, 450);
	    transferStage.setScene(scene);
	    transferStage.show();
	}

	// Enhanced transfer money method with comprehensive validation
	private TransferResult transferMoney(BankAccount fromAccount, int targetAccountNumber, double amount, String description) {
	    // Input validation
	    if (amount <= 0) {
	        return new TransferResult(false, "Transfer amount must be positive.");
	    }
	    
	    if (amount > fromAccount.getBalance()) {
	        return new TransferResult(false, "Insufficient funds. Current balance: R" + String.format("%.2f", fromAccount.getBalance()));
	    }
	    
	    if (fromAccount.getAccountNumber() == targetAccountNumber) {
	        return new TransferResult(false, "Cannot transfer money to the same account.");
	    }
	    
	    // Find target account
	    BankAccount targetAccount = findAccountByNumber(targetAccountNumber);
	    if (targetAccount == null) {
	        return new TransferResult(false, "Target account not found. Please check the account number.");
	    }
	    
	    if (!targetAccount.isActive()) {
	        return new TransferResult(false, "Target account is inactive. Transfer cannot be completed.");
	    }
	    
	    // Perform the transfer
	    try {
	        // Deduct from source account
	        fromAccount.withdrawMoney(amount);
	        
	        // Add to target account
	        targetAccount.depositMoney(amount);
	        
	        // Create detailed transaction records
	        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	        String descriptionText = description.isEmpty() ? "" : " - " + description;
	        
	        // Add transaction to source account history
	        addTransactionToHistory(fromAccount, "Transferred R" + String.format("%.2f", amount) + 
	                              " to Account " + targetAccountNumber + descriptionText + " on " + timestamp);
	        
	        // Add transaction to target account history
	        addTransactionToHistory(targetAccount, "Received R" + String.format("%.2f", amount) + 
	                              " from Account " + fromAccount.getAccountNumber() + descriptionText + " on " + timestamp);
	        
	        // Save data after successful transfer
	        DataManager.saveAllData(customers, accounts);
	        
	        String successMessage = "Successfully transferred R" + String.format("%.2f", amount) + 
	                               " to Account " + targetAccountNumber + "\n" +
	                               "Your new balance: R" + String.format("%.2f", fromAccount.getBalance());
	        
	        return new TransferResult(true, successMessage);
	        
	    } catch (Exception e) {
	        return new TransferResult(false, "Transfer failed due to an unexpected error: " + e.getMessage());
	    }
	}

	// Helper method to find account by account number
	private BankAccount findAccountByNumber(int accountNumber) {
	    for (BankAccount account : accounts.values()) {
	        if (account.getAccountNumber() == accountNumber) {
	            return account;
	        }
	    }
	    return null;
	}

	// Helper method to add transaction to history using reflection
	private void addTransactionToHistory(BankAccount account, String transaction) {
	    try {
	        java.lang.reflect.Field field = BankAccount.class.getDeclaredField("transactionHistory");
	        field.setAccessible(true);
	        
	        @SuppressWarnings("unchecked")
	        List<String> transactionHistory = (List<String>) field.get(account);
	        
	        if (transactionHistory != null) {
	            transactionHistory.add(transaction);
	        }
	    } catch (Exception e) {
	        System.err.println("Error adding transaction to history: " + e.getMessage());
	    }
	}

	// Result class for transfer operations
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
	
    private void showTransactionForm(String title, boolean isDeposit) {
        Stage transactionStage = new Stage();
        transactionStage.setTitle(title);
        transactionStage.initOwner(primaryStage);
        
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #f8f9fa;");
        layout.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: #333;");
        
        TextField amountField = createTextField("Enter amount");
        Button processBtn = createStyledButton(isDeposit ? "Deposit" : "Withdraw", 
                                             isDeposit ? "#4CAF50" : "#FF9800", 250, 45);
        Button cancelBtn = createStyledButton("Cancel", "#9E9E9E", 250, 45);
        
        Label messageLabel = new Label();
        messageLabel.setWrapText(true);
        messageLabel.setAlignment(Pos.CENTER);
        
        processBtn.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                if (amount <= 0) {
                    showError(messageLabel, "Amount must be positive.");
                    return;
                }
                
                boolean success;
                if (isDeposit) {
                    success = currentAccount.depositMoney(amount);
                } else {
                    success = currentAccount.withdrawMoney(amount);
                }
                
                if (success) {
                    showSuccessDialog("Transaction Successful!", 
                        (isDeposit ? "Deposited: R" : "Withdrawn: R") + String.format("%.2f", amount) +
                        "\nNew Balance: R" + String.format("%.2f", currentAccount.getBalance()));
                    transactionStage.close();
                    showDashboard(); // Refresh dashboard
                } else {
                    showError(messageLabel, "Transaction failed. Please try again.");
                }
            } catch (NumberFormatException ex) {
                showError(messageLabel, "Please enter a valid amount.");
            }
        });
        
        cancelBtn.setOnAction(e -> transactionStage.close());
        
        layout.getChildren().addAll(titleLabel, amountField, processBtn, cancelBtn, messageLabel);
        
        Scene scene = new Scene(layout, 400, 300);
        transactionStage.setScene(scene);
        transactionStage.show();
    }
    
    private void showRecoverForm() {
        Stage recoverStage = new Stage();
        recoverStage.setTitle("Recover Account Details");
        recoverStage.initOwner(primaryStage);
        
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #f8f9fa;");
        
        Label titleLabel = new Label("Recover Account Details");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: #333;");
        
        Label instructionLabel = new Label("Enter your personal information to recover your account details:");
        instructionLabel.setWrapText(true);
        instructionLabel.setStyle("-fx-text-fill: #666;");
        
        TextField nameField = createTextField("Full Name");
        TextField phoneField = createTextField("Phone Number");
        
        Button recoverBtn = createStyledButton("Recover Details", "#FF9800", 300, 45);
        Button backBtn = createStyledButton("Back", "#9E9E9E", 300, 45);
        
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefRowCount(8);
        resultArea.setWrapText(true);
        resultArea.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-font-family: 'Courier New';");
        
        recoverBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            
            if (name.isEmpty() && phone.isEmpty()) {
                resultArea.setText("❌ Please enter at least your name or phone number.");
                return;
            }
            
            java.util.List<Customer> matches = findMatchingCustomers(name, phone);
            
            if (matches.isEmpty()) {
                resultArea.setText("❌ No account found with the provided information.\n\nPlease check your details and try again.");
            } else {
                StringBuilder result = new StringBuilder();
                result.append("✅ Account(s) Found!\n\n");
                result.append("═══════════════════════════════════\n");
                
                for (Customer customer : matches) {
                    BankAccount account = accounts.get(customer.getCustomerID());
                    if (account != null) {
                        result.append("Name: ").append(customer.getFullName()).append("\n");
                        result.append("Account Number: ").append(account.getAccountNumber()).append("\n");
                        result.append("Customer ID: ").append(customer.getCustomerID()).append("\n");
                        result.append("Phone: ").append(customer.getPhoneNumber()).append("\n");
                        if (customer.getEmail() != null && !customer.getEmail().isEmpty()) {
                            result.append("Email: ").append(customer.getEmail()).append("\n");
                        }
                        result.append("─────────────────────────────────\n");
                    }
                }
                
                result.append("\n💡 Use your Account Number and PIN to login.");
                resultArea.setText(result.toString());
            }
        });
        
        backBtn.setOnAction(e -> recoverStage.close());
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(recoverBtn, backBtn);
        
        layout.getChildren().addAll(titleLabel, instructionLabel, nameField, phoneField, buttonBox, resultArea);
        
        Scene scene = new Scene(layout, 500, 550);
        recoverStage.setScene(scene);
        recoverStage.show();
    }
 
    private boolean authenticateUser(int accountNumber, String pin) {
        // Find the account by account number
        for (BankAccount account : accounts.values()) {
            if (account.getAccountNumber() == accountNumber) {
                // Use the validatePin method from BankAccount class
                if (account.validatePin(pin)) {
                    currentAccount = account;
                    return true;
                }
                return false; // Account found but PIN is incorrect
            }
        }
        return false; // Account not found
    }

    private Customer findCustomerById(String customerID) {
        for (Customer customer : customers) {
            if (customer.getCustomerID().equals(customerID)) {
                return customer;
            }
        }
        return null;
    }

    private void showAccountDetailsDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account Details");
        alert.setHeaderText("Account Information");
        
        // Find the customer by iterating through customers and matching account holder name
        Customer customer = null;
        for (Customer c : customers) {
            if (c.getFullName().equals(currentAccount.getAccountHolder())) {
                customer = c;
                break;
            }
        }
        
        StringBuilder details = new StringBuilder();
        
        details.append("Account Number: ").append(currentAccount.getAccountNumber()).append("\n");
        details.append("Account Holder: ").append(currentAccount.getAccountHolder()).append("\n");
        details.append("Current Balance: R").append(String.format("%.2f", currentAccount.getBalance())).append("\n");
        
        if (customer != null) {
            details.append("Phone: ").append(customer.getPhoneNumber()).append("\n");
            if (customer.getEmail() != null && !customer.getEmail().isEmpty()) {
                details.append("Email: ").append(customer.getEmail()).append("\n");
            }
        }
        
        alert.setContentText(details.toString());
        alert.initOwner(primaryStage);
        alert.showAndWait();
    }
    
    private java.util.List<Customer> findMatchingCustomers(String name, String phone) {
        java.util.List<Customer> matches = new java.util.ArrayList<>();
        
        for (Customer customer : customers) {
            boolean nameMatch = name.isEmpty() || customer.getFullName().toLowerCase().contains(name.toLowerCase());
            boolean phoneMatch = phone.isEmpty() || customer.getPhoneNumber().equals(phone);
            
            if (nameMatch && phoneMatch) {
                matches.add(customer);
            }
        }
        
        return matches;
    }
	
	private TextField createTextField(String promptText) {
		TextField textField = new TextField();
		textField.setPromptText(promptText);
		textField.setPrefHeight(40);
		textField.setStyle(getTextFieldStyle());
		return textField;
	}
	
	private String getTextFieldStyle() {
        return "-fx-background-color: white;" +
               "-fx-border-color: #ddd;" +
               "-fx-border-radius: 5;" +
               "-fx-background-radius: 5;" +
               "-fx-padding: 10;" +
               "-fx-font-size: 14px;";
    }
	
	private Button createStyledButton(String text, String color) {
        return createStyledButton(text, color, 280, 45);
    }
    
    private Button createStyledButton(String text, String color, int width, int height) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        button.setPrefHeight(height);
        button.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1);"
        );
        
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: derive(" + color + ", -10%);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);" +
            "-fx-scale-x: 1.02;" +
            "-fx-scale-y: 1.02;"
        ));
        
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 1);" +
            "-fx-scale-x: 1;" +
            "-fx-scale-y: 1;"
        ));
        
        return button;
    }
    
    private void showError(Label label, String message) {
        label.setText(message);
        label.setStyle("-fx-text-fill: #F44336; -fx-font-weight: bold;");
    }
    
    private void showSuccessDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(primaryStage);
        alert.showAndWait();
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
