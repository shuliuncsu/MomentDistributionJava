package momentdistribution;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import structure.Node;
import structure.Structure;
import structure.StructureGenerator;

/**
 *
 * @author Shu Liu
 */
public class Sequential {

    public static int analyzeStructure_Linear(Structure structure) {
        System.out.print("Start analyze... ");
        long timingStart = new Date().getTime();
        Node[] nodes = structure.getNodes();
        boolean finish = false;
        int iterationCount = 0;
        while (!finish) {
            finish = true;
            iterationCount++;
            for (Node node : nodes) {
                finish = node.redistributeMoment(MomentDistribution.TOLERANCE) && finish;
            }
        }
        long timingEnd = new Date().getTime();
        System.out.println("Finished after " + iterationCount + " iterations. Time cost: " + (timingEnd - timingStart) + " ms.");
        return iterationCount;
    }

    public static void singleRun(String filename) {
        Structure structure = Structure.createStructureFromFile(filename);
        analyzeStructure_Linear(structure);
    }

    public static void batchRun(String filename) {
        StructureGenerator sg = new StructureGenerator();
        try {
            PrintStream out = new PrintStream("src/Example/summary.txt");
            int numOfNodes = 100;
            while (numOfNodes <= 1000000) {
                for (int i = 0; i < 3; i++) {
                    sg.generateStructure_AllRandom(filename, numOfNodes, numOfNodes * 2);
                    Structure structure = Structure.createStructureFromFile(filename);
                    System.out.println(analyzeStructure_Linear(structure));
                }
                numOfNodes *= 10;
            }
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MomentDistribution.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
