package Test;

import Sequential.Node;
import Sequential.Sequential;
import Sequential.Structure;

/**
 *
 * @author Shu Liu
 */
public class MomentDistribution {

    public static final double TOLERANCE = 0.01;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filename = "src/Example/Random10.txt";
        System.out.println("Error: " + compare(filename));
    }

    public static double compare(String filename) {
        double maxDiff = 0;

        Structure seqLinear = Structure.createStructureFromFile(filename);
        //seqLinear.printStructure();
        Sequential.analyzeStructure_Linear(seqLinear);
        Node[] nodesSeqLinear = seqLinear.getNodes();

        Structure seqHeap = Structure.createStructureFromFile(filename);
        Sequential.analyzeStructure_MaxHeap(seqHeap);
        Node[] nodesSeqHeap = seqHeap.getNodes();

        //StructureAsy asy = SequentialAsy.singleRun(filename);
        //NodeAsy[] nodesAsy = asy.getNodes();
        for (int i = 0; i < nodesSeqLinear.length; i++) {
            for (int j = 0; j < nodesSeqLinear[i].getEnds().size(); j++) {
                //System.out.printf("Node: %d, End %d,\tS.L.M.: %5.0f,\tS.H.M.:%5.0f\n", (i + 1), (j + 1), nodesSeqLinear[i].getEnds().get(j).getMoment(), nodesSeqHeap[i].getEnds().get(j).getMoment());
                maxDiff = Math.max(maxDiff,
                        Math.abs(nodesSeqLinear[i].getEnds().get(j).getMoment() - nodesSeqHeap[i].getEnds().get(j).getMoment()));
            }
        }
        return maxDiff;
    }
}
