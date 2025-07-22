# Banking System

A comprehensive JavaFX-based desktop banking application that provides secure account management, transaction processing, and customer data persistence.

## Features

### Core Banking Operations
- **Account Creation**: Register new customers with personal information and secure PIN setup
- **Secure Login**: Account number and 4-digit PIN authentication system
- **Money Transactions**: Deposit and withdraw funds with real-time balance updates
- **Account Recovery**: Recover account details using personal information
- **Balance Inquiry**: View current account balance and transaction status

### User Experience
- **Modern GUI**: Attractive JavaFX interface with gradient backgrounds and styled components
- **Responsive Design**: Hover effects, button animations, and intuitive navigation
- **Multi-window Interface**: Separate windows for different operations (login, registration, transactions)
- **Error Handling**: Comprehensive validation and user-friendly error messages

### Data Management
- **Data Persistence**: Automatic saving/loading of customer and account data
- **Session Management**: Automatic data saving on application exit

## 🛠 Technical Stack

- **Language**: Java
- **GUI Framework**: JavaFX
- **Data Persistence**: Java Serialization
- **Architecture**: Object-Oriented Design with separate classes for different concerns

## 📁 Project Structure

```
BankingSystem/
├── BankAccount.java         # Bank account model with transaction methods
├── Customer.java            # Customer information and PIN management
├── Main.java                # Main JavaFX application with UI components
├── Data/
│   └── DataManager.java     # Data persistence and file operations
```

## 🚀 Getting Started

### Prerequisites

- **Java Development Kit (JDK)**: Version 8 or higher
- **JavaFX Runtime**: Included with JDK 8, separate download for JDK 11+
- **IDE**: IntelliJ IDEA, VS Code or Eclipse (recommended)

### Installation & Setup

1. **Clone or Download** the project files
2. **Import** the project into your IDE
3. **Ensure JavaFX** is properly configured in your development environment
4. **Compile** all Java files in the BankingSystem package

### Running the Application

#### From IDE:
```bash
# Run the main class
Main.main()
```

#### From Command Line:
```bash
# Navigate to the project directory
cd path/to/BankingSystem

# Compile (if not already compiled)
javac *.java

# Run the application
java Main
```

## 📖 User Guide

### First Time Setup
1. **Launch** the application
2. **Click** "Create New Account" on the landing page
3. **Fill** in your personal information:
   - Full Name (required)
   - Phone Number (required)
   - Email Address (optional)
   - 4-Digit PIN (required)
4. **Confirm** your PIN and create the account
5. **Save** your account number and customer ID for future logins

### Logging In
1. **Click** "Login to Account" on the landing page
2. **Enter** your account number and 4-digit PIN
3. **Access** your dashboard upon successful authentication

### Making Transactions
1. **Login** to your account
2. **Choose** "Deposit Money" or "Withdraw Money"
3. **Enter** the transaction amount
4. **Confirm** the transaction
5. **View** updated balance on the dashboard

### Account Recovery
1. **Click** "Recover Account Details" on the landing page
2. **Enter** your name and/or phone number
3. **Retrieve** your account information

## 🔒 Security Features

- **PIN Authentication**: 4-digit secure PIN system
- **Input Validation**: Comprehensive validation for all user inputs
- **Data Encryption**: Secure storage of sensitive information
- **Session Management**: Automatic logout and data protection

## 💾 Data Storage

The application automatically creates and manages three data files:

- `customers.ser`: Customer personal information
- `accounts.ser`: Bank account details and balances
- 
**Important**: These files contain sensitive data. Keep them secure and backed up.

## 🛡 Error Handling

The application includes robust error handling for:
- Invalid input formats (non-numeric account numbers, amounts)
- Insufficient funds for withdrawals
- Duplicate account creation attempts
- File I/O operations
- Authentication failures

## 🧪 Testing

### Manual Testing Scenarios
1. **Account Creation**: Test with various input combinations
2. **Authentication**: Test correct and incorrect PIN entries
3. **Transactions**: Test deposits, withdrawals, and insufficient funds
4. **Data Persistence**: Close and reopen application to verify data saving
5. **Account Recovery**: Test with partial and complete information

## 🔧 Configuration

### Customizable Settings
- Account number range: Currently 100,000,000 - 999,999,999
- Customer ID format: "C" + 6-digit number
- Currency display: Currently shows "$" (can be modified to "R" for Rand)
- PIN length: 4 digits (configurable in validation method)

## 🚨 Known Issues & Limitations

- **Concurrent Access**: Not designed for multi-user concurrent access 
- **Network Support**: Local storage only, no network/database integration
- **PIN Recovery**: No PIN reset functionality 

## 🔮 Future Enhancements

### Planned Features
- **Interest Calculation**: Automatic interest accrual
- **Admin Panel**: Administrative functions and reporting
- **Database Integration**: MySQL/PostgreSQL support
- **Web Interface**: Browser-based access
- **Mobile App**: Android/iOS companion app

### Technical Improvements
- **Encryption**: Advanced encryption for data files
- **Backup System**: Automated data backup and recovery
- **Multi-language**: Internationalization support
- **API Integration**: External service integration
- **Cloud Storage**: Cloud-based data synchronization

## 🤝 Contributing

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'Add some AmazingFeature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Open** a Pull Request

## 📞 Support

For technical support or questions:
- **Email**: support@hexsoft.com
- **Documentation**: Check inline code comments for detailed implementation notes
- **Issues**: Report bugs through the project issue tracker

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- JavaFX community for GUI components and styling examples
- Java serialization documentation for data persistence implementation
- Security best practices for PIN and authentication handling

---

**Banking System** - Your Trusted Banking Partner 
*Secure • Reliable • 24/7 Access*
