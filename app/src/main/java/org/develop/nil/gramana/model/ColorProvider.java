package org.develop.nil.gramana.model;

import android.content.Context;
import android.graphics.Color;
import org.develop.nil.gramana.R;

/**
 * Created by n0ne on 13/10/16.
 */
public class ColorProvider {

    private int mDefaultColor;
    private int[] mColorsList;
    private int mCurrColor;

    public ColorProvider (Context c, int defaultColor) {
        mDefaultColor = defaultColor;
        /*
        TODO This hard-coded assignment should be changed, also taking into consideration
        the variable in PermutationsActivity.<SomeNestedClass> which stores the maximum number of syllables
        */
        mColorsList = new int[]{
                Color.RED,
                Color.GREEN,
                Color.BLUE,
                Color.CYAN,
                Color.YELLOW,
                Color.WHITE,
                Color.MAGENTA,
        };
        mCurrColor = 0;
    }

    public int getNextColor () {
        if (mCurrColor < mColorsList.length) {
            mCurrColor++;
            return mColorsList[mCurrColor];
        }
        return mDefaultColor;
    }

}
