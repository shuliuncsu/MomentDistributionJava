package Sequential;

import Test.MomentDistribution;
import java.util.Date;
import java.util.PriorityQueue;

/**
 *
 * @author Shu Liu
 */
public class Sequential {

    public static int analyzeStructure_Linear(Structure structure) {
        System.out.print("Start analyze (sequential, default linear order)... ");
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
//            double maxError = 0;
//            for (Node node : nodes) {
//                maxError = Math.max(maxError, Math.abs(node.getUnbalancedMoment()));
//            }
//            System.out.println(maxError);
        }
        long timingEnd = new Date().getTime();
        System.out.println("Finished after " + iterationCount + " iterations. Time cost: " + (timingEnd - timingStart) + " ms.\n");
        return iterationCount;
    }

    public static int analyzeStructure_MaxHeap(Structure structure) {
        System.out.print("Start analyze (sequential, max heap order)... ");
        long timingStart = new Date().getTime();
        PriorityQueue<Node> nodes = new PriorityQueue(structure.getNodesSet());
        int iterationCount = 0;
        while (Math.abs(nodes.peek().getUnbalancedMoment()) > MomentDistribution.TOLERANCE) {
            nodes.peek().redistributeMoment(MomentDistribution.TOLERANCE);
        }
        long timingEnd = new Date().getTime();
        System.out.println("Finished after " + iterationCount + " iterations. Time cost: " + (timingEnd - timingStart) + " ms.\n");
        return iterationCount;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filename = "src/Example/Random20.txt";
        Structure structure = Structure.createStructureFromFile(filename);
        analyzeStructure_Linear(structure);
    }
}
