package Test;

import java.io.PrintStream;
import java.util.Random;

/**
 *
 * @author Shu Liu
 */
public class StructureGenerator {

    public static final int MAX_NUM_NODES = 100000;
    public static final int AVERAGE_MOMENT = 500;
    public static final double FIXED_RATE = 0.001;
    public static final double BEAM_NODE_RATIO = 2;
    public static final double MAX_COF = 0.7;
    public static final double MIN_COF = 0.2;
    public static final double MIN_DF = 0.2;

    public static void main(String[] args) {
        StructureGenerator sg = new StructureGenerator();
        String filename = "src/Example/Node1e5.txt";
        sg.generateStructure_AllRandom(filename, MAX_NUM_NODES, (int) (BEAM_NODE_RATIO * MAX_NUM_NODES));
    }

    public void generateStructure_Linear(String filename, int numOfNodes) {
        try {
            int numOfBeams = numOfNodes - 1;

            PrintStream out = new PrintStream(filename);
            Random rand = new Random();

            out.println(numOfNodes);
            System.out.print("Generating file for " + numOfNodes + " nodes...");

            for (int nodeIndex = 1; nodeIndex <= numOfNodes; nodeIndex++) {
                out.println(nodeIndex + " " + (nodeIndex == 1 || nodeIndex == numOfNodes ? "F" : "N"));
            }

            out.println(numOfBeams);

            for (int beamIndex = 1; beamIndex <= numOfBeams; beamIndex++) {
                int node1 = beamIndex;
                int node2 = beamIndex + 1;

                out.printf("%d %.1f %.1f %.1f\t%d %.1f %.1f %.1f\n",
                        node1, rand.nextDouble() + MIN_DF, rand.nextDouble() * (MAX_COF - MIN_COF) + MIN_COF, AVERAGE_MOMENT * rand.nextDouble(),
                        node2, rand.nextDouble() + MIN_DF, rand.nextDouble() * (MAX_COF - MIN_COF) + MIN_COF, -AVERAGE_MOMENT * rand.nextDouble());
            }

            out.close();
            System.out.println(" Done");
        } catch (Exception ex) {
            System.out.println("Error when create file");
            ex.printStackTrace();
        }
    }

    public void generateStructure_AllRandom(String filename, int numOfNodes, int numOfBeams) {
        try {
            PrintStream out = new PrintStream(filename);
            Random rand = new Random();

            out.println(numOfNodes);
            System.out.print("Generating file for " + numOfNodes + " nodes...");

            for (int nodeIndex = 1; nodeIndex <= numOfNodes; nodeIndex++) {
                out.println(nodeIndex + " " + (rand.nextDouble() < FIXED_RATE ? "F" : "N"));
            }

            out.println(numOfBeams);

            for (int beamIndex = 1; beamIndex <= numOfBeams; beamIndex++) {
                int node1 = rand.nextInt(numOfNodes) + 1;
                int node2 = rand.nextInt(numOfNodes) + 1;
                while (node2 == node1) {
                    node2 = rand.nextInt(numOfNodes) + 1;
                }

                out.printf("%d %.1f %.1f %.1f\t%d %.1f %.1f %.1f\n",
                        node1, rand.nextDouble() + MIN_DF, rand.nextDouble() * (MAX_COF - MIN_COF) + MIN_COF, AVERAGE_MOMENT * rand.nextDouble(),
                        node2, rand.nextDouble() + MIN_DF, rand.nextDouble() * (MAX_COF - MIN_COF) + MIN_COF, -AVERAGE_MOMENT * rand.nextDouble());
            }

            out.close();
            System.out.println(" Done");
        } catch (Exception ex) {
            System.out.println("Error when create file");
            ex.printStackTrace();
        }
    }
}
