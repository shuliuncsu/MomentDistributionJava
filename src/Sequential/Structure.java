package Sequential;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author Shu Liu
 */
public class Structure {

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
                structure.connectNodes(input.nextInt(), input.nextDouble(), input.nextInt(), input.nextDouble(), input.nextDouble());
            }
            structure.normalize();
            System.out.println(" Done");
            return structure;
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
            return null;
        }
    }

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

    public void connectNodes(int nodeID1, double moment1, int nodeID2, double moment2, double stiffness) {

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

        End end1 = new End();
        End end2 = new End();

        node1.ends.add(end1);
        node2.ends.add(end2);

        end1.df = stiffness;
        end1.moment = moment1;
        end1.farNode = node2;
        end1.farEnd = end2;

        end2.df = stiffness;
        end2.moment = moment2;
        end2.farNode = node1;
        end2.farEnd = end1;
    }

    public void normalize() {
        for (Node node : nodeMap.values()) {
            node.normalize();
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

    public Collection<Node> getNodesSet() {
        return nodeMap.values();
    }
}
