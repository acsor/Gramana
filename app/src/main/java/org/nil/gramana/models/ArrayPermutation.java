package org.nil.gramana.models;

import android.support.annotation.NonNull;
import org.nil.gramana.utils.Utils;

/**
 * Created by n0ne on 07/01/17.
 */
public class ArrayPermutation implements Permutation {

    public static final String OUT_SEP_DEFAULT = "";
    public static final String OUT_SEP_EMPTY = "";

    private String[] tokens;
    private String outSeparator;

    public ArrayPermutation (String tokens[]) {
        this(tokens, OUT_SEP_DEFAULT);
    }

    public ArrayPermutation (String[] tokens, String outSeparator) {
        this.tokens = tokens;
        this.outSeparator = outSeparator;
    }

    public String[] getTokens () {
        return tokens;
    }

    public String getOutSeparator () {
        return outSeparator;
    }

    @Override
    public String toString () {
        return Utils.join(tokens, outSeparator);
    }

    @Override
    public int compareTo (@NonNull Permutation another) {
        if (another instanceof ArrayPermutation) {
            return Utils.join(tokens, OUT_SEP_EMPTY).compareTo (
                    Utils.join(((ArrayPermutation) another).tokens, OUT_SEP_EMPTY)
            );
        } else {
            return Utils.join(tokens, OUT_SEP_EMPTY).compareTo(another.toString());
        }
    }

}
