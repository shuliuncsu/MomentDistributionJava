package momentdistribution;

/**
 *
 * @author Shu Liu
 */
public class Beam {

    Node otherEndNode;
    Beam otherEndBeam;
    double df = 1;
    double cof = 0;
    double moment = 0;

    @Override
    public String toString() {
        return String.format("OtherEndNode: %d df: %.2f cof: %.2f moment: %.1f",
                otherEndNode.id, df, cof, moment);
    }
}
