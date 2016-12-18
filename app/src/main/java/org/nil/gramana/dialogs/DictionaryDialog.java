package org.nil.gramana.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import org.nil.gramana.R;
import org.nil.gramana.utils.DictionaryManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by n0ne on 19/11/16.
 */
public class DictionaryDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        final AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        final Resources r = getActivity().getResources();
        final DictionaryManager m = DictionaryManager.getInstance(getActivity().getApplicationContext());
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
        .setSingleChoiceItems(dictionaries.toArray(new String[dictionaries.size()]),
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
