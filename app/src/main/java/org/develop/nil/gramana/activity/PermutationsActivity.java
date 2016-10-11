package org.develop.nil.gramana.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import org.develop.nil.gramana.R;
import org.develop.nil.gramana.model.InputValidator;
import org.develop.nil.gramana.model.Scrambler;

/**
 * Created by n0ne on 02/10/16.
 */
public class PermutationsActivity extends ListActivity {

    public static final String ATTR_WHITESPACE_IN_SEP = "\\s+?";
    public static final String ATTR_DEFAULT_IN_SEP = ATTR_WHITESPACE_IN_SEP;
    public static final char ATTR_DEFAULT_OUT_SEP = '-';

    public static final String PARAM_PERMUTATION_STRING = "0";
    public static final String PARAM_IN_SEP = "1";
    public static final String PARAM_OUT_SEP = "2";

    private String mPermutationString;
    private String mInSep;
    private char mOutSep;
    private ArrayAdapter<String>    mAdapter;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permutations);

        final Intent i = getIntent();
        final InputValidator<String> v;

        mPermutationString = i.getStringExtra(PARAM_PERMUTATION_STRING);
        mPermutationString = (mPermutationString == null) ? "": mPermutationString;

        mInSep = i.getStringExtra(PARAM_IN_SEP);
        mInSep = mInSep == null ? ATTR_DEFAULT_IN_SEP: mInSep;

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

        mOutSep = i.getCharExtra(PARAM_OUT_SEP, ATTR_DEFAULT_OUT_SEP);

        mAdapter = new ArrayAdapter<String>(this, R.layout.adapter_permutation);

        getActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        setTitle(String.format("%s \"%s\"", getResources().getString(R.string.permutations_for_uc), mPermutationString.replace(mInSep, String.valueOf(mOutSep))));

        //TO-DO Try to populate mAdapter in another thread
        mAdapter.addAll(Scrambler.permute(mPermutationString, mInSep, mOutSep));
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
    public void onResume () {
        super.onResume();

        setListAdapter(mAdapter);
        //TODO Be careful about this statement. Perhaps it could be safer to insert it into a Loader callback method
        setTitle(String.format(
                "%d %s \"%s\"",
                mAdapter.getCount(),
                getResources().getString(R.string.permutations_for_lc),
                mPermutationString.replace(mInSep, String.valueOf(mOutSep)))
        );
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
            return String.format("%s %d\n",
                    mContext.getResources().getString(R.string.activity_permutations_input_validator),
                    ATTR_MAX_SYLLABLES
            );
        }

    }

}
