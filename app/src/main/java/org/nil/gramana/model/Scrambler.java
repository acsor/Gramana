package org.nil.gramana.model;

import org.apache.commons.collections4.CollectionUtils;
import org.nil.gramana.utils.Utils;

import java.util.*;

/**
 * Created by n0ne on 24/09/16.
 */
public class Scrambler {

    public static Set<String[]> permute (String s, String inSeparator) {
        final String[] tokens = s.split(inSeparator);
        final Comparator<String[]> comparator = new Comparator<String[]> () {

            @Override
            public int compare (String[] first, String[] second) {
                return Utils.join(first, "").compareTo(Utils.join(second, ""));
            }

        };
        final TreeSet<String[]> permutations = new TreeSet<>(comparator);

        for (List<String> p: CollectionUtils.<String>permutations(Arrays.asList(tokens))) {
            permutations.add(p.toArray(new String[p.size()]));
        }

        return permutations;
    }

}
