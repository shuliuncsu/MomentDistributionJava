package momentdistribution;

import structure.Structure;

/**
 *
 * @author Shu Liu
 */
public class Parallel {

    public static void singleRun(String filename) {
        Structure structure = Structure.createStructureFromFile(filename);
        analyzeStructure_Syn(structure);
    }

    public static void analyzeStructure_Syn(Structure structure) {

    }
}
