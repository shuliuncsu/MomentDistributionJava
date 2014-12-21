package ParallelAsy;

// Moment.java  -- moment distribution: sequential and concurrent versions
import java.text.DecimalFormat;
import java.util.ArrayList;

class Joint extends Thread {

    final public String name;
    private ArrayList<Member> members = new ArrayList<>();

    public Joint(String s) {
        name = s;
    }

    public void addMember(Member m) {
        members.add(m);
    }

    public boolean anyNonzeroDFs() {
        for (Member m : members) {
            if (m.e1.distribution_factor > 0) {
                return true;
            }
        }
        return false;
    }

    public double unbalancedMoment() {
        double moment = 0.0;
        for (Member m : members) {
            moment += m.e1.getMoment();
        }
        return moment;
    }

    public void unclamp(double moment) {
        for (Member m : members) {
            m.distribute(name, moment);
        }
//      Moment.printSummary();
    }

    public void run() {
        double moment;
        synchronized (this) {
            while (Math.abs(moment = unbalancedMoment()) < 0.0001) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        unclamp(moment);
        run();
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        for (Member m : members) {
            s.append(m.name + " " + m);
        }
        return s.toString();
    }
}

class End {

    final public Joint joint;
    final public double distribution_factor;
    final public double carryover_factor;
    private double moment;

    public End(Joint j, double d, double m, double c) {
        joint = j;
        distribution_factor = d;
        moment = m;
        carryover_factor = c;
    }

    public End(Joint j, double d, double m) {
        this(j, d, m, 0.5);
    }

    synchronized public void decrMoment(double dm) {
        moment -= dm;
    }

    public double getMoment() {
        return moment;
    }
}

class Member {

    final public String name;
    final public End e1, e2;

    final DecimalFormat df = new DecimalFormat("###,###.00");

    private Member(String s, End e1, End e2) {
        name = s;
        this.e1 = e1;
        this.e2 = e2;
    }

    public String toString() {
        return df.format(e1.getMoment()) + "   ";
    }

    public static void make(End e1, End e2) {
        String n1 = e1.joint.name;
        String n2 = e2.joint.name;
        e1.joint.addMember(new Member(n1 + n2, e1, e2));
        e2.joint.addMember(new Member(n2 + n1, e2, e1));
    }

    public void distribute(String s, double value) {
        double my_share = e1.distribution_factor * value;
        e1.decrMoment(my_share);
        e2.decrMoment(e1.carryover_factor * my_share);
        synchronized (e2.joint) {
            e2.joint.notify();
        }
    }
}

public class Moment {

    public static Joint[] problem1() {

        /*
         Example 16.5.1 from Analysis of Structures by H.H. West, p. 534

         AB -27.13   BA 406.54   BC -406.54   CB 00.00

         */
        Joint a = new Joint("A");
        Joint b = new Joint("B");
        Joint c = new Joint("C");

        Member.make(new End(a, 0.0, -172.8), new End(b, 0.5, 115.2));
        Member.make(new End(b, 0.5, -416.7), new End(c, 1.0, 416.7));

        Joint[] j = {a, b, c};

        return j;
    }

    public static Joint[] problem2() {

        /*
         Example 11-1 from Structural Analysis (2nd Ed.) by R.C. Hibbeler, p. 541

         AB 62.63   BA 125.26   BC -125.26   CB 281.58   CD -281.58   DC 234.21

         */
        Joint a = new Joint("A");
        Joint b = new Joint("B");
        Joint c = new Joint("C");
        Joint d = new Joint("D");

        Member.make(new End(a, 0.0, 0.0), new End(b, 0.5, 0.0));
        Member.make(new End(b, 0.5, -240.0), new End(c, 0.4, 240.0));
        Member.make(new End(c, 0.6, -250.0), new End(d, 0.0, 250.0));

        Joint j[] = {a, b, c, d};

        return j;
    }

    public static Joint[] problem3() {

        /*
         Example 10.3 from Structural Analysis by J.C. Smith, p. 421

         AB 461.41   BA -157.17   BC 157.17   CB 10.11   CD -167.75
         CE 157.64   DC -83.87   EC -149.95   EF 149.95   FE 00.00

         */
        Joint a = new Joint("A");
        Joint b = new Joint("B");
        Joint c = new Joint("C");
        Joint d = new Joint("D");
        Joint e = new Joint("E");
        Joint f = new Joint("F");

        Member.make(new End(a, 0.0, 360.0), new End(b, 0.5, -360.0));
        Member.make(new End(b, 0.5, 0.0), new End(c, 0.258, 0.0));
        Member.make(new End(c, 0.474, 0.0), new End(d, 0.0, 0.0));
        Member.make(new End(c, 0.268, 202.5), new End(e, 0.4, -202.5));
        Member.make(new End(e, 0.6, 0.0, 0.0), new End(f, 0.0, 0.0));

        Joint j[] = {a, b, c, d, e, f};

        return j;
    }

    public static void sequential(Joint j[]) {
        double moment;
        boolean done;

        System.out.println("Initial moments:");
        printSummary();
        System.out.println();
        do {
            done = true;
            for (int i = 0; i < j.length; i++) {
                if (j[i].anyNonzeroDFs()) {
                    if (Math.abs(moment = j[i].unbalancedMoment()) >= 0.0001) {
                        done = false;
                        j[i].unclamp(moment);
                    }
                }
            }
        } while (!done);
        System.out.println("\nFinal moments:");
        printSummary();
    }

    public static void concurrent(Joint j[]) {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        System.out.println("Initial moments:");
        printSummary();
        for (int i = 0; i < j.length; i++) {
            if (j[i].anyNonzeroDFs()) {
                System.out.println("Starting " + j[i].name);
                j[i].start();
            }
        }
        Thread.yield();
        System.out.println("Final moments:");
        printSummary();
        System.exit(0);
    }

    public static Joint[] joints;

    public static void printSummary() {
        for (int i = 0; i < joints.length; i++) {
            System.out.print(joints[i]);
        }
        System.out.println();
    }

    public static void main(String args[]) {

        Joint[][] problems = {problem1(), problem2(), problem3()};

        if (args.length != 2) {
            System.out.println("Usage: java Moment [sequential|concurrent] [1|2|3]");
            System.exit(1);
        }

        joints = problems[Integer.parseInt(args[1]) - 1];

        if (args[0].equals("sequential")) {
            sequential(joints);
        } else if (args[0].equals("concurrent")) {
            concurrent(joints);
        } else {
            System.out.println("First argument not 'sequential' or 'concurrent'");
        }
    }
}
