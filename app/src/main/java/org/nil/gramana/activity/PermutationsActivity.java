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
import org.nil.gramana.tools.Dictionary;
import org.nil.gramana.tools.InputValidator;
import org.nil.gramana.tools.Scrambler;
import org.nil.gramana.utils.DictionaryManager;
import org.nil.gramana.utils.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by n0ne on 02/10/16.
 */
public class PermutationsActivity extends ListActivity {

    public static final String ATTR_IN_SEP_WHITESPACE = "\\s+?";
    public static final String ATTR_OUT_SEP_WHITESPACE = " ";
    public static final String ATTR_IN_SEP_DEFAULT = ATTR_IN_SEP_WHITESPACE;
    public static final String ATTR_OUT_SEP_DEFAULT = "";

    public static final String PARAM_PERMUTATION_STRING = "0";
    public static final String PARAM_IN_SEP = "1";
    public static final String PARAM_OUT_SEP = "2";

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

        if (!v.isInputValid(mPermutationString)) {  //If input is not valid
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

        updateView();
    }

    /**
     * TO-DO Populate mAdapter in another thread. The UI thread is blocked for a few seconds when using the dictionary feature.
     */
    private void updateView () {
        Set<String[]> permutations;
        final SortedSet<String[]> adapterData = new TreeSet<>(Scrambler.stringArrayComparator);
        Dictionary dictionary = null;

        setTitle(String.format(
                "%s \"%s\"",
                getResources().getString(R.string.permutations_for_uc),
                mPermutationString.replace(mInSep, String.valueOf(mOutSep))
            )
        );

        permutations = Scrambler.permute(mPermutationString, mInSep);
        adapterData.addAll(permutations);

        if (mDM.getSelectedDictionaryFileName() != null) {
            try {
                dictionary = mDM.openSelectedDictionary();
                final SortedSet<String> words = dictionary.getWords();

                //Show "Opening <filename"
                Toast.makeText(
                        this,
                        String.format(
                                "%s %s",
                                getResources().getString(R.string.opening_uc),
                                mDM.getSelectedDictionaryFileName()
                        ),
                        Toast.LENGTH_LONG
                ).show();

                //Filtering adapterData through dictionary
                for (String[] e: permutations) {
                    if (!words.contains(Utils.join(e, "").toLowerCase())) {
                        adapterData.remove(e);
                    }
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(
                        this,
                        String.format(
                                "%s (%s)",
                                getResources().getString(R.string.error_dictionary_not_found),
                                mDM.getSelectedDictionaryFileName()
                        ),
                        Toast.LENGTH_LONG
                ).show();
            } catch (IOException e) {

            } finally {
                if (dictionary != null) {
                    dictionary.close();
                    dictionary = null;
                }
                permutations = null;
            }
        }

        mAdapter.setData(adapterData);
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
