package org.nil.gramana.tools;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nil.gramana.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by n0ne on 24/09/16.
 */
public class Scrambler {

    private static Pattern PATTERN_SPECIFIED_LETTERS (final String letters) {
        final String pattern = "^[%s]+";

        return Pattern.compile(
                String.format(pattern, letters)
        );
    }

    public static SortedSet<String[]> permute (String s, String inSeparator) {
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

    /**
     * Find all permutations of {@code tokens} contained in {@code file}
     *
     * @param file InputStream to check permutations from
     * @param tokens list of string pieces whose permutations are to be found in {@code file}
     * @return a sorted set of permutations contained in {@code file}
     */
    public static SortedSet<String> findInFileIgnoreCase (final File file, String[] tokens) throws FileNotFoundException {
        final SortedSet<String> result = new TreeSet<>();

        final Scanner reader = new Scanner(file, "UTF-8");
        String line;

        while (reader.hasNextLine()) {
            line = reader.nextLine();

            if (isPermutationOfIgnoreCase(line, tokens)) {
                result.add(line);
            }
        }

        if (reader != null) {
            reader.close();
        }

        return result;
    }

    private static boolean isPermutationOfIgnoreCase (String supposedPermutation, final String tokens[]) {
        if (Utils.lengthAsJoinedString(tokens) != supposedPermutation.length()) {
            return false;
        }

        for (String token: tokens) {
            /*
                A bug during a test was found within this loop. It was apparently fixed, but the correctness of this algorithm
                should nevertheless be proved.
                TO-DO Prove {@code isPermutationOfIgnoreCase} executes correctly.
            */
            if (StringUtils.containsIgnoreCase(supposedPermutation, token)) {
                supposedPermutation = supposedPermutation.replaceFirst(token, "");
            } else {
                return false;
            }
        }

        return supposedPermutation.isEmpty();
    }

    public static final Comparator<String[]> stringArrayComparator = new Comparator<String[]> () {

        @Override
        public int compare (String[] first, String[] second) {
            return Utils.join(first, "").compareTo(Utils.join(second, ""));
        }

    };

}
