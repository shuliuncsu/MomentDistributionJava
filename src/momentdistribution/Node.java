package momentdistribution;

import java.util.ArrayList;

/**
 *
 * @author Shu Liu
 */
public class Node {

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

    public boolean isIsolated() {
        return beams.isEmpty();
    }

    @Override
    public String toString() {
        String result = "Node # " + id + " - " + beams.size() + " beams - " + (isFixed ? "F" : "N") + "\n";
        for (Beam beam : beams) {
            result += "\t" + beam.toString() + "\n";
        }
        return result;
    }

    public double getUnbalancedMoment() {
        if (isFixed) {
            return 0;
        }

        double result = 0;
        for (Beam beam : beams) {
            result += beam.moment;
        }
        return result;
    }

    /**
     * Redistribute moment of a node. Return true if it is already balanced,
     * false otherwise.
     *
     * @param tolerance maximum tolerance
     * @return whether the node is already balanced
     */
    public boolean redistributeMoment(double tolerance) {
        double unbalanced = getUnbalancedMoment();
        if (isFixed || Math.abs(unbalanced) <= tolerance) {
            return true;
        } else {
            for (Beam beam : beams) {
                beam.moment -= unbalanced * beam.df;
                beam.otherEndBeam.moment -= unbalanced * beam.df * beam.cof;
            }
            return false;
        }
    }
}
