package org.nil.gramana.activity;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.Toast;
import org.nil.gramana.PermutationsLoader;
import org.nil.gramana.R;
import org.nil.gramana.adapter.PermutationsAdapter;
import org.nil.gramana.tools.Dictionary;
import org.nil.gramana.tools.InputValidator;
import org.nil.gramana.tools.Scrambler;
import org.nil.gramana.utils.DictionaryManager;
import org.nil.gramana.utils.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by n0ne on 02/10/16.
 */
public class PermutationsActivity extends ListActivity
    implements LoaderManager.LoaderCallbacks<Collection<String[]>> {

    public static final String ATTR_IN_SEP_WHITESPACE = "\\s+?";
    public static final String ATTR_OUT_SEP_WHITESPACE = " ";
    public static final String ATTR_IN_SEP_DEFAULT = ATTR_IN_SEP_WHITESPACE;
    public static final String ATTR_OUT_SEP_DEFAULT = "";

    public static final String PARAM_PERMUTATION_STRING = "0";
    public static final String PARAM_IN_SEP = "1";
    public static final String PARAM_OUT_SEP = "2";

    public static final int LOADER_ID_PERMUTATIONS = 0;

    private String mPermutationString;
    private String mInSep;
    private String mOutSep;
    private PermutationsAdapter mAdapter;

    private DictionaryManager mDM;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permutations);

        final Intent i = getIntent();
        final ActionBar aBar = getActionBar();
        final InputValidator<String> v;

        mPermutationString = i.getStringExtra(PARAM_PERMUTATION_STRING).trim();
        mPermutationString = (mPermutationString == null) ? "": mPermutationString;

        mInSep = i.getStringExtra(PARAM_IN_SEP);
        mInSep = mInSep == null ? ATTR_IN_SEP_DEFAULT : mInSep;

        mDM = DictionaryManager.getInstance(this.getApplicationContext());

        //Checking input data
        v = new InputStringValidator(this, mInSep);

        if (!v.isInputValid(mPermutationString)) {
            Toast.makeText(
                    this,
                    v.getErrorMessage(),
                    Toast.LENGTH_LONG
            ).show();

            finish();

            return;
        }

        mOutSep = i.getStringExtra(PARAM_OUT_SEP);
        mOutSep = mOutSep == null ? ATTR_OUT_SEP_DEFAULT: mOutSep;

        getActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        mAdapter = new PermutationsAdapter(this, mOutSep);

        setTitle(String.format(
                "%s \"%s\"",
                getResources().getString(R.string.permutations_for_uc),
                mPermutationString.replace(mInSep, String.valueOf(mOutSep))
                )
        );

        getLoaderManager().initLoader(LOADER_ID_PERMUTATIONS, null, this);
    }

    @Override
    public void onStart () {
        super.onStart();

        final LayoutInflater inflater = LayoutInflater.from(
                this.getApplicationContext()
        );

        //TO-DO This doesn't seem to be displayed. Fix it.
        getListView().setEmptyView(inflater.inflate(R.layout.view_empty, getListView()));
    }

    @Override
    public Loader<Collection<String[]>> onCreateLoader (int id, Bundle args) {
        if (id == LOADER_ID_PERMUTATIONS) {
            getListView().setEnabled(false);
            return new PermutationsLoader(this, mPermutationString, mInSep);
        }
        return null;
    }

    @Override
    public void onLoadFinished (Loader<Collection<String[]>> loader, Collection<String[]> data) {
        mAdapter.setData(data);
        setListAdapter(mAdapter);
        getListView().setEnabled(true);

        setTitle(
                String.format(
                        Locale.getDefault(),
                        "%d %s \"%s\"",
                        mAdapter.getCount(),
                        getResources().getString(R.string.permutations_for_lc),
                        mPermutationString.replace(mInSep, String.valueOf(mOutSep))
                )
        );
    }

    @Override
    public void onLoaderReset (Loader<Collection<String[]>> loader) {
        mAdapter.setData(null);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();

        mAdapter.close();
    }

    public static class InputStringValidator implements InputValidator<String> {

        public static final int  ATTR_MAX_SYLLABLES = 7;

        private Context mContext;
        protected String mInputSep;

        public InputStringValidator (Context c, String inputSep) {
            mContext = c;
            mInputSep = inputSep;
        }

        @Override
        public boolean isInputValid (String data) {
            return data.split(mInputSep).length <= ATTR_MAX_SYLLABLES;
        }

        @Override
        public String getErrorMessage () {
            return String.format(
                    Locale.getDefault(),
                    "%s %d.",
                    mContext.getResources().getString(R.string.activity_permutations_input_validator),
                    ATTR_MAX_SYLLABLES
            );
        }

    }

}
