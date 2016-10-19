package org.develop.nil.gramana.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.develop.nil.gramana.R;
import org.develop.nil.gramana.activity.PermutationsActivity;
import org.develop.nil.gramana.model.ColorProvider;

import java.util.*;

/**
 * Created by n0ne on 13/10/16.
 */
public class PermutationsAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mData;
    private char mOutSep;

    public PermutationsAdapter (Context context, char outSep) {
        this(context, new LinkedList<String>(), outSep);
    }

    public PermutationsAdapter (Context context, Collection<String> data, char outSep) {
        mContext = context;
        setData(data);
        mOutSep = outSep;
    }

    public void setData (Collection<String> data) {
        mData = new LinkedList<>(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount () {
        return mData.size();
    }

    @Override
    public Object getItem (int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        final ViewHolder h;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_permutation, parent, false);
            h = new ViewHolder(convertView, mOutSep);

            convertView.setTag(h);
        } else {
            h = (ViewHolder) convertView.getTag();
        }

        h.update((String) getItem(position));

        return convertView;
    }

    /**
     * This method should be invoked by the respective Activity
     * in order to free the static variables {@code ViewHolder.sSyllablesToColors}
     * and {@code ViewHolder.sColorP} when it has finished running.
     */
    public void close () {
        PermutationsAdapter.ViewHolder.sSyllablesToColors = null;
        PermutationsAdapter.ViewHolder.sColorP = null;
    }

    public static class ViewHolder {

        private static Map<String, Integer> sSyllablesToColors;
        private static ColorProvider sColorP;
        private char mOutSep;
        private TextView mName;

        public ViewHolder (View root, char outSep) {
            mName = (TextView) root.findViewById(R.id.adapter_permutation_name);
            mOutSep = outSep;

            if (sSyllablesToColors == null) {
                sSyllablesToColors = new Hashtable<String, Integer>();
            }

            if (sColorP == null) {
                sColorP = new ColorProvider(
                        mName.getContext(),
                        mName.getCurrentTextColor(),
                        PermutationsActivity.InputStringValidator.ATTR_MAX_SYLLABLES
                );
            }
        }

        public void update (String data) {
            final Spannable spannable = new SpannableString(data);
            final String[] syllables = data.split("" + mOutSep);
            int currIndex = 0;

            //For every syllable in {@code syllables} a different text color is applied
            for (int i = 0; i < syllables.length; i++) {

                //If no color is associated to the current syllable:
                if (!sSyllablesToColors.containsKey(syllables[i])) {
                    sSyllablesToColors.put(syllables[i], sColorP.getNextColor());
                }

                spannable.setSpan (
                        new ForegroundColorSpan(sSyllablesToColors.get(syllables[i])),
                        currIndex,
                        currIndex + syllables[i].length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                /*
                The length of {@code mOutSep} will be added to {@code currIndex} even during the last iteration, where
                there is no corresponding {@code mOutSep} after the current (and last) syllable. However, this should not
                be a problem, since {@code currIndex} won't be used after that iteration, so no code checking to add the
                correct amount to {@code currIndex} will be added.
                 */
                currIndex += syllables[i].length() + String.valueOf(mOutSep).length();
            }

            mName.setText(spannable);
        }

    }

}
