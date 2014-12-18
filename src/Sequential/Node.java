package Sequential;

import java.util.ArrayList;

/**
 *
 * @author Shu Liu
 */
public class Node implements Comparable {

    final int id;
    final boolean isFixed;
    ArrayList<End> ends;

    public Node(int id, boolean isFixed) {
        this.id = id;
        this.isFixed = isFixed;
        ends = new ArrayList();
    }

    public void normalize() {
        if (ends.size() > 0) {
            if (!isFixed) {
                double totalDF = 0;
                for (End end : ends) {
                    totalDF += end.df;
                }
                for (End end : ends) {
                    end.df /= totalDF;
                }
            }
        } else {
            System.out.println("Warning: Node " + id + " is not connected");
        }
    }

    public boolean isIsolated() {
        return ends.isEmpty();
    }

    @Override
    public String toString() {
        String result = "Node # " + id + " - " + ends.size() + " ends - " + (isFixed ? "F" : "N") + "\n";
        for (End end : ends) {
            result += "\t" + end.toString() + "\n";
        }
        return result;
    }

    public double getUnbalancedMoment() {
        if (isFixed) {
            return 0;
        }

        double result = 0;
        for (End end : ends) {
            result += end.moment;
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
        if (isFixed) {
            return true;
        } else {
            double unbalanced = getUnbalancedMoment();
            if (Math.abs(unbalanced) <= tolerance) {
                return true;
            } else {
                for (End end : ends) {
                    end.moment -= unbalanced * end.df;
                    end.farEnd.moment -= unbalanced * end.df * 0.5;
                }
                return false;
            }
        }
    }

    public ArrayList<End> getEnds() {
        return ends;
    }

    @Override
    public int compareTo(Object o) {
        Node node2 = (Node) o;
        return -Double.compare(Math.abs(getUnbalancedMoment()), Math.abs(node2.getUnbalancedMoment()));
    }

}
