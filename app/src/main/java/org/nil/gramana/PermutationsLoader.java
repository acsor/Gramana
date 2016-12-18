package org.nil.gramana;

import android.content.AsyncTaskLoader;
import android.content.Context;
import org.nil.gramana.tools.Dictionary;
import org.nil.gramana.tools.Scrambler;
import org.nil.gramana.utils.DictionaryManager;
import org.nil.gramana.utils.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by n0ne on 17/12/16.
 */
public class PermutationsLoader extends AsyncTaskLoader<Collection<String[]>> {

    private String mPermutationString;
    private String mInSep;

    private DictionaryManager mDM;
    private Dictionary mDictionary;

    private Set<String[]> mPermutations;
    private SortedSet<String> mDictionaryWords;
    private SortedSet<String[]> mFilteredPermutations;

    public PermutationsLoader (Context context, String inputString, String inSep) {
        super(context.getApplicationContext());
        mPermutationString = inputString;
        mInSep = inSep;
        mDM = DictionaryManager.getInstance(context.getApplicationContext());
    }

    @Override
    public void deliverResult (Collection<String[]> data) {
        if (isReset()) {
            close();
        } else if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    public void onStartLoading () {
        if (mFilteredPermutations != null) {
            deliverResult(mFilteredPermutations);
        }

        if (mFilteredPermutations == null) {
            forceLoad();
        }
    }

    @Override
    public void onStopLoading () {
        //In this implementation of onStopLoading I prefer to stop the current load
        //and discard the acquired data
        cancelLoad();
    }

    @Override
    public void onCanceled (Collection<String[]> data) {
        super.onCanceled(data);
        close();
    }

    @Override
    public void onReset () {
        onStopLoading();
        close();
    }

    @Override
    public Collection<String[]> loadInBackground () {
        mFilteredPermutations = new TreeSet<>(Scrambler.stringArrayComparator);

        mPermutations = Scrambler.permute(mPermutationString, mInSep);
        mFilteredPermutations.addAll(mPermutations);

        if (mDM.getSelectedDictionaryFileName() != null) {
            try {
                mDictionary = mDM.openSelectedDictionary();
                mDictionaryWords = mDictionary.getWords();

                //Filtering mPermutations through dictionary
                for (String[] e: mPermutations) {
                    if (!mDictionaryWords.contains(Utils.join(e, "").toLowerCase())) {
                        //Attempting to modify the mPermutations attribute which is being iterated over
                        //would yield a state error. Hence, we need to make a copy of it.
                        //TO-DO Try to see if there is any better performing solution to this.
                        mFilteredPermutations.remove(e);
                    }
                }
            } catch (FileNotFoundException e) {
                //TO-DO Figure out a way to communicate this error state to the activity or to the user.
                //Directly invoking a Toast from here doesn't seem appropriate.
                // Toast.makeText(
                //         this,
                //         String.format(
                //                 "%s (%s)",
                //                 getResources().getString(R.string.error_dictionary_not_found),
                //                 mDM.getSelectedDictionaryFileName()
                //         ),
                //         Toast.LENGTH_LONG
                // ).show();
                return null;
            } catch (IOException e) {
                //TO-DO Figure out a way to communicate this error state to the activity or to the user.
                return null;
            }
        }

        return mFilteredPermutations;
    }

    public void close () {
        mDM = null;
        if (mDictionary != null) {
            mDictionary.close();
        }

        mPermutations = null;
        mDictionaryWords = null;
        mFilteredPermutations = null;
    }

}
