package org.nil.gramana;

import android.content.AsyncTaskLoader;
import android.content.Context;
import org.nil.gramana.tools.Scrambler;
import org.nil.gramana.utils.DictionaryManager;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by n0ne on 17/12/16.
 */
public class PermutationsLoader extends AsyncTaskLoader<Collection<String[]>> {

    private String mPermutationString;
    private String mInSep;
    private Set<String[]> mPermutations;

    private DictionaryManager mDM;

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
        if (mPermutations != null) {
            deliverResult(mPermutations);
        }

        if (mPermutations == null) {
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
        final SortedSet<String> dictionaryPermutations;

        if (mDM.getSelectedDictionaryFileName() != null) {
            try {
                // TO-DO See whether DictionaryManager.getSelectedDictionaryFile() returns a File with the correct path.
                dictionaryPermutations = Scrambler.findInFileIgnoreCase(
                        mDM.getSelectedDictionaryFile(),
                        mPermutationString.split(mInSep)
                );

                mPermutations = stringPermutationsToArrayPermutations(dictionaryPermutations, mPermutationString.split(mInSep));
            } catch (FileNotFoundException e) {
                //TO-DO Figure out a way to communicate this error to the activity or to the user.
                return null;
            }
        } else {
            mPermutations = Scrambler.permute(mPermutationString, mInSep);
        }

        return mPermutations;
    }

    private SortedSet<String[]> stringPermutationsToArrayPermutations (SortedSet<String> permutations, String[] tokens) {
        final SortedSet<String[]> result = new TreeSet<>(Scrambler.stringArrayComparator);

        for (String permutation: permutations) {
            result.add(new String[]{permutation});
        }

        return result;
    }

    public void close () {
        mDM = null;
        mPermutations = null;
    }

}
