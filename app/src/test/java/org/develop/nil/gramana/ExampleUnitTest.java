package org.develop.nil.gramana;

import org.develop.nil.gramana.activity.PermutationsActivity;
import org.develop.nil.gramana.model.InputValidator;
import org.develop.nil.gramana.model.Scrambler;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void testPermutations () {
        final String word = "A-na-gram";
        final Set<String> permutations = Scrambler.permute(word, "-", '-');

        System.out.format("Extracted %d permutation(s)\n", permutations.size());

        for (String s: permutations) {
            System.out.println(s);
        }
    }

    @Test
    public void testValueOf() throws Exception {
        System.out.format("Printing %s\n", String.valueOf(null)); //It is going to throw an exception
    }

    @Test
    public void testPermutationsActivityInputValidator () {
        final String syllables = "a-b-c-d-e-f-g-h-j-k-l-m";
        final InputValidator<String> v = new PermutationsActivity.InputStringValidator(null, "-");

        System.out.println(v.isInputValid(syllables));
    }

}