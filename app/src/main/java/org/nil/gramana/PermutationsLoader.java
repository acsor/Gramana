package org.nil.gramana;

import android.content.AsyncTaskLoader;
import android.content.Context;
import org.nil.gramana.models.Permutation;
import org.nil.gramana.utils.Scrambler;
import org.nil.gramana.utils.ApplicationDictionaryManager;

import java.io.IOException;
import java.util.Collection;
import java.util.SortedSet;

/**
 * Created by n0ne on 17/12/16.
 */
public class PermutationsLoader extends AsyncTaskLoader<Collection<Permutation>> {

    private String mPermutationString;
    private String mInSep;
    private SortedSet<Permutation> mPermutations;

    private ApplicationDictionaryManager mDM;

    public PermutationsLoader (Context context, String inputString, String inSep) {
        super(context.getApplicationContext());
        mPermutationString = inputString;
        mInSep = inSep;
        mDM = ApplicationDictionaryManager.getInstance(context.getApplicationContext());
    }

    @Override
    public void deliverResult (Collection<Permutation> data) {
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
    public void onCanceled (Collection<Permutation> data) {
        super.onCanceled(data);
        close();
    }

    @Override
    public void onReset () {
        onStopLoading();
        close();
    }

    @Override
    public Collection<Permutation> loadInBackground () {
        if (mDM.getSelectedDictionaryFileName() != null) {
            try {
                mPermutations = Scrambler.findInFileIgnoreCase(
                        mDM.selectedDictionaryInputStream(),
                        mPermutationString.split(mInSep)
                );
            } catch (IOException e) {
                //TO-DO Figure out a way to communicate this error to the activity or to the user.
                return null;
            }
        } else {
            mPermutations = Scrambler.permute(mPermutationString, mInSep);
        }

        return mPermutations;
    }

    public void close () {
        mDM = null;
        mPermutations = null;
    }

}
