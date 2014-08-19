package momentdistribution;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Shu Liu
 */
public class MomentDistribution {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filename = "structure1s";
        ArrayList<Node> structure = createStructureFromFile(filename);
        analyzeStructure_Linear(structure);
    }

    public static ArrayList<Node> createStructureFromFile(String filename) {
        try {
            Scanner input = new Scanner(new File(filename));
            ArrayList<Node> structure = new ArrayList();

            return structure;
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
            return null;
        }
    }

    public static void analyzeStructure_Linear(ArrayList<Node> structure) {

    }

    public static void connectNodes(Node node1, double df1, double cof1, double moment1,
            Node node2, double df2, double cof2, double moment2) {

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

    public static class Node {

        final int id;
        final boolean isFixed;
        ArrayList<Beam> beams;

        public Node(int id, boolean isFixed) {
            this.id = id;
            this.isFixed = isFixed;
            beams = new ArrayList();
        }

        public void normalize() {
            if (beams.size() > 0) {
                if (!isFixed) {
                    double totalDF = 0;
                    for (Beam beam : beams) {
                        totalDF += beam.df;
                    }
                    for (Beam beam : beams) {
                        beam.df /= totalDF;
                    }
                }
            } else {
                System.out.println("Warning: Node " + id + " is not connected");
            }
        }
    }

    public static class Beam {

        Node otherEndNode;
        Beam otherEndBeam;
        double df = 1;
        double cof = 0;
        double moment = 0;
    }

}
