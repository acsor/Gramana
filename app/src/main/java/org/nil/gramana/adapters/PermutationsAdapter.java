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
import org.nil.gramana.models.ArrayPermutation;
import org.nil.gramana.models.Permutation;
import org.nil.gramana.tools.ColorProvider;
import org.nil.gramana.utils.Utils;

import java.io.Closeable;
import java.util.*;

/**
 * Created by n0ne on 13/10/16.
 */
public class PermutationsAdapter extends BaseAdapter implements Closeable {

    private Context mContext;
    private List<Permutation> mData;

    public PermutationsAdapter (Context context) {
        this(context, new LinkedList<Permutation>());
    }

    public PermutationsAdapter (Context context, Collection<Permutation> data) {
        mContext = context;
        setData(data);
    }

    public void setData (Collection<Permutation> data) {
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
            h = new ViewHolder(convertView);

            convertView.setTag(h);
        } else {
            h = (ViewHolder) convertView.getTag();
        }

        h.update((Permutation) getItem(position));

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
        private TextView mName;

        ViewHolder (View root) {
            mName = (TextView) root.findViewById(R.id.adapter_permutation_name);

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

        public void update (Permutation data) {
            final Spannable spannable;

            if (data instanceof ArrayPermutation) {
                final ArrayPermutation castData = (ArrayPermutation) data;
                final String[] tokens = castData.getTokens();
                int currIndex = 0;

                spannable = new SpannableString(castData.toString());

                for (int i = 0; i < tokens.length; i++) {
                    if (!sSyllablesToColors.containsKey(tokens[i])) {
                        sSyllablesToColors.put(tokens[i], sColorP.getNextColor());
                    }

                    spannable.setSpan (
                            new ForegroundColorSpan(sSyllablesToColors.get(tokens[i])),
                            currIndex,
                            currIndex + tokens[i].length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );

                    currIndex += tokens[i].length() + castData.getOutSeparator().length();
                }
            } else {
                spannable = new SpannableString(data.toString());
            }

            mName.setText(spannable);
        }

    }

}
