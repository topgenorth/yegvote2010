package net.opgenorth.yeg.vote.views;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.util.Log;
import net.opgenorth.yeg.vote.Constants;

public abstract class PollingYegVoteListActivity extends ListActivity {
    protected boolean isSvcConnBound = false;

    protected void earthShatteringKaboom(Throwable t) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exception!")
                .setMessage(t.toString())
                .setPositiveButton("OK", null)
                .show();
        Log.e(Constants.LOG_TAG, "Earth Shattering Kaboom: \n\r" + t.toString());
    }
}
