package momentdistribution;

import java.io.PrintStream;
import java.util.Random;

/**
 *
 * @author Shu Liu
 */
public class StructureGenerator {

    public static final int MAX_NUM_NODES = 100;
    public static final int AVERAGE_MOMENT = 500;
    public static final double FIXED_RATE = 0.1;
    public static final double BEAM_NODE_RATIO = 2;
    public static final double MAX_COF = 0.7;

    public static void main(String[] args) {
        StructureGenerator sg = new StructureGenerator();
        String filename = "src/Example/RandomStructure1.txt";
        sg.generateStructure(filename);
    }

    public void generateStructure(String filename) {
        try {
            PrintStream out = new PrintStream(filename);
            Random rand = new Random();

            int numOfNodes = rand.nextInt(MAX_NUM_NODES);
            out.println(numOfNodes);

            for (int nodeIndex = 1; nodeIndex <= numOfNodes; nodeIndex++) {
                out.println(nodeIndex + " " + (rand.nextDouble() < FIXED_RATE ? "F" : "N"));
            }

            int numOfBeams = (int) (BEAM_NODE_RATIO * numOfNodes);
            out.println(numOfBeams);

            for (int beamIndex = 1; beamIndex <= numOfBeams; beamIndex++) {
                int node1 = rand.nextInt(numOfNodes) + 1;
                int node2 = rand.nextInt(numOfNodes) + 1;
                while (node2 == node1) {
                    node2 = rand.nextInt(numOfNodes) + 1;
                }

                out.printf("%d %.1f %.1f %.1f %d %.1f %.1f %.1f\n",
                        node1, rand.nextDouble(), rand.nextDouble() * MAX_COF, AVERAGE_MOMENT * rand.nextDouble(),
                        node2, rand.nextDouble(), rand.nextDouble() * MAX_COF, -AVERAGE_MOMENT * rand.nextDouble());
            }

            out.close();
        } catch (Exception ex) {
            System.out.println("Error when create file");
            ex.printStackTrace();
        }
    }
}
