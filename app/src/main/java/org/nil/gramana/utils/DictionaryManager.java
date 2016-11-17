package org.nil.gramana.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;

import java.io.IOException;

/**
 * Created by n0ne on 17/11/16.
 */
public class DictionaryManager {

    private Context mContext;
    private AssetManager mAssets;
    private String mSelectedDir;

    private static DictionaryManager sInstance;
    private static String mRoot = "dictionaries/";

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
        mSelectedDir = null;
    }

    public String selectDictionary (String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        try {
            //Check access to the requested dictionary
            mAssets.open(mRoot + fileName).close();
            return fileName;
        } catch (IOException e) {
            return null;
        }
    }

    public String getCurrent () {
        return mSelectedDir;
    }

    public String[] getDictionaries () {
        try {
            return mAssets.list(mRoot);
        } catch (IOException e) {
            return null;
        }
    }

    public void close () throws Exception {
        mAssets.close();
        sInstance = null;
    }

}
