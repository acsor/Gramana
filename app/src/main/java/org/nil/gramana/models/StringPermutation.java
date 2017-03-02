package org.nil.gramana.models;

import android.support.annotation.NonNull;

/**
 * Created by n0ne on 07/01/17.
 */
public class StringPermutation implements Permutation {

    private String permutation;

    public StringPermutation (String permutation) {
        this.permutation = permutation;
    }

    @Override
    public String toString () {
        return String.valueOf(permutation);
    }

    @Override
    public int compareTo (@NonNull Permutation another) {
        // TO-DO Handle null pointer exception cases in this method.
        if (another == null && permutation == null) {
            return 0;
        } else if (another instanceof StringPermutation) {
            return String.valueOf(permutation).compareTo (
                    String.valueOf(((StringPermutation) another).permutation)
            );
        } else {
            return String.valueOf(permutation).compareTo(String.valueOf(another));
        }
    }

}
