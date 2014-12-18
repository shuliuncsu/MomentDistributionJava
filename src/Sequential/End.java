package Sequential;

/**
 *
 * @author Shu Liu
 */
public class End {

    Node farNode;
    End farEnd;
    double df = 1;
    //double cof = 0;
    double moment = 0;

    @Override
    public String toString() {
        return String.format("df: %.2f moment: %.1f FarEndNode: %d",
                df, moment, farNode.id);
    }

    public double getMoment() {
        return moment;
    }
}
