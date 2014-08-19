package momentdistribution;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Shu Liu
 */
public class Structure {

    HashMap<Integer, Node> nodeMap;

    public Structure() {
        nodeMap = new HashMap();
    }

    public void addNode(Node newNode) {
        if (nodeMap.containsKey(newNode.id)) {
            System.out.println("Warning: " + newNode.toString() + " already exists");
        } else {
            nodeMap.put(newNode.id, newNode);
        }
    }

    public void connectNodes(int nodeID1, double df1, double cof1, double moment1,
            int nodeID2, double df2, double cof2, double moment2) {

        if (!nodeMap.containsKey(nodeID1)) {
            System.out.println("Error: node " + nodeID1 + " doesn't exists in structure");
            return;
        }
        if (!nodeMap.containsKey(nodeID2)) {
            System.out.println("Error: node " + nodeID2 + " doesn't exists in structure");
            return;
        }

        Node node1 = nodeMap.get(nodeID1);
        Node node2 = nodeMap.get(nodeID2);

        Beam beam1 = new Beam();
        Beam beam2 = new Beam();

        node1.beams.add(beam1);
        node2.beams.add(beam2);

        beam1.df = df1;
        beam1.cof = cof1;
        beam1.moment = moment1;
        beam1.otherEndNode = node2;
        beam1.otherEndBeam = beam2;

        beam2.df = df2;
        beam2.cof = cof2;
        beam2.moment = moment2;
        beam2.otherEndNode = node1;
        beam2.otherEndBeam = beam1;
    }

    public void normalize() {
        Iterator<Map.Entry<Integer, Node>> iter = nodeMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, Node> entry = iter.next();
            if (entry.getValue().isIsolated()) {
                iter.remove();
            } else {
                entry.getValue().normalize();
            }
        }
    }

    public void printStructure() {
        System.out.println("Total number of nodes: " + nodeMap.size());
        for (Node node : nodeMap.values()) {
            System.out.println(node);
        }
    }

    public Node[] getNodes() {
        Node[] nodes = new Node[nodeMap.size()];
        int index = 0;
        for (Node node : nodeMap.values()) {
            nodes[index] = node;
            index++;
        }
        return nodes;
    }

    public double getMaxUnbalanced() {
        double max = 0;
        for (Node node : nodeMap.values()) {
            max = Math.max(max, Math.abs(node.getUnbalancedMoment()));
        }

        return max;
    }

//    public boolean withinTolerance(double tolerance) {
//        for (Node node : nodeMap.values()) {
//            if (Math.abs(node.getUnbalancedMoment()) > tolerance) {
//                return false;
//            }
//        }
//
//        return true;
//    }
}
