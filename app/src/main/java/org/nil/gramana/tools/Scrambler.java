package org.nil.gramana.tools;

import org.apache.commons.collections4.CollectionUtils;
import org.nil.gramana.utils.Utils;

import java.util.*;

/**
 * Created by n0ne on 24/09/16.
 */
public class Scrambler {

    public static Set<String[]> permute (String s, String inSeparator) {
        final String[] tokens = s.split(inSeparator);
        final TreeSet<String[]> permutations = new TreeSet<>(stringArrayComparator);

        for (List<String> p: CollectionUtils.<String>permutations(Arrays.asList(tokens))) {
            permutations.add(p.toArray(new String[p.size()]));
        }

        return permutations;
    }

    public static Set<String> permuteString (String s, String inSep, String outSep) {
        final String[] tokens = s.split(inSep);
        final TreeSet<String> permutations = new TreeSet<>();

        for (List<String> p: CollectionUtils.<String>permutations(Arrays.asList(tokens))) {
            permutations.add(Utils.join(p, outSep));
        }

        return permutations;
    }

    public static final Comparator<String[]> stringArrayComparator = new Comparator<String[]> () {

        @Override
        public int compare (String[] first, String[] second) {
            return Utils.join(first, "").compareTo(Utils.join(second, ""));
        }

    };

}
