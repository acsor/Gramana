package org.develop.nil.gramana.activity;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import org.develop.nil.gramana.R;
import org.develop.nil.gramana.model.InputValidator;
import org.develop.nil.gramana.model.Scrambler;

/**
 * Created by n0ne on 02/10/16.
 */
public class PermutationsActivity extends ListActivity {

    public static final String ATTR_DEFAULT_IN_SEP = "-";
    public static final String ATTR_WHITESPACE_IN_SEP = "\\s";
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
        final Toast errorMessageToast;

        mPermutationString = i.getStringExtra(PARAM_PERMUTATION_STRING);
        mPermutationString = (mPermutationString == null) ? "": mPermutationString;

        //TO-DO Handle mInSep and mOutSep variables initialization
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

        setTitle(String.format("%s \"%s\"", getResources().getString(R.string.permutations_for), mPermutationString.replace(mInSep, String.valueOf(mOutSep))));

        //TO-DO Try to populate mAdapter in another thread
        mAdapter.addAll(Scrambler.permute(mPermutationString, mInSep, mOutSep));
    }

    @Override
    public void onResume () {
        super.onResume();

        setListAdapter(mAdapter);
    }

    public static class InputStringValidator implements InputValidator<String> {

        public static final int  ATTR_MAX_SYLLABLES = 7;

        private Context mContext;
        private String mInputSep;

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
