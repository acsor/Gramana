package org.develop.nil.gramana.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.develop.nil.gramana.R;

public class MainActivity extends Activity
    implements View.OnClickListener,
        TextView.OnEditorActionListener {

    private EditText mEditText;
    private Button mETButton;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.activity_main_et);
        mETButton = (Button) findViewById(R.id.activity_main_b);

        mEditText.setOnEditorActionListener(this);
        mETButton.setOnClickListener(this);
    }

    @Override
    /*
    TODO This method is called when the user finishes editing, not on each character modification.
    The latter is the event on which we want our callback function to be executed. Should change this.
     */
    public boolean onEditorAction (TextView v, int actionId, KeyEvent event) {
        if (mEditText.getText().length() == 0) {
            mETButton.setActivated(false);
            return true;
        }
        else if (mEditText.getText().length() != 0) {
            mETButton.setActivated(true);
            return true;
        }

        return false;
    }

    @Override
    public void onClick (View v) {
        if (v.getId() == mETButton.getId()) {
            launchPermutationsActivity(
                    "" + mEditText.getText().toString(),
                    PermutationsActivity.ATTR_WHITESPACE_IN_SEP,
                    PermutationsActivity.ATTR_DEFAULT_OUT_SEP
            );
        }
    }

    private void launchPermutationsActivity (String s, String inSep, char outSep) {
        final Intent i = new Intent(this, PermutationsActivity.class);

        i.putExtra(PermutationsActivity.PARAM_PERMUTATION_STRING, s);
        i.putExtra(PermutationsActivity.PARAM_IN_SEP, inSep);
        i.putExtra(PermutationsActivity.PARAM_OUT_SEP, outSep);

        startActivity(i);
    }

}
