package org.develop.nil.gramana.model;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by n0ne on 24/09/16.
 */
public class Scrambler {

    public static List<List<String>> permute (String s, char inSeparator, char outSeparator) {
        final ArrayList<String> tokens = new ArrayList<>(Arrays.asList(s.split(String.valueOf(inSeparator))));
        final Collection<List<String>> permutations = CollectionUtils.permutations(tokens);

        return new ArrayList<List<String>>(permutations);
    }

}
