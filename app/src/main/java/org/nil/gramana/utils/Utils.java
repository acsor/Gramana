package org.nil.gramana.utils;

/**
 * Created by n0ne on 12/12/16.
 */
public class Utils {

    public static String join (String[] array, String separator) {
        String result = "";

        for (int i = 0; i < array.length; i++) {
            result += array[i];

            if (i < array.length - 1) {
                result += separator;
            }
        }

        return result;
    }

}
