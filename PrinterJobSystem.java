class PrinterJob {
    private int availablePages; // Pages in the printer tray
    
    public PrinterJob(int initialPages) {
        this.availablePages = initialPages;
    }
    
    // Method for the print thread to print pages
    public synchronized void printPages(int pagesToPrint) {
        while (pagesToPrint > availablePages) {
            try {
                System.out.println("Not enough pages in the tray! Waiting for refill...");
                wait(); // Wait until pages are refilled in the tray
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Print the pages
        availablePages -= pagesToPrint;
        System.out.println("Printing " + pagesToPrint + " pages. Remaining pages: " + availablePages);
    }
    
    // Method for the tray thread to refill pages
    public synchronized void refillPages(int pagesToAdd) {
        availablePages += pagesToAdd;
        System.out.println("Refilled printer tray with " + pagesToAdd + " pages. Available pages: " + availablePages);
        notify(); // Notify the print thread that pages are now available
    }
}

class PrintThread extends Thread {
    private PrinterJob printerJob;
    private int pagesToPrint;
    
    public PrintThread(PrinterJob printerJob, int pagesToPrint) {
        this.printerJob = printerJob;
        this.pagesToPrint = pagesToPrint;
    }
    
    @Override
    public void run() {
        printerJob.printPages(pagesToPrint);
    }
}

class RefillThread extends Thread {
    private PrinterJob printerJob;
    private int pagesToAdd;
    
    public RefillThread(PrinterJob printerJob, int pagesToAdd) {
        this.printerJob = printerJob;
        this.pagesToAdd = pagesToAdd;
    }
    
    @Override
    public void run() {
        printerJob.refillPages(pagesToAdd);
    }
}

public class PrinterJobSystem {
    public static void main(String[] args) {
        PrinterJob printerJob = new PrinterJob(10);  // Initial pages in tray
        
        // Create threads
        Thread printThread = new PrintThread(printerJob, 15); // Print job for 15 pages
        Thread refillThread = new RefillThread(printerJob, 10); // Refill the tray with 10 pages
        
        // Start the threads
        printThread.start();
        refillThread.start();
        
        try {
            printThread.join();  // Wait for the print thread to complete
            refillThread.join(); // Wait for the refill thread to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Printing job completed.");
    }
}
