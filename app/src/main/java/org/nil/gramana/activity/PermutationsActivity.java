package org.nil.gramana.activity;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import org.nil.gramana.R;
import org.nil.gramana.adapter.PermutationsAdapter;
import org.nil.gramana.model.InputValidator;
import org.nil.gramana.model.Scrambler;

import java.util.Locale;

/**
 * Created by n0ne on 02/10/16.
 */
public class PermutationsActivity extends ListActivity {

    public static final String ATTR_IN_SEP_WHITESPACE = "\\s+?";
    public static final char ATTR_OUT_SEP_WHITESPACE = ' ';
    public static final String ATTR_IN_SEP_DEFAULT = ATTR_IN_SEP_WHITESPACE;
    public static final String ATTR_OUT_SEP_DEFAULT = "-";

    public static final String PARAM_PERMUTATION_STRING = "0";
    public static final String PARAM_IN_SEP = "1";
    public static final String PARAM_OUT_SEP = "2";

    private String mPermutationString;
    private String mInSep;
    private String mOutSep;
    private PermutationsAdapter mAdapter;

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

        mAdapter = new PermutationsAdapter(this, mOutSep);

        /*
        TO-DO This code wrapped by the if-statement has been causing problems when running
        on a 23-API-level device. Checking against a null value is just a temporary solution,
        and a better one should be found. I'm not going to lose a whole evening for it.
         */
        if (aBar != null) {
            aBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        }
        setTitle(String.format(
                "%s \"%s\"",
                getResources().getString(R.string.permutations_for_uc),
                mPermutationString.replace(mInSep, String.valueOf(mOutSep))
            )
        );

        //TO-DO Try to populate mAdapter in another thread
        mAdapter.setData(Scrambler.permute(mPermutationString, mInSep, mOutSep));
        if (mAdapter.getFilter() != null) {

        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();

        mAdapter.close();
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
                Locale.getDefault(),
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
            return String.format(
                    Locale.getDefault(),
                    "%s %d.",
                    mContext.getResources().getString(R.string.activity_permutations_input_validator),
                    ATTR_MAX_SYLLABLES
            );
        }

    }

}
