package org.develop.nil.gramana.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import org.develop.nil.gramana.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent i = new Intent(this, PermutationsActivity.class);
        i.putExtra(PermutationsActivity.PARAM_PERMUTATION_STRING, "A-na-GRAM-ma");
        i.putExtra(PermutationsActivity.PARAM_OUT_SEP, '-');

        startActivity(i);
    }

}
