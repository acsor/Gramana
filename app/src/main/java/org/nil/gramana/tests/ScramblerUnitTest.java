package org.nil.gramana.tests;

import org.junit.Test;
import org.nil.gramana.models.Permutation;
import org.nil.gramana.tools.Scrambler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.SortedSet;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ScramblerUnitTest {

    @org.junit.Test
    public void testPermutations () {
        final String word = "l e a s t";
        final SortedSet<Permutation> permutations = Scrambler.permute(word, "\\s");

        System.out.format("Extracted %d permutation(s)\n", permutations.size());

        for (Permutation p: permutations) {
            System.out.println(p);
        }
    }

    @Test
    public void testFindInFile () {
        final String[] permutationsTokens = {
                "E S T R I N",
                "L A P S E",
                "A S P E R S",
                "L E A S T",
                "E N T E R S",
                "A R L E S",
                "E A R I N G s",
                "P E R I S",
                "A P E R S",
                "A L E R T S",
                "C A P E R S",
                "P A L E S T",
                "A N E S T R i",
                "A T E S",
                "C A R E T S"
        };
        final String dictionaryFileName = "src/main/assets/dictionaries/English dictionary.txt";
        FileInputStream dictionaryIS = null;
        SortedSet<Permutation> permutations;

        try {
            for (String permutation: permutationsTokens) {
                dictionaryIS = new FileInputStream(dictionaryFileName);

                permutations = Scrambler.findInFileIgnoreCase(dictionaryIS, permutation.split("\\s"));

                System.out.format("Found %d results for \"%s\".\n", permutations.size(), permutation);
                System.out.println(permutations + "\n");

                dictionaryIS.close();
            }
        } catch (FileNotFoundException e) {
            System.out.format("File %s was not found.", dictionaryFileName);
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (dictionaryIS != null) {
                try {
                    dictionaryIS.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }

}
