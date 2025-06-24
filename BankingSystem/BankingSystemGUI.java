package BankingSystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class BankingSystemGUI extends Application {
    
    private ArrayList<Customer> customers = new ArrayList<>();
    private HashMap<String, BankAccount> accounts = new HashMap<>();
    private Stage primaryStage;
    
	@Override
	public void start(Stage primaryStage) {
	    this.primaryStage = primaryStage;
	    primaryStage.setTitle("HexSoft Banking System");
	    
	    // Load existing data on startup
	    DataManager.loadAllData(customers, accounts);
	    
	    showMainMenu();
	    
	    // Save data when application is closed (X button)
	    primaryStage.setOnCloseRequest(e -> {
	        saveDataOnExit();
	    });
	    
	    primaryStage.show();
	}
	
	private void saveDataOnExit() {
	    System.out.println("=== SAVING DATA ON EXIT ===");
	    System.out.println("Customers to save: " + customers.size());
	    System.out.println("Accounts to save: " + accounts.size());
	    
	    DataManager.saveAllData(customers, accounts);
	    System.out.println("Application closed. Data saved successfully!");
	}
	
	private void showMainMenu() {
	    VBox mainLayout = new VBox(20);
	    mainLayout.setAlignment(Pos.CENTER);
	    mainLayout.setPadding(new Insets(40));
	    mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");
	    
	    // Title
	    Label titleLabel = new Label("HexSoft Banking System");
	    titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
	    titleLabel.setStyle("-fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 2, 0, 0, 1);");
	    
	    Label subtitleLabel = new Label("Secure • Reliable • Easy");
	    subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
	    subtitleLabel.setStyle("-fx-text-fill: #f0f0f0;");
	    
	    // Menu buttons
	    VBox buttonBox = new VBox(12);
	    buttonBox.setAlignment(Pos.CENTER);
	    buttonBox.setMaxWidth(300);
	    
	    Button createAccountBtn = createStyledButton("Create New Account", "#4CAF50");
	    Button depositBtn = createStyledButton("Deposit Money", "#2196F3");
	    Button withdrawBtn = createStyledButton("Withdraw Money", "#FF9800");
	    Button balanceBtn = createStyledButton("Check Balance", "#9C27B0");
	    Button detailsBtn = createStyledButton("View Account Details", "#607D8B");
	    Button recoverIdBtn = createStyledButton("Recover Customer ID", "#FF5722");
	    Button exitBtn = createStyledButton("Exit", "#F44336");
	    
	    // Button actions
	    createAccountBtn.setOnAction(e -> showCreateAccountForm());
	    depositBtn.setOnAction(e -> showDepositForm());
	    withdrawBtn.setOnAction(e -> showWithdrawForm());
	    balanceBtn.setOnAction(e -> showBalanceCheck());
	    detailsBtn.setOnAction(e -> showAccountDetails());
	    recoverIdBtn.setOnAction(e -> showRecoverCustomerIdForm());
	    exitBtn.setOnAction(e -> {
	        saveDataOnExit();  // Save data first
	        primaryStage.close();  // Then close
	    });
	    
	    buttonBox.getChildren().addAll(
	        createAccountBtn, depositBtn, withdrawBtn, 
	        balanceBtn, detailsBtn,recoverIdBtn, exitBtn
	    );
	    
	    mainLayout.getChildren().addAll(titleLabel, subtitleLabel, buttonBox);
	    
	    Scene scene = new Scene(mainLayout, 600, 550);
	    primaryStage.setScene(scene);
	}
	
	private void showRecoverCustomerIdForm() {
	    Stage formStage = new Stage();
	    formStage.setTitle("Recover Customer ID");
	    formStage.initOwner(primaryStage);
	    
	    VBox formLayout = new VBox(15);
	    formLayout.setPadding(new Insets(30));
	    formLayout.setStyle("-fx-background-color: #f8f9fa;");
	    
	    Label titleLabel = new Label("Recover Customer ID");
	    titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	    titleLabel.setStyle("-fx-text-fill: #333;");
	    
	    Label instructionLabel = new Label("Enter your personal information to recover your Customer ID:");
	    instructionLabel.setWrapText(true);
	    instructionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
	    instructionLabel.setStyle("-fx-text-fill: #666;");
	    
	    // Form fields
	    TextField nameField = createTextField("Full Name (exactly as registered)");
	    TextField phoneField = createTextField("Phone Number");
	    TextField emailField = createTextField("Email Address");
	    
	    Button recoverBtn = createStyledButton("Recover ID", "#FF5722");
	    Button cancelBtn = createStyledButton("Cancel", "#9E9E9E");
	    
	    // Results area
	    TextArea resultArea = new TextArea();
	    resultArea.setEditable(false);
	    resultArea.setPrefRowCount(6);
	    resultArea.setWrapText(true);
	    resultArea.setStyle(
	        "-fx-background-color: white;" +
	        "-fx-border-color: #ddd;" +
	        "-fx-border-radius: 5;" +
	        "-fx-background-radius: 5;" +
	        "-fx-font-family: 'Courier New';" +
	        "-fx-font-size: 12px;"
	    );
	    
	    HBox buttonBox = new HBox(10);
	    buttonBox.setAlignment(Pos.CENTER);
	    buttonBox.getChildren().addAll(recoverBtn, cancelBtn);
	    
	    recoverBtn.setOnAction(e -> {
	        String name = nameField.getText().trim();
	        String phone = phoneField.getText().trim();
	        String email = emailField.getText().trim();
	        
	        if (name.isEmpty() && phone.isEmpty() && email.isEmpty()) {
	            resultArea.setText("❌ Please enter at least one field to search.");
	            resultArea.setStyle(resultArea.getStyle() + "-fx-text-fill: #F44336;");
	            return;
	        }
	        
	        // Search for matching customers
	        java.util.List<Customer> matchingCustomers = findMatchingCustomers(name, phone, email);
	        
	        if (matchingCustomers.isEmpty()) {
	            resultArea.setText("❌ No customer found with the provided information.\n\n" +
	                "Please check your details and try again.\n" +
	                "Make sure you enter the information exactly as you registered.");
	            resultArea.setStyle(resultArea.getStyle() + "-fx-text-fill: #F44336;");
	        } else if (matchingCustomers.size() == 1) {
	            Customer customer = matchingCustomers.get(0);
	            BankAccount account = accounts.get(customer.getCustomerID());
	            
	            StringBuilder result = new StringBuilder();
	            result.append("✅ Customer ID Found!\n\n");
	            result.append("═══════════════════════════════════\n");
	            result.append("Customer ID: ").append(customer.getCustomerID()).append("\n");
	            result.append("Name: ").append(customer.getName()).append("\n");
	            result.append("Phone: ").append(customer.getPhoneNumber()).append("\n");
	            result.append("Email: ").append(customer.getEmail()).append("\n");
	            
	            if (account != null) {
	                result.append("\nAccount Number: ").append(account.getAccountNo()).append("\n");
	                result.append("Current Balance: $").append(String.format("%.2f", account.getBalance())).append("\n");
	            }
	            
	            result.append("═══════════════════════════════════\n");
	            result.append("\n💡 Please save your Customer ID for future use!");
	            
	            resultArea.setText(result.toString());
	            resultArea.setStyle(resultArea.getStyle() + "-fx-text-fill: #4CAF50;");
	        } else {
	            // Multiple matches found
	            StringBuilder result = new StringBuilder();
	            result.append("⚠️ Multiple customers found with similar information:\n\n");
	            
	            for (int i = 0; i < matchingCustomers.size(); i++) {
	                Customer customer = matchingCustomers.get(i);
	                BankAccount account = accounts.get(customer.getCustomerID());
	                
	                result.append("Match #").append(i + 1).append(":\n");
	                result.append("Customer ID: ").append(customer.getCustomerID()).append("\n");
	                result.append("Name: ").append(customer.getName()).append("\n");
	                result.append("Phone: ").append(customer.getPhoneNumber()).append("\n");
	                result.append("Email: ").append(customer.getEmail()).append("\n");
	                
	                if (account != null) {
	                    result.append("Account #: ").append(account.getAccountNo()).append("\n");
	                }
	                result.append("─────────────────────────────────\n");
	            }
	            
	            result.append("\n💡 Please identify your correct Customer ID from the list above.");
	            
	            resultArea.setText(result.toString());
	            resultArea.setStyle(resultArea.getStyle() + "-fx-text-fill: #FF9800;");
	        }
	    });
	    
	    cancelBtn.setOnAction(e -> formStage.close());
	    
	    formLayout.getChildren().addAll(
	        titleLabel, instructionLabel, nameField, phoneField, emailField,
	        buttonBox, resultArea
	    );
	    
	    Scene scene = new Scene(formLayout, 500, 550);
	    formStage.setScene(scene);
	    formStage.show();
	}

	// Add this helper method to find matching customers:

	private java.util.List<Customer> findMatchingCustomers(String name, String phone, String email) {
	    java.util.List<Customer> matches = new java.util.ArrayList<>();
	    
	    for (Customer customer : customers) {
	        boolean nameMatch = name.isEmpty() || customer.getName().equalsIgnoreCase(name.trim());
	        boolean phoneMatch = phone.isEmpty() || customer.getPhoneNumber().equals(phone.trim());
	        boolean emailMatch = email.isEmpty() || customer.getEmail().equalsIgnoreCase(email.trim());
	        
	        // Customer matches if ALL provided fields match
	        if (nameMatch && phoneMatch && emailMatch) {
	            // But at least one field must have been provided and matched
	            if ((!name.isEmpty() && customer.getName().equalsIgnoreCase(name.trim())) &&
	                (!phone.isEmpty() && customer.getPhoneNumber().equals(phone.trim())) ||
	                (!email.isEmpty() && customer.getEmail().equalsIgnoreCase(email.trim()))) {
	                matches.add(customer);
	            }
	        }
	    }
	    
	    return matches;
	}
	
	private Button createStyledButton(String text, String color) {
	        Button button = new Button(text);
	        button.setPrefWidth(280);
	        button.setPrefHeight(45);
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
	    
    private void showCreateAccountForm() {
        Stage formStage = new Stage();
        formStage.setTitle("Create New Account");
        formStage.initOwner(primaryStage);
        
        VBox formLayout = new VBox(15);
        formLayout.setPadding(new Insets(30));
        formLayout.setStyle("-fx-background-color: #f8f9fa;");
        
        Label titleLabel = new Label("Create New Account");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: #333;");
        
        // Form fields
        TextField nameField = createTextField("Full Name");
        TextField phoneField = createTextField("Phone Number");
        TextField emailField = createTextField("Email Address");
        
        Button createBtn = createStyledButton("Create Account", "#4CAF50");
        Button cancelBtn = createStyledButton("Cancel", "#9E9E9E");
        
        Label resultLabel = new Label();
        resultLabel.setWrapText(true);
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(createBtn, cancelBtn);
        
        createBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            
            if (validateInput(name, phone, email, resultLabel)) {
                Customer customer = new Customer(name, phone, email);
                customers.add(customer);
                
                int accountNo = generateAccountNumber();
                BankAccount account = new BankAccount(accountNo, name, customer.getCustomerID());
                accounts.put(customer.getCustomerID(), account);
                
                showSuccessDialog("Account Created Successfully!", 
                    "Customer ID: " + customer.getCustomerID() + 
                    "\nAccount Number: " + accountNo +
                    "\n\nPlease save your Customer ID for future transactions.");
                
                formStage.close();
            }
        });
        
        cancelBtn.setOnAction(e -> formStage.close());
        
        formLayout.getChildren().addAll(
            titleLabel, nameField, phoneField, emailField, 
            buttonBox, resultLabel
        );
        
        Scene scene = new Scene(formLayout, 400, 350);
        formStage.setScene(scene);
        formStage.show();
    }
    
    private void showDepositForm() {
        showTransactionForm("Deposit Money", "Enter deposit amount:", true);
    }
    
    private void showWithdrawForm() {
        showTransactionForm("Withdraw Money", "Enter withdrawal amount:", false);
    }
    
    private void showTransactionForm(String title, String amountLabel, boolean isDeposit) {
        Stage formStage = new Stage();
        formStage.setTitle(title);
        formStage.initOwner(primaryStage);
        
        VBox formLayout = new VBox(15);
        formLayout.setPadding(new Insets(30));
        formLayout.setStyle("-fx-background-color: #f8f9fa;");
        
        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLbl.setStyle("-fx-text-fill: #333;");
        
        TextField customerIdField = createTextField("Customer ID");
        TextField amountField = createTextField(amountLabel);
        
        Button processBtn = createStyledButton(isDeposit ? "Deposit" : "Withdraw", 
                                             isDeposit ? "#4CAF50" : "#FF9800");
        Button cancelBtn = createStyledButton("Cancel", "#9E9E9E");
        
        Label resultLabel = new Label();
        resultLabel.setWrapText(true);
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(processBtn, cancelBtn);
        
        processBtn.setOnAction(e -> {
            String customerId = customerIdField.getText().trim();
            String amountStr = amountField.getText().trim();
            
            BankAccount account = accounts.get(customerId);
            if (account == null) {
                showError(resultLabel, "Account not found. Please check your Customer ID.");
                return;
            }
            
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    showError(resultLabel, "Amount must be positive.");
                    return;
                }
                
                boolean success = isDeposit ? account.depositMoney(amount) : account.withdrawMoney(amount);
                if (success) {
                    showSuccessDialog("Transaction Successful!", 
                        (isDeposit ? "Deposited: $" : "Withdrawn: $") + amount +
                        "\nNew Balance: $" + account.getBalance());
                    formStage.close();
                } else {
                    if (!isDeposit) {
                        showError(resultLabel, "Insufficient funds. Current balance: $" + account.getBalance());
                    }
                }
            } catch (NumberFormatException ex) {
                showError(resultLabel, "Please enter a valid amount.");
            }
        });
        
        cancelBtn.setOnAction(e -> formStage.close());
        
        formLayout.getChildren().addAll(
            titleLbl, customerIdField, amountField, 
            buttonBox, resultLabel
        );
        
        Scene scene = new Scene(formLayout, 400, 280);
        formStage.setScene(scene);
        formStage.show();
    }
    
    private void showBalanceCheck() {
        Stage formStage = new Stage();
        formStage.setTitle("Check Balance");
        formStage.initOwner(primaryStage);
        
        VBox formLayout = new VBox(15);
        formLayout.setPadding(new Insets(30));
        formLayout.setStyle("-fx-background-color: #f8f9fa;");
        
        Label titleLabel = new Label("Check Balance");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: #333;");
        
        TextField customerIdField = createTextField("Customer ID");
        
        Button checkBtn = createStyledButton("Check Balance", "#2196F3");
        Button cancelBtn = createStyledButton("Cancel", "#9E9E9E");
        
        Label resultLabel = new Label();
        resultLabel.setWrapText(true);
        resultLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(checkBtn, cancelBtn);
        
        checkBtn.setOnAction(e -> {
            String customerId = customerIdField.getText().trim();
            BankAccount account = accounts.get(customerId);
            
            if (account == null) {
                showError(resultLabel, "Account not found. Please check your Customer ID.");
            } else {
                resultLabel.setText("Current Balance: $" + String.format("%.2f", account.getBalance()));
                resultLabel.setStyle("-fx-text-fill: #4CAF50;");
            }
        });
        
        cancelBtn.setOnAction(e -> formStage.close());
        
        formLayout.getChildren().addAll(
            titleLabel, customerIdField, buttonBox, resultLabel
        );
        
        Scene scene = new Scene(formLayout, 400, 250);
        formStage.setScene(scene);
        formStage.show();
    }
    
    private void showAccountDetails() {
        Stage formStage = new Stage();
        formStage.setTitle("Account Details");
        formStage.initOwner(primaryStage);
        
        VBox formLayout = new VBox(15);
        formLayout.setPadding(new Insets(30));
        formLayout.setStyle("-fx-background-color: #f8f9fa;");
        
        Label titleLabel = new Label("Account Details");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: #333;");
        
        TextField customerIdField = createTextField("Customer ID");
        
        Button viewBtn = createStyledButton("View Details", "#607D8B");
        Button cancelBtn = createStyledButton("Cancel", "#9E9E9E");
        
        TextArea detailsArea = new TextArea();
        detailsArea.setEditable(false);
        detailsArea.setPrefRowCount(10);
        detailsArea.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(viewBtn, cancelBtn);
        
        viewBtn.setOnAction(e -> {
            String customerId = customerIdField.getText().trim();
            BankAccount account = accounts.get(customerId);
            
            if (account == null) {
                detailsArea.setText("Account not found. Please check your Customer ID.");
            } else {
                Customer customer = findCustomerById(customerId);
                StringBuilder details = new StringBuilder();
                
                details.append("=== ACCOUNT INFORMATION ===\n");
                details.append("Account Number: ").append(account.getAccountNo()).append("\n");
                details.append("Account Holder: ").append(account.getAccountHolder()).append("\n");
                details.append("Current Balance: $").append(String.format("%.2f", account.getBalance())).append("\n");
                details.append("Customer ID: ").append(account.getCustomerID()).append("\n\n");
                
                if (customer != null) {
                    details.append("=== CUSTOMER INFORMATION ===\n");
                    details.append("Name: ").append(customer.getName()).append("\n");
                    details.append("Phone: ").append(customer.getPhoneNumber()).append("\n");
                    details.append("Email: ").append(customer.getEmail()).append("\n");
                }
                
                detailsArea.setText(details.toString());
            }
        });
        
        cancelBtn.setOnAction(e -> formStage.close());
        
        formLayout.getChildren().addAll(
            titleLabel, customerIdField, buttonBox, detailsArea
        );
        
        Scene scene = new Scene(formLayout, 500, 450);
        formStage.setScene(scene);
        formStage.show();
    }
    
    private TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setPrefHeight(35);
        textField.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #ddd;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 8;"
        );
        return textField;
    }
    
    private boolean validateInput(String name, String phone, String email, Label resultLabel) {
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            showError(resultLabel, "All fields are required.");
            return false;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            showError(resultLabel, "Please enter a valid email address.");
            return false;
        }
        
        return true;
    }
    
//    private String forgotPassword(String name,String phone) {
//    	
//    }
    
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
    
    private Customer findCustomerById(String customerID) {
        return customers.stream()
                .filter(c -> c.getCustomerID().equals(customerID))
                .findFirst()
                .orElse(null);
    }
    
    private int generateAccountNumber() {
        return (int) ThreadLocalRandom.current().nextLong(100000000L, 999999999L);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
