package org.develop.nil.gramana;

import org.develop.nil.gramana.activity.PermutationsActivity;
import org.develop.nil.gramana.model.InputValidator;
import org.develop.nil.gramana.model.Scrambler;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void testPermutations () {
        final String word = "Tri co lo re";
        final Set<String> permutations = Scrambler.permute(word, "\\s", "");

        System.out.format("Extracted %d permutation(s)\n", permutations.size());

        for (String s: permutations) {
            System.out.println(s);
        }
    }

    @Test
    public void testDictionaryFeature () {
        final String word = "a b a c o";
        String tempWord;
        final String dictionaryFileName = "src/main/assets/Italian dictionary.txt";

        final Set<String> permutations = Scrambler.permute(word, "\\s", "");
        final Set<String> dictionary = new TreeSet<>();
        Scanner dictionaryReader = null;

        try {
            dictionaryReader = new Scanner(new File(dictionaryFileName));

            while (dictionaryReader.hasNextLine()) {
                try {
                    //TO-DO Find out why the program seems to get stuck at this block
                    tempWord = dictionaryReader.next("^\\p{Alpha}+").toLowerCase();
                    dictionary.add(tempWord);
                    dictionaryReader.nextLine();
                } catch (NoSuchElementException e) {

                }
            }
        } catch (FileNotFoundException e) {
            System.err.format("\"%s\" not found\n", dictionaryFileName);
            return;
        } finally {
            if (dictionaryReader != null) {
                dictionaryReader.close();
            }
        }

        permutations.retainAll(dictionary);

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