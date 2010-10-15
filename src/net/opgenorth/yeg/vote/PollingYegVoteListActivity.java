package net.opgenorth.yeg.vote;

import android.app.AlertDialog;
import android.app.ListActivity;

/**
 * Created by IntelliJ IDEA.
 * User: CANL-TOMO2
 * Date: 15-Oct-2010
 * Time: 2:37:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class PollingYegVoteListActivity extends ListActivity {
    protected void earthShatteringKaboom(Throwable t) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exception!")
                .setMessage(t.toString())
                .setPositiveButton("OK", null)
                .show();
    }
}
