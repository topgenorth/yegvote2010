package net.opgenorth.yeg.vote;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;

public class WardDetailsActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warddetails);
    }
}
