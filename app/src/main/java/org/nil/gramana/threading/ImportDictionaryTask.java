package org.nil.gramana.threading;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;
import org.nil.gramana.R;
import org.nil.gramana.utils.ApplicationDictionaryManager;

import java.io.*;

/**
 * Created by n0ne on 03/03/17.
 */
public class ImportDictionaryTask extends AsyncTask<Uri, Void, Integer> {

	private static Integer ERR_NO_ERROR = 0;
	private static Integer ERR_LOCAL_FILES_ONLY = 1;
	private static Integer ERR_DICTIONARY_ALREADY_EXISTS = 2;
	private static Integer ERR_CREATE_DICTIONARY = 3;
	private static Integer ERR_DICTIONARY_NOT_FOUND = 4;

	private Context mContext;
	private ProgressDialog mDialog;

	public ImportDictionaryTask (Context context) {
		mContext = context;
	}

	@Override
	public void onPreExecute () {
		mDialog = ProgressDialog.show(
				mContext,
				mContext.getResources().getString(R.string.importing_dictionary),
				mContext.getResources().getString(R.string.importing_dictionary_desc),
				true,
				false
		);
	}

	@Override
	protected Integer doInBackground (Uri... params) {
		final Uri dictionaryUri = params[0];
		final ApplicationDictionaryManager dictMg = ApplicationDictionaryManager.getInstance(mContext);
		InputStream dictInput = null;
		OutputStream dictOutput = null;
		String destName;

		try {
			destName = new File(dictionaryUri.getPath()).getName(); //We are supposing the URI points to a local file

			if (destName == null) { //If getLastPathSegment() method above returns null, i.e. if the path is empty:
				return ERR_LOCAL_FILES_ONLY;
			} else {
				if (dictMg.dictionaryExists(destName)) { //If a dictionary with the same name already exists:
					//TO-DO Continue implementing here.
					return ERR_DICTIONARY_ALREADY_EXISTS;
				}

				dictInput = mContext.getContentResolver().openInputStream(dictionaryUri);
				dictOutput = mContext.openFileOutput(destName, Context.MODE_PRIVATE);

				//If some errors occurred while importing/creating the dictionary:
				if (!ApplicationDictionaryManager.createDictionary(dictInput, dictOutput)) {
					return ERR_CREATE_DICTIONARY;
				}
			}
		} catch (FileNotFoundException e) {
			return ERR_DICTIONARY_NOT_FOUND;
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

		return ERR_NO_ERROR;
	}

	@Override
	public void onPostExecute (Integer result) {
		mDialog.cancel();

		if (result.equals(ERR_NO_ERROR)) {

		} else if (result.equals(ERR_LOCAL_FILES_ONLY)) {
			Toast.makeText(
					mContext,
					R.string.error_local_files_only,
					Toast.LENGTH_LONG
			).show();
		} else if (result.equals(ERR_DICTIONARY_ALREADY_EXISTS)) {
			Toast.makeText(
					mContext,
					R.string.error_dictionary_already_exists,
					Toast.LENGTH_LONG
			).show();
		} else if (result.equals(ERR_CREATE_DICTIONARY)) {
			Toast.makeText(
					mContext,
					R.string.error_creating_dictionary,
					Toast.LENGTH_LONG
			).show();
		} else if (result.equals(ERR_DICTIONARY_NOT_FOUND)) {
			Toast.makeText(
					mContext,
					R.string.error_dictionary_not_found,
					Toast.LENGTH_LONG
			).show();
		}
	}

}
