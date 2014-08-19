package momentdistribution;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Shu Liu
 */
public class MomentDistribution {

    public static final double TOLERANCE = 0.1;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filename = "src/Example/Random3.txt";
        Structure structure = createStructureFromFile(filename);
        //structure.printStructure();
        analyzeStructure_Linear(structure);
    }

    public static Structure createStructureFromFile(String filename) {
        try {
            Scanner input = new Scanner(new File(filename));
            Structure structure = new Structure();

            int numOfNodes = input.nextInt();
            for (int i = 0; i < numOfNodes; i++) {
                structure.addNode(new Node(input.nextInt(), input.next().startsWith("F")));
            }

            int numOfBeams = input.nextInt();
            for (int i = 0; i < numOfBeams; i++) {
                structure.connectNodes(input.nextInt(), input.nextDouble(), input.nextDouble(), input.nextDouble(),
                        input.nextInt(), input.nextDouble(), input.nextDouble(), input.nextDouble());
            }

            structure.normalize();

            return structure;
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
            return null;
        }
    }

    public static void analyzeStructure_Linear(Structure structure) {
        Node[] nodes = structure.getNodes();
        boolean finish = false;
        int iterationCount = 0;
        while (!finish) {
            finish = true;
            iterationCount++;
            System.out.println("Iteration: " + iterationCount + ", Max Error: " + structure.getMaxUnbalanced());
            for (Node node : nodes) {
                finish = node.redistributeMoment(TOLERANCE) && finish;
            }
            //structure.printStructure();
        }
        System.out.println("Finished after " + iterationCount + " iterations.");
        //structure.printStructure();
    }
}
