package parallel;

/**
 *
 * @author Shu Liu
 */
public class ParallelThread extends Thread {

    private final ParallelThread[] threadPool;
    private final int threadIndex;

    private Thread t;
    private int testValue = 1;
    private final int[] bufferValue;

    public ParallelThread(ParallelThread[] threadPool, int index) {
        this.threadPool = threadPool;
        this.threadIndex = index;
        bufferValue = new int[threadPool.length];
    }

    public void run() {
        //Random rand = new Random();
        while (testValue < 100) {
            //testValue += rand.nextInt(10);
            for (int i = 0; i < bufferValue.length; i++) {
                bufferValue[i] = testValue;
            }
//            try {
//                Thread.sleep(rand.nextInt(100));
//            } catch (InterruptedException ex) {
//                Logger.getLogger(ParallelThread.class.getName()).log(Level.SEVERE, null, ex);
//            }
            for (int i = 0; i < bufferValue.length; i++) {
                if (i != threadIndex) {
                    testValue += threadPool[i].bufferValue[threadIndex];
                }
            }
            System.out.println("ID: " + threadIndex + " value: " + testValue);
        }
    }

    public static void main(String[] args) {
        int numThreads = 4;
        ParallelThread[] pool = new ParallelThread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            pool[i] = new ParallelThread(pool, i);
        }
        for (int i = 0; i < numThreads; i++) {
            pool[i].start();
        }
    }
}
