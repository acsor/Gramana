package org.nil.gramana.tools;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nil.gramana.models.ArrayPermutation;
import org.nil.gramana.models.Permutation;
import org.nil.gramana.models.StringPermutation;
import org.nil.gramana.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
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

    public static SortedSet<Permutation> permute (String input, String inSep) {
        return permute(input, inSep, ArrayPermutation.OUT_SEP_EMPTY);
    }

    public static SortedSet<Permutation> permute (String input, String inSep, String outSep) {
        final String[] tokens = input.split(inSep);
        final SortedSet<Permutation> permutations = new TreeSet<>();

        for (List<String> permutation: CollectionUtils.<String>permutations(Arrays.asList(tokens))) {
            permutations.add(
                    new ArrayPermutation(
                            permutation.toArray(new String[permutation.size()]),
                            outSep
                    )
            );
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
    public static SortedSet<Permutation> findInFileIgnoreCase (final File file, String[] tokens) throws FileNotFoundException {
        final SortedSet<Permutation> result = new TreeSet<>();

        final Scanner reader = new Scanner(file, "UTF-8");
        String line;

        while (reader.hasNextLine()) {
            // reader.nextLine() doesn't return content filtered according to a certain Pattern!
            // TO-DO Filter the input read from file.
            line = reader.nextLine();

            if (isPermutationOfIgnoreCase(line, tokens)) {
                result.add(new StringPermutation(line));
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

}
