package org.nil.gramana.adapters;

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
import org.nil.gramana.R;
import org.nil.gramana.activities.PermutationsActivity;
import org.nil.gramana.tools.ColorProvider;
import org.nil.gramana.utils.Utils;

import java.io.Closeable;
import java.util.*;

/**
 * Created by n0ne on 13/10/16.
 */
public class PermutationsAdapter extends BaseAdapter implements Closeable {

    private Context mContext;
    private List<String[]> mData;
    private String mOutSep;

    public PermutationsAdapter (Context context, String outSep) {
        this(context, new LinkedList<String[]>(), outSep);
    }

    public PermutationsAdapter (Context context, Collection<String[]> data, String outSep) {
        mContext = context;
        setData(data);
        mOutSep = outSep;
    }

    public void setData (Collection<String[]> data) {
        if (data == null) {
            mData = new LinkedList<>();
        } else {
            mData = new LinkedList<>(data);
        }
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_permutations, parent, false);
            h = new ViewHolder(convertView, mOutSep);

            convertView.setTag(h);
        } else {
            h = (ViewHolder) convertView.getTag();
        }

        h.update((String[]) getItem(position));

        return convertView;
    }

    /**
     * This method should be invoked by the corresponding Activity
     * in order to free the static variables {@code ViewHolder.sSyllablesToColors}
     * and {@code ViewHolder.sColorP} when finishing running.
     */
    @Override
    public void close () {
        PermutationsAdapter.ViewHolder.sSyllablesToColors = null;
        PermutationsAdapter.ViewHolder.sColorP = null;
    }

    private static class ViewHolder {

        private static Map<String, Integer> sSyllablesToColors;
        private static ColorProvider sColorP;
        private String mOutSep;
        private TextView mName;

        ViewHolder (View root, String outSep) {
            mName = (TextView) root.findViewById(R.id.adapter_permutation_name);
            mOutSep = outSep;

            if (sSyllablesToColors == null) {
                sSyllablesToColors = new Hashtable<>();
            }

            if (sColorP == null) {
                sColorP = new ColorProvider(
                        mName.getContext(),
                        mName.getCurrentTextColor(),
                        PermutationsActivity.InputStringValidator.ATTR_MAX_SYLLABLES
                );
            }
        }

        void update (String[] data) {
            final Spannable spannable = new SpannableString(Utils.join(data, mOutSep));
            int currIndex = 0;

            for (int i = 0; i < data.length; i++) {
                if (!sSyllablesToColors.containsKey(data[i])) {
                    sSyllablesToColors.put(data[i], sColorP.getNextColor());
                }

                spannable.setSpan (
                        new ForegroundColorSpan(sSyllablesToColors.get(data[i])),
                        currIndex,
                        currIndex + data[i].length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                currIndex += data[i].length() + mOutSep.length();
            }

            mName.setText(spannable);
        }

    }

}
