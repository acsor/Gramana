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

    public static class ViewHolder {

        private static char sOutSep;
        private TextView mName;

        public ViewHolder (View root, char outSep) {
            mName = (TextView) root.findViewById(R.id.adapter_permutation_name);
            sOutSep = outSep;
        }

        public void update (String data) {
            final ColorProvider c = new ColorProvider(mName.getContext(), mName.getCurrentTextColor());
            final Spannable spannable = new SpannableString(data);
            final String[] syllables = data.split("" + sOutSep);
            int currIndex = 0;

            //We apply a different text color to every syllable in our permutation string:
            for (int i = 0; i < syllables.length; i++) {
                spannable.setSpan (
                        /*
                        TODO Important: the direct use of {@code ColorProvider.getNextColor()} here is
                        only temporary, and should be changed soon by a hashmap.
                         */
                        new ForegroundColorSpan(c.getNextColor()),
                        currIndex,
                        currIndex + syllables[i].length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                /*
                The length of {@code sOutSep} will be added to {@code currIndex} even during the last iteration, where
                no corresponding {@code sOutSep} after the current (and last) syllable can be found. However, this should not
                be a problem, since {@code currIndex} won't be used after that iteration, so no code checking to add the
                correct amount to {@code currIndex} will be added.
                 */
                currIndex += syllables[i].length() + String.valueOf(sOutSep).length();
            }

            mName.setText(spannable);
        }

    }

}
