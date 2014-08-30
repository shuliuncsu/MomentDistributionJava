package Test;

import Sequential.Node;
import Sequential.Sequential;
import Sequential.Structure;
import SequentialAsy.NodeAsy;
import SequentialAsy.SequentialAsy;
import SequentialAsy.StructureAsy;

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
        String filename = "src/Example/Random2.txt";
        System.out.println("Error: " + compare(filename));
    }

    public static double compare(String filename) {
        double maxDiff = 0;
        Structure seq = Sequential.singleRun(filename);
        Node[] nodes = seq.getNodes();
        StructureAsy asy = SequentialAsy.singleRun(filename);
        NodeAsy[] nodesAsy = asy.getNodes();
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].getBeams().size(); j++) {
                System.out.printf("Node: %d, Beam %d,\tS.M.: %5.0f,\tA.M.:%5.0f\n", i, j, nodes[i].getBeams().get(j).getMoment(), nodesAsy[i].getBeams().get(j).getMoment());
                maxDiff = Math.max(maxDiff,
                        Math.abs(nodes[i].getBeams().get(j).getMoment() - nodesAsy[i].getBeams().get(j).getMoment()));
            }
        }
        return maxDiff;
    }
}
