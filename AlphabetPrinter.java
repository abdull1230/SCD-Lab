package Lab5;

import java.util.Random;
class PrintAlphabetThread extends Thread {
    private char characterToPrint;
    
    public PrintAlphabetThread(char character) {
        this.characterToPrint = character;
    }
    
    @Override
    public void run() {
        try {
            Random rand = new Random();
            int sleepTime = rand.nextInt(401) + 100;
           
            Thread.sleep(sleepTime);

            System.out.print(characterToPrint + " ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void stopThread() {
    	running = false;
    }
}
public class AlphabetPrinter {

	public static void main(String[] args) {
		for (char ch = 'A'; ch <= 'Z'; ch++) {
            
            PrintAlphabetThread thread = new PrintAlphabetThread(ch);
            thread.start();
            
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


