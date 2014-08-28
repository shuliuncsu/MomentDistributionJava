package momentdistribution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        String filename = "src/Example/Batch.txt";
        batchRun(filename);
    }

    public static Structure createStructureFromFile(String filename) {
        try {
            Scanner input = new Scanner(new File(filename));
            Structure structure = new Structure();

            int numOfNodes = input.nextInt();
            System.out.print("Reading from file " + filename + " with " + numOfNodes + " nodes...");
            for (int i = 0; i < numOfNodes; i++) {
                structure.addNode(new Node(input.nextInt(), input.next().startsWith("F")));
            }

            int numOfBeams = input.nextInt();
            for (int i = 0; i < numOfBeams; i++) {
                structure.connectNodes(input.nextInt(), input.nextDouble(), input.nextDouble(), input.nextDouble(),
                        input.nextInt(), input.nextDouble(), input.nextDouble(), input.nextDouble());
            }

            structure.normalize();
            System.out.println(" Done");
            return structure;
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
            return null;
        }
    }

    public static int analyzeStructure_Linear(Structure structure) {
        System.out.print("Start analyze... ");
        //timing starts
        long timingStart = new Date().getTime();
        Node[] nodes = structure.getNodes();
        boolean finish = false;
        int iterationCount = 0;
        while (!finish) {
            finish = true;
            iterationCount++;
            //System.out.println("Iteration: " + iterationCount + ", Max Error: " + structure.getMaxUnbalanced());
            for (Node node : nodes) {
                finish = node.redistributeMoment(TOLERANCE) && finish;
            }
            //structure.printStructure();
        }
        //timing ends
        long timingEnd = new Date().getTime();
        System.out.println("Finished after " + iterationCount
                + " iterations. Time cost: " + (timingEnd - timingStart) + " ms.");
        return iterationCount;
        //structure.printStructure();
    }

    public static void singleRun(String filename) {
        Structure structure = createStructureFromFile(filename);
        //structure.printStructure();
        analyzeStructure_Linear(structure);
    }

    public static void batchRun(String filename) {
        StructureGenerator sg = new StructureGenerator();
        try {
            PrintStream out = new PrintStream("src/Example/summary.txt");

            int numOfNodes = 100;
            while (numOfNodes <= 1000000) {
                for (int i = 0; i < 3; i++) {
                    //sg.generateStructure_AllRandom(filename, numOfNodes, 2 * numOfNodes);
                    sg.generateStructure_AllRandom(filename, numOfNodes, numOfNodes * 2);

                    Structure structure = createStructureFromFile(filename);
                    out.println(structure.nodeMap.size() + " " + analyzeStructure_Linear(structure));
                }
                numOfNodes *= 10;
            }

            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MomentDistribution.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
