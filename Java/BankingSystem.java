import java.io.*;
import java.util.*;

class BankSystem {
    final int max_limit = 20;
    final int min_limit = 1;
    final double min_bal = 500;

    private String[] name = new String[max_limit];
    private int[] accNo = new int[max_limit];
    private String[] accType = new String[max_limit];
    private double[] balamount = new double[max_limit];

    static int totRec = 0;

    BankSystem() {
        // Load existing records from file into memory at startup
        loadFromFile();
    }

    // Load existing data from the file
    private void loadFromFile() {
        try (Scanner scanner = new Scanner(new File("E:\\Projects\\Java\\bank_records.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] details = line.split(" \\| ");
                accNo[totRec] = Integer.parseInt(details[0].split(": ")[1]);
                name[totRec] = details[1].split(": ")[1];
                accType[totRec] = details[2].split(": ")[1];
                balamount[totRec] = Double.parseDouble(details[3].split(": ")[1]);
                totRec++;
            }
        } catch (IOException e) {
            System.out.println("Error loading data from file: " + e.getMessage());
        }
    }

    // Save all account records to the file
    private void saveToFile() {
        try (FileWriter fw = new FileWriter("E:\\Projects\\Java\\bank_records.txt")) {
            for (int i = 0; i < totRec; i++) {
                fw.write("Account Number: " + accNo[i] + " | ");
                fw.write("Name: " + name[i] + " | ");
                fw.write("Account Type: " + accType[i] + " | ");
                fw.write("Balance: " + balamount[i] + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    // Create a new account
    public void newEntry() {
        if (totRec >= max_limit) {
            System.out.println("\n\nSorry, we cannot admit you into our bank. Maximum limit reached.\n");
            return;
        }

        totRec++;
        System.out.println("\n\n===== SAVING NEW ENTRY =====");

        try {
            BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));

            accNo[totRec - 1] = 1000 + totRec; // Auto-increment Account Number (starting from 1001)
            System.out.println("Account Number: " + accNo[totRec - 1]);

            // Ensure customer name only contains alphabetic characters (no digits)
            String customerName;
            while (true) {
                System.out.print("Enter the name of the Customer: ");
                customerName = obj.readLine();
                if (customerName.matches("[a-zA-Z ]+")) { // Check if it contains only letters and spaces
                    break; // Valid name, break the loop
                } else {
                    System.out.println("Name should only contain alphabets and spaces. Please try again.");
                }
            }
            name[totRec - 1] = customerName;

            // Ensure valid account type: only "Savings" or "Current" (case insensitive)
            String accountType;
            while (true) {
                System.out.print("Enter Account Type (Savings/Current): ");
                accountType = obj.readLine().trim();
                // Check if the account type is either "Savings" or "Current" (case insensitive)
                if (accountType.equalsIgnoreCase("Savings") || accountType.equalsIgnoreCase("Current")) {
                    break; // Valid account type, break the loop
                } else {
                    System.out.println("Invalid account type. Please enter either 'Savings' or 'Current' only.");
                }
            }
            accType[totRec - 1] = accountType;

            // Ensure deposit amount is numeric and greater than or equal to Rs. 500
            double amount;
            while (true) {
                System.out.print("Enter Initial Amount to be deposited (minimum Rs. 500): ");
                try {
                    amount = Double.parseDouble(obj.readLine());
                    if (amount >= min_bal) {
                        break; // Valid amount, break the loop
                    } else {
                        System.out.println("Initial amount must be at least Rs. 500. Try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid numeric value for the deposit.");
                }
            }
            balamount[totRec - 1] = amount;

            // Save account details to file
            saveToFile();

            System.out.println("New account created successfully!\n\n");

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error in saving new entry: " + e.getMessage());
        }
    }

    // Display account details
    public void display() {
        try {
            BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the account number to display: ");
            int account = Integer.parseInt(obj.readLine());

            // Search for the account number
            boolean found = false;
            for (int i = 0; i < totRec; i++) {
                if (accNo[i] == account) {
                    found = true;
                    System.out.println("\n\n===== ACCOUNT DETAILS =====");
                    System.out.println("Account Number: " + accNo[i]);
                    System.out.println("Name: " + name[i]);
                    System.out.println("Account Type: " + accType[i]);
                    System.out.println("Balance Amount: " + balamount[i] + "\n");
                    break;
                }
            }
            if (!found) {
                System.out.println("\nInvalid Account Number\n");
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error in displaying record: " + e.getMessage());
        }
    }

    // Deposit money into an account
    public void deposit() {
        try {
            BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter Account Number: ");
            int account = Integer.parseInt(obj.readLine());

            boolean found = false;
            int index = -1;
            for (int i = 0; i < totRec; i++) {
                if (accNo[i] == account) {
                    found = true;
                    index = i;
                    break;
                }
            }

            if (!found) {
                System.out.println("\nInvalid Account Number\n");
                return;
            }

            System.out.print("Enter Amount to Deposit: ");
            double amount = Double.parseDouble(obj.readLine());

            balamount[index] += amount;

            // Save updated records to file
            saveToFile();

            System.out.println("\nDeposit Successful!");
            System.out.println("Updated Balance: " + balamount[index] + "\n");

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error in depositing amount: " + e.getMessage());
        }
    }

    // Withdraw money from an account
    public void withdraw() {
        try {
            BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter Account Number: ");
            int account = Integer.parseInt(obj.readLine());

            boolean found = false;
            int index = -1;
            for (int i = 0; i < totRec; i++) {
                if (accNo[i] == account) {
                    found = true;
                    index = i;
                    break;
                }
            }

            if (!found) {
                System.out.println("\nInvalid Account Number\n");
                return;
            }

            System.out.println("Current Balance: " + balamount[index]);
            System.out.print("Enter Amount to Withdraw: ");
            double amount = Double.parseDouble(obj.readLine());

            if (balamount[index] - amount >= min_bal) {
                balamount[index] -= amount;

                // Save updated records to file
                saveToFile();

                System.out.println("\nWithdrawal Successful!");
                System.out.println("Updated Balance: " + balamount[index] + "\n");
            } else {
                System.out.println("\nInsufficient Balance. Minimum balance of Rs. 500 must be maintained.\n");
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error in withdrawing amount: " + e.getMessage());
        }
    }

    // Search account by name
    public void searchByName() {
        try {
            BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter Customer Name to Search: ");
            String searchName = obj.readLine();

            boolean found = false;
            for (int i = 0; i < totRec; i++) {
                if (name[i].equalsIgnoreCase(searchName)) {
                    System.out.println("\n\n===== ACCOUNT DETAILS =====");
                    System.out.println("Account Number: " + accNo[i]);
                    System.out.println("Name: " + name[i]);
                    System.out.println("Account Type: " + accType[i]);
                    System.out.println("Balance Amount: " + balamount[i] + "\n");
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("\nCustomer not found.\n");
            }

        } catch (IOException e) {
            System.out.println("Error in searching account: " + e.getMessage());
        }
    }

    // Delete an account
    public void deleteAccount() {
        try {
            BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter Account Number to Delete: ");
            int account = Integer.parseInt(obj.readLine());

            boolean found = false;
            int index = -1;
            for (int i = 0; i < totRec; i++) {
                if (accNo[i] == account) {
                    found = true;
                    index = i;
                    break;
                }
            }

            if (!found) {
                System.out.println("\nInvalid Account Number\n");
                return;
            }

            // Shift the records to remove the deleted account
            for (int i = index; i < totRec - 1; i++) {
                accNo[i] = accNo[i + 1];
                name[i] = name[i + 1];
                accType[i] = accType[i + 1];
                balamount[i] = balamount[i + 1];
            }

            totRec--; // Decrement the total records count

            // Save updated records to file
            saveToFile();

            System.out.println("\nAccount Deleted Successfully!");

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error in deleting account: " + e.getMessage());
        }
    }
}

public class BankingSystem {
    public static void main(String[] args) {
        BankSystem BW_obj = new BankSystem();
        BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
        int choice = 0;

        do {
            System.out.println("===== BANKING SYSTEM MENU =====");
            System.out.println("1) New Record Entry");
            System.out.println("2) Display Record Details");
            System.out.println("3) Deposit");
            System.out.println("4) Withdraw");
            System.out.println("5) Search Account by Name");
            System.out.println("6) Delete Account");
            System.out.println("7) Exit");
            System.out.print("Enter Your Choice: ");

            try {
                choice = Integer.parseInt(obj.readLine());

                switch (choice) {
                    case 1:
                        BW_obj.newEntry();
                        break;
                    case 2:
                        BW_obj.display();
                        break;
                    case 3:
                        BW_obj.deposit();
                        break;
                    case 4:
                        BW_obj.withdraw();
                        break;
                    case 5:
                        BW_obj.searchByName();
                        break;
                    case 6:
                        BW_obj.deleteAccount();
                        break;
                    case 7:
                        System.out.println("\nExiting... Thank you for using the banking system!");
                        break;
                    default:
                        System.out.println("\nInvalid Option. Please try again.");
                }
            } catch (IOException e) {
                System.out.println("\nError: " + e.getMessage());
            }
        } while (choice != 7);
    }
}
