package org.develop.nil.gramana;

import org.develop.nil.gramana.model.Scrambler;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void testPermutations () {
        final String word = "h-o-u-s-e";
        final Collection<List<String>> permutations = Scrambler.permute(word, '-', ' ');

        System.out.format("Extracted %d permutations\n", permutations.size());

        for (List<String> i: permutations) {
            System.out.println(i.toString());
        }
    }

    @Test
    public void additionisCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

}