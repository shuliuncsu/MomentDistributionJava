package momentdistribution;

/**
 *
 * @author Shu Liu
 */
public class MomentDistribution {

    public static final double TOLERANCE = 0.0001;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filename = "src/Example/Random1.txt";
        //Sequential.batchRun(filename);
        Parallel.singleRun(filename);
    }
}
