package org.nil.gramana.utils;

import java.util.List;

/**
 * Created by n0ne on 12/12/16.
 */
public class Utils {

    /**
     * Joins a String[] array's elements separating them by separator.
     * @param array String[] array to join.
     * @param separator separator to distance each element.
     * @return String with content equal to array's elements separated by separator or an empty String if array == null.
     */
    public static String join (String[] array, String separator) {
        String result = "";

        for (int i = 0; i < array.length; i++) {
            result += array[i];

            if (i < array.length - 1) {
                result += String.valueOf(separator);
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

    /**
     * The use of this method should be preferred to {@code join}, as {@code lengthAsJoinedString}
     * does not attempt to construct a String and executes thus more quickly.
     * @param array
     * @return length of {@code array} as it were a joined String
     */
    public static int lengthAsJoinedString (String[] array) {
        int result = 0;

        for (String element: array) {
            if (element != null) {
                result += element.length();
            }
        }

        return result;
    }

}
