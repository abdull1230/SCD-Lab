class BankAccount {
    private double balance;
    
    // Lock for synchronization
    private final Object lock = new Object();
    
    public BankAccount(double balance) {
        this.balance = balance;
    }
    
    // Withdraw method with synchronization
    public void withdraw(double amount, String userName) {
        synchronized (lock) {
            // Critical section - withdraw money
            if (amount <= balance) {
                System.out.println(userName + " is attempting to withdraw " + amount + "...");
                try {
                    Thread.sleep(2000);  // Simulate withdrawal processing time
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                balance -= amount;
                System.out.println(userName + " successfully withdrew " + amount + ". Remaining balance: " + balance);
            } else {
                System.out.println(userName + " attempted to withdraw " + amount + ", but insufficient funds! Balance: " + balance);
            }
        }
    }
    
    public double getBalance() {
        return balance;
    }
}

class UserA extends Thread {
    private BankAccount account;
    
    public UserA(BankAccount account) {
        this.account = account;
    }
    
    @Override
    public void run() {
        account.withdraw(45000, "User A");
    }
}

class UserB extends Thread {
    private BankAccount account;
    
    public UserB(BankAccount account) {
        this.account = account;
    }
    
    @Override
    public void run() {
        account.withdraw(20000, "User B");
    }
}

public class Main {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(50000);  // Initial balance of 50,000
        
        // Create user threads
        UserA userA = new UserA(account);
        UserB userB = new UserB(account);
        
        // Start both threads
        userA.start();
        userB.start();
        
        // Wait for both threads to finish
        try {
            userA.join();
            userB.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Print final balance
        System.out.println("Final balance in the account: " + account.getBalance());
    }
}
