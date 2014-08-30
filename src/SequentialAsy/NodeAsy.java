package SequentialAsy;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Shu Liu
 */
public class NodeAsy {

    final int id;
    final boolean isFixed;
    ArrayList<BeamAsy> beams;

    public NodeAsy(int id, boolean isFixed) {
        this.id = id;
        this.isFixed = isFixed;
        beams = new ArrayList();
    }

    public void normalize() {
        if (beams.size() > 0) {
            if (!isFixed) {
                double totalDF = 0;
                for (BeamAsy beam : beams) {
                    totalDF += beam.df;
                }
                for (BeamAsy beam : beams) {
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
        for (BeamAsy beam : beams) {
            result += "\t" + beam.toString() + "\n";
        }
        return result;
    }

    public double getUnbalancedMoment() {
        if (isFixed) {
            for (BeamAsy beam : beams) {
                beam.moment += beam.newMoment;
                beam.newMoment = 0;
            }
            return 0;
        }

        Random rand = new Random();
        double result = 0;
        for (BeamAsy beam : beams) {
            if (rand.nextDouble() <= 0.5) {
                beam.moment += beam.newMoment;
                beam.newMoment = 0;
            }
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
        if (isFixed || (Math.abs(unbalanced) <= tolerance && isClear())) {
            return true;
        } else {
            for (BeamAsy beam : beams) {
                beam.moment -= unbalanced * beam.df;
                beam.otherEndBeam.newMoment -= unbalanced * beam.df * beam.cof;
            }
            return false;
        }
    }

    private boolean isClear() {
        for (BeamAsy beam : beams) {
            if (beam.newMoment > 0) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<BeamAsy> getBeams() {
        return beams;
    }
}
