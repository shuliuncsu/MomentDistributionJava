package SequentialAsy;

import Test.MomentDistribution;
import Test.StructureGenerator;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shu Liu
 */
public class SequentialAsy {

    public static int analyzeStructureAsy(StructureAsy structure) {
        System.out.print("Start analyze (sequential ASY)... ");
        long timingStart = new Date().getTime();
        NodeAsy[] nodes = structure.getNodes();
        boolean finish = false;
        int iterationCount = 0;
        while (!finish) {
            finish = true;
            iterationCount++;
            for (NodeAsy node : nodes) {
                finish = node.redistributeMoment(MomentDistribution.TOLERANCE) && finish;

                //structure.printStructure();
            }
            //structure.printStructure();
        }
        long timingEnd = new Date().getTime();
        System.out.println("Finished after " + iterationCount + " iterations. Time cost: " + (timingEnd - timingStart) + " ms.\n");
        return iterationCount;
    }

    public static StructureAsy singleRun(String filename) {
        StructureAsy structure = StructureAsy.createStructureFromFile(filename);
        analyzeStructureAsy(structure);
        return structure;
    }

    public static void batchRun(String filename) {
        StructureGenerator sg = new StructureGenerator();
        try {
            PrintStream out = new PrintStream("src/Example/summary.txt");
            int numOfNodes = 100;
            while (numOfNodes <= 1000000) {
                for (int i = 0; i < 3; i++) {
                    sg.generateStructure_AllRandom(filename, numOfNodes, numOfNodes * 2);
                    StructureAsy structure = StructureAsy.createStructureFromFile(filename);
                    System.out.println(analyzeStructureAsy(structure));
                }
                numOfNodes *= 10;
            }
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MomentDistribution.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filename = "src/Example/Random2.txt";
        singleRun(filename);
    }
}
