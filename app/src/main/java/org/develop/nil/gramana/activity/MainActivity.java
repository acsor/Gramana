package org.develop.nil.gramana.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.develop.nil.gramana.R;
import org.develop.nil.gramana.model.InputValidator;

public class MainActivity extends Activity
    implements View.OnClickListener,
        TextWatcher {

    private EditText mEditText;
    private Button mETButton;
    private PermutationsActivity.InputStringValidator mETInputValidator;
    private boolean mLastTimeNotMe = true;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.activity_main_et);
        mETButton = (Button) findViewById(R.id.activity_main_b);
        mETInputValidator = new EditTextInputValidator(this, PermutationsActivity.ATTR_WHITESPACE_IN_SEP);

        mEditText.addTextChangedListener(this);
        mETButton.setOnClickListener(this);
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

    @Override
    public void beforeTextChanged (CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged (CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged (Editable s) {
        if (s.toString().matches("\\s*")) { //If this Editable instance is empty:
            mETButton.setEnabled(false);
            mLastTimeNotMe = true;
        } else if (!mETInputValidator.isInputValid(s.toString())) { //Else, if the input is not valid by the check from PermutationsActivity:
            mETButton.setEnabled(false);

            if (mLastTimeNotMe) {
                Toast.makeText(
                        this,
                        mETInputValidator.getErrorMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }

            mLastTimeNotMe = false;
        } else {
            mETButton.setEnabled(true);
            mLastTimeNotMe = true;
        }
    }

    public static class EditTextInputValidator extends PermutationsActivity.InputStringValidator {

        public EditTextInputValidator (Context c, String inputSep) {
            super(c, inputSep);
        }

    }

}
