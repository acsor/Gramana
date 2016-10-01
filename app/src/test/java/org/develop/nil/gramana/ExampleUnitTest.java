package org.develop.nil.gramana;

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
        final String word = "a-b-c";
        final Set<String> permutations = Scrambler.permute(word, "", Character.MIN_VALUE);

        System.out.format("Extracted %d permutation(s)\n", permutations.size());

        for (String s: permutations) {
            System.out.println(s);
        }
    }

    @Test
    public void additionisCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

}