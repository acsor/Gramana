package org.nil.gramana.test;

import org.nil.gramana.activity.PermutationsActivity;
import org.nil.gramana.tools.InputValidator;
import org.nil.gramana.tools.Scrambler;
import org.junit.Test;
import org.nil.gramana.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void testPermutations () {
        final String word = "Tri co lo re";
        final Set<String[]> permutations = Scrambler.permute(word, "\\s");

        System.out.format("Extracted %d permutation(s)\n", permutations.size());

        for (String[] s: permutations) {
            System.out.println(Utils.join(s, ""));
        }
    }

    @Test
    public void testDictionaryFeature () {
        final String permutationWord = "t o r t a";
        final String inSep = "\\s";
        final Set<String> permutations = Scrambler.permuteString(permutationWord, inSep, "");

        final String dictionaryFileName = "src/main/assets/dictionaries/Italian dictionary.txt";
        final org.nil.gramana.tools.Dictionary dictionary;

        try {
            dictionary = new org.nil.gramana.tools.Dictionary(new File(dictionaryFileName));
        } catch (FileNotFoundException e) {
            System.err.format("\"%s\" not found\n", dictionaryFileName);
            return;
        }

        permutations.retainAll(dictionary.getWords());

        System.out.format("Found %d matches in \"%s\"\n", permutations.size(),
                dictionaryFileName);

        if (!permutations.isEmpty()) {
            System.out.println(permutations);
        }
    }

    @Test
    public void testPermutationsActivityInputValidator () {
        final String syllables = "a-b-c-d-e-f-g-h-j-k-l-m";
        final InputValidator<String> v = new PermutationsActivity.InputStringValidator(null, "-");

        System.out.println(v.isInputValid(syllables));
    }

}