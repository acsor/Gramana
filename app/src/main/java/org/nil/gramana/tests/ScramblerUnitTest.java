package org.nil.gramana.tests;

import org.junit.Test;
import org.nil.gramana.models.Permutation;
import org.nil.gramana.tools.Scrambler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.SortedSet;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ScramblerUnitTest {

    @org.junit.Test
    public void testPermutations () {
        final String word = "Tri co lo re";
        final SortedSet<Permutation> permutations = Scrambler.permute(word, "\\s");

        System.out.format("Extracted %d permutation(s)\n", permutations.size());

        for (Permutation p: permutations) {
            System.out.println(p);
        }
    }

    @Test
    public void testFindInFile () {
        final String[] permutationsTokens = {
                "e s t r i n",
                "l a p s e",
                "a s p e r s",
                "l e a s t",
                "e n t e r s",
                "a r l e s",
                "e a r i n g s",
                "p e r i s",
                "a p e r s",
                "a l e r t s",
                "c a p e r s",
                "p a l e s t",
                "a n e s t r i",
                "a t e s",
                "c a r e t s"
        };
        final String dictionaryFileName = "src/main/assets/dictionaries/English dictionary.txt";
        final File dictionaryFile = new File(dictionaryFileName);
        SortedSet<Permutation> permutations;

        try {
            for (String permutation: permutationsTokens) {
                permutations = Scrambler.findInFileIgnoreCase(dictionaryFile, permutation.split("\\s"));

                System.out.format("Found %d results for \"%s\".\n", permutations.size(), permutation);
                System.out.println(permutations + "\n");
            }
        } catch (FileNotFoundException e) {
            System.out.format("File %s was not found.", dictionaryFileName);
        }
    }

}
