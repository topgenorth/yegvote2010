package net.opgenorth.yeg.vote;

import android.app.AlertDialog;
import android.app.ListActivity;

public class PollingYegVoteListActivity extends ListActivity {
    protected void earthShatteringKaboom(Throwable t) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exception!")
                .setMessage(t.toString())
                .setPositiveButton("OK", null)
                .show();
    }
}
