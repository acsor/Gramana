package org.nil.gramana.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import org.nil.gramana.R;
import org.nil.gramana.utils.ApplicationDictionaryManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by n0ne on 19/11/16.
 */
public class DictionaryDialog extends DialogFragment {

	public static final int REQUEST_PICK_DICTIONARY = 1;

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        final AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        final Resources r = getActivity().getResources();
        final ApplicationDictionaryManager m = ApplicationDictionaryManager.getInstance(getActivity().getApplicationContext());
        final List<String> dictionaries = new LinkedList<>();

        dictionaries.add(r.getString(R.string.none));
        for (String file: m.getDictionaries()) {
            dictionaries.add(file);
        }

        b.setTitle(r.getString(R.string.dialog_dictionary_title))
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick (DialogInterface dialog, int which) {

            }

        })
		//Using setPositiveButton() for letting the dialog disappear once the button is clicked
		//(assuming it actually does it)
		.setPositiveButton(R.string.Import, new DialogInterface.OnClickListener() {

			@Override
			public void onClick (DialogInterface dialog, int which) {
				final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
				intent.addCategory(Intent.CATEGORY_OPENABLE);

				if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
					getActivity().startActivityForResult(intent, DictionaryDialog.REQUEST_PICK_DICTIONARY);
				} else {
					//TO-DO Add visual feedback for the user in case there is no component to handle the Intent request.
				}
			}

		})
        .setSingleChoiceItems(
        		dictionaries.toArray(new String[dictionaries.size()]),
                m.getSelectedDictionaryFileName() == null ? 0: dictionaries.indexOf(m.getSelectedDictionaryFileName()),
                new DialogInterface.OnClickListener() {

            @Override
            public void onClick (DialogInterface dialog, int which) {
                if (which == 0) {
                    m.selectDictionary(null);
                }
                else if (which >= (dictionaries.size() - m.getDictionaries().length)) {
                    m.selectDictionary(dictionaries.get(which));
                }
                dismiss();
            }

        });

        return b.create();
    }

}
