package org.nil.gramana.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;

/**
 * Created by n0ne on 17/11/16.
 */
public class DictionaryManager {

    private Context mContext;
    private AssetManager mAssets;
    private String mSelectedDic;

    private static DictionaryManager sInstance;
    private static String mRoot = "dictionaries";

    public static DictionaryManager getInstance (Context c) {
        if (sInstance == null) {
            sInstance = new DictionaryManager(c);
        }
        return sInstance;
    }

    private DictionaryManager () {
        throw new IllegalStateException("Cannot instantiate a DictionaryManager instance through default constructor");
    }

    private DictionaryManager (Context c) {
        mContext = c.getApplicationContext();
        mAssets = mContext.getResources().getAssets();
        mSelectedDic = null;
    }

    public String selectDictionary (String fileName) {
        return mSelectedDic = fileName;
    }

    public String getSelectedDictionary () {
        return mSelectedDic;
    }

    public String[] getDictionaries () {
        try {
            return mAssets.list(mRoot);
        } catch (IOException e) {
            return new String[0];
        }
    }

    public void close () throws Exception {
        mAssets.close();
        sInstance = null;
    }

}
