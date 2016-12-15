package org.nil.gramana.utils;

import java.util.List;

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

    public static <T> String join (List<T> data, String sep) {
        String result = "";

        for (int i = 0; i < data.size(); i++) {
            result += data.get(i);

            if (i < data.size() - 1) {
                result += sep;
            }
        }

        return result;
    }

}
