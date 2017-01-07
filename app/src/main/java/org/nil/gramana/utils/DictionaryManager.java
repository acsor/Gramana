package org.nil.gramana.utils;

import android.content.Context;
import android.content.res.AssetManager;
import org.nil.gramana.tools.Dictionary;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by n0ne on 17/11/16.
 */
public class DictionaryManager implements Closeable {

    private Context mContext;
    private AssetManager mAssets;
    private String mSelectedDict;

    private static DictionaryManager sInstance;
    private static String sRoot = "dictionaries";

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
        mSelectedDict = null;
    }

    public String selectDictionary (String fileName) {
        return mSelectedDict = fileName;
    }

    public String getSelectedDictionaryFileName () {
        return mSelectedDict;
    }

    public InputStream openSelectedDictionary () throws IOException {
        return mAssets.open(
                String.format("%s/%s", sRoot, mSelectedDict),
                AssetManager.ACCESS_BUFFER
        );
    }

    public String[] getDictionaries () {
        try {
            return mAssets.list(sRoot);
        } catch (IOException e) {
            return null;
        }
    }

    public void close () throws IOException {
        mAssets.close();
        sInstance = null;
    }

}
