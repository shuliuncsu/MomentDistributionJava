package parallel;

import SequentialAsy.StructureAsy;

/**
 *
 * @author Shu Liu
 */
public class Parallel {

    public static void singleRun(String filename) {
        StructureAsy structure = StructureAsy.createStructureFromFile(filename);
        analyzeStructure_Syn(structure);
    }

    public static void analyzeStructure_Syn(StructureAsy structure) {

    }
}
