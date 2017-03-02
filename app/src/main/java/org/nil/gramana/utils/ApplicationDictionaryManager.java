package org.nil.gramana.utils;

import android.content.Context;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by n0ne on 17/11/16.
 */
public class ApplicationDictionaryManager extends DictionaryManager implements Closeable {

    private Context mContext;
    private String mSelectedDict;

    private static ApplicationDictionaryManager sInstance;

    public static ApplicationDictionaryManager getInstance (Context c) {
        if (sInstance == null) {
            sInstance = new ApplicationDictionaryManager(c);
        }
        return sInstance;
    }

    private ApplicationDictionaryManager (Context c) {
        mContext = c.getApplicationContext();
        mSelectedDict = null;
    }

    public String selectDictionary (String fileName) {
    	//TO-DO Add a constraint to allow the fileName argument to take values
		//only present in getDictionaries().
        return mSelectedDict = fileName;
    }

    public String getSelectedDictionaryFileName () {
        return mSelectedDict;
    }

    public InputStream selectedDictionaryInputStream () throws IOException {
    	return mContext.openFileInput(mSelectedDict);
    }

    public boolean dictionaryExists (String dictionaryFileName) {
    	for (String d: getDictionaries()) {
    		if (d.equals(dictionaryFileName)) {
    			return true;
			}
		}

		return false;
	}

    public String[] getDictionaries () {
    	return mContext.fileList();
    }

    public void close () {
        sInstance = null;
    }

	public void deleteDictionary (final String dictionary) {
    	if (dictionary != null) {
			mContext.deleteFile(dictionary);
		}
	}

	public static void deleteDictionary (final File dictionary) {
    	throw new UnsupportedOperationException(
    			String.format(
    					"%s does not support this method",
						ApplicationDictionaryManager.class.getSimpleName()
				)
		);
	}

}
