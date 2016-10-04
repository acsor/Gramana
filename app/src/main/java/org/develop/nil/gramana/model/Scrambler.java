package org.develop.nil.gramana.model;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.set.PredicatedSortedSet;

import java.util.*;

/**
 * Created by n0ne on 24/09/16.
 */
public class Scrambler {

    public static Set<String> permute (String s, String inSeparator, char outSeparator) {
        final List<String> tokens = new ArrayList<String>(Arrays.asList(s.split(String.valueOf(inSeparator))));
        final Set<String> permutations = new TreeSet<String>(); //We don't want elements to be repeated, so we use a Set implementation.

        for (List<String> p: CollectionUtils.permutations(tokens)) {
            //TO-DO Try to find a more elegant solution for populating this. Have a look at newer Java features for iterations.
            permutations.add(listToString(p, outSeparator));
        }

        return permutations;
    }

    private static String listToString (List<String> l, char outSeparator) {
        String result = "";
        final int listSize = l.size();

        for (int i = 0; i < listSize; i++) {
            result += l.get(i);

            if ((listSize - i) > 1) {   //If we haven't reached the last element yet:
                result += outSeparator;
            }
        }

        return result;
    }

}
