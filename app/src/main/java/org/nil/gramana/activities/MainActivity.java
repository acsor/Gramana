package org.nil.gramana.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.nil.gramana.R;
import org.nil.gramana.dialogs.DictionaryDialog;
import org.nil.gramana.utils.ApplicationDictionaryManager;

import java.io.*;

public class MainActivity extends FragmentActivity
    implements View.OnClickListener,
        TextWatcher {

    private EditText mEditText;
    private DictionaryDialog mDDialog;
    private Button mETButton;
    private PermutationsActivity.InputStringValidator mETInputValidator;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.activity_main_et);
        mDDialog = new DictionaryDialog();
        mETButton = (Button) findViewById(R.id.activity_main_b);
        mETInputValidator = new EditTextInputValidator(this, PermutationsActivity.ATTR_IN_SEP_WHITESPACE);

        mEditText.addTextChangedListener(this);
        mETButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        final MenuInflater i = getMenuInflater();

        i.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_dictionaries:
                mDDialog.show(getSupportFragmentManager(), DictionaryDialog.class.getName());
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onClick (View v) {
        final String inputString;

        if (v.getId() == mETButton.getId()) {
            inputString = "" + mEditText.getText().toString();

            if (mETInputValidator.isInputValid(inputString)) {
                launchPermutationsActivity(
                        inputString,
                        PermutationsActivity.ATTR_IN_SEP_WHITESPACE,
                        PermutationsActivity.ATTR_OUT_SEP_DEFAULT
                );
            } else {
                Toast.makeText(
                        this,
                        mETInputValidator.getErrorMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }

	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		//TO-DO Try to find a way to handle this Activity's result within
		//DictionaryDialog, whose onActivityResult() method was never invoked until now.
		if (requestCode == DictionaryDialog.REQUEST_PICK_DICTIONARY) {
			if (resultCode == Activity.RESULT_OK) {
				onActivityResultDictionaryImport(intent);
			}
		}
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
        } else {
            mETButton.setEnabled(true);
        }
    }

	private void launchPermutationsActivity (String s, String inSep, String outSep) {
		final Intent i = new Intent(this, PermutationsActivity.class);

		i.putExtra(PermutationsActivity.PARAM_PERMUTATION_STRING, s);
		i.putExtra(PermutationsActivity.PARAM_IN_SEP, inSep);
		i.putExtra(PermutationsActivity.PARAM_OUT_SEP, outSep);

		startActivity(i);
	}

	private void onActivityResultDictionaryImport (final Intent intent) {
		final ApplicationDictionaryManager dictMg = ApplicationDictionaryManager.getInstance(this);
		InputStream dictInput = null;
		OutputStream dictOutput = null;
		String destName;

		try {
			destName = new File(intent.getData().getPath()).getName(); //We are supposing the URI points to a local file

			if (destName == null) { //If getLastPathSegment() method above returns null, i.e. the path is empty:
				Toast.makeText(
						this,
						R.string.error_local_files_only,
						Toast.LENGTH_LONG
				).show();
			} else {
				if (dictMg.dictionaryExists(destName)) { //If a dictionary with the same name already exists:
					//TO-DO Continue implementing here.
					Toast.makeText(
							this,
							R.string.error_dictionary_already_exists,
							Toast.LENGTH_LONG
					).show();
					return;
				}

				dictInput = getContentResolver().openInputStream(intent.getData());
				dictOutput = openFileOutput(destName, Context.MODE_PRIVATE);

				//If some errors occurred while importing/creating the dictionary:
				if (!ApplicationDictionaryManager.createDictionary(dictInput, dictOutput)) {
					Toast.makeText(
							this,
							R.string.error_creating_dictionary,
							Toast.LENGTH_LONG
					).show();
					//dictMg.deleteDictionary(destName); //Perhaps it is not the case to delete the dictionary
					//when an error occurs.
				}
			}
		} catch (FileNotFoundException e) {
			//TO-DO Handle this exception.
		} catch (UnsupportedEncodingException e) {

		} finally {
			try {
				if (dictInput != null) {
					dictInput.close();
				}
				if (dictOutput != null) {
					dictOutput.close();
				}
			} catch (IOException e) {

			}
		}
	}

    public static class EditTextInputValidator extends PermutationsActivity.InputStringValidator {

        public EditTextInputValidator (Context c, String inputSep) {
            super(c, inputSep);
        }

    }

}
