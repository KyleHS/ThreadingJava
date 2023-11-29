import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadCounters {
    private static final Lock resource = new ReentrantLock();
    private static final Condition th = resource.newCondition();
    private static boolean ready = false;

    // Method to count up to 20
    public static void countingUp() throws InterruptedException {
        for (int i = 1; i <= 20; ++i) {  //starts at 1 and counts to 20
            resource.lock();
            System.out.println("Counting Up: " + i);
            if (i == 20) {
                ready = true;
                th.signal(); // Notifies waiting thread
            }
            resource.unlock();//releases resources for next thread
            Thread.sleep(100); // Delay to emulate counting
        }
    }

    // Method to count down to 0
    public static void countingDown() throws InterruptedException {
        resource.lock();
        try {
            while (!ready) {
                th.await(); // Waits until notified
            }

            for (int i = 20; i >= 0; --i) {  //starts now at 20 and counts down to 0
                System.out.println("Counting Down: " + i);
                Thread.sleep(100); // Delay to emulate counting
            }
        } finally {
            resource.unlock(); //releases resources afterwards 
        }
    }

    // Main program to test methods
    public static void main(String[] args) throws InterruptedException {
    	//start
        System.out.println("|||--Program Start--|||");

        //first thread
        Thread t1 = new Thread(() -> {
            try {
                countingUp(); //initiates first counting method
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //second thread
        Thread t2 = new Thread(() -> {
            try {
                countingDown(); //initiates second counting method
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //begins execution of threads
        t1.start();
        t1.join();
        t2.start();

        //returns the threads
        
        t2.join();

        //end
        System.out.println("|||--Program End--|||");
    }
}
