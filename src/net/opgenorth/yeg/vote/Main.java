package net.opgenorth.yeg.vote;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Main extends ListActivity {
    private IElectionResultMonitor service = null;
    private TextView textView;

    private ServiceConnection svcConn = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            service = (IElectionResultMonitor) binder;
            try {
                service.register(listener);
                Log.v(Constants.LOG_TAG, "Elections2010ResultsService connected.");
            } catch (Throwable t) {
                Log.e(Constants.LOG_TAG, "Exception in call to registerAccount()", t);
                earthShatteringKaboom(t);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            service.unregister(listener);
            service = null;
            Log.v(Constants.LOG_TAG, "Disconnected from the Elections2010ResultsService.");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textView = (TextView) findViewById(R.id.info);

        bindService(new Intent(this, Election2010ResultsService.class), svcConn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(svcConn);
        Log.v(Constants.LOG_TAG, "Shutting down " + Constants.LOG_TAG);
    }

    private void earthShatteringKaboom(Throwable t) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exception!")
                .setMessage(t.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    private IElectionResultListener listener = new IElectionResultListener() {
        public void newSetOfElectionResults(SetOfElectionResults results) {
            final String message = "Last Updated: " + results.getRequestDate().toString();
            Log.v(Constants.LOG_TAG, results.toString());
            runOnUiThread(new Runnable() {
                public void run() {
                    textView.setText(message);
                }
            });
        }
    };

    class WardResult {
        String wardName;
        String candidateName;
    }

    class WardResultWrapper {
        private View row;

        WardResultWrapper(View row) {
            this.row = row;
        }
    }

    class WardResultViewWrapper extends ArrayAdapter<WardResult> {
        WardResultViewWrapper() {
            super(Main.this, R.layout.row);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            WardResultWrapper wrapper = null;

            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();

                row = inflater.inflate(R.layout.row, parent, false);
                wrapper = new WardResultWrapper(row);
                row.setTag(wrapper);
            } else {
                wrapper = (WardResultWrapper) row.getTag();
            }

//			wrapper.populateFrom(timeline.get(position));

            return (row);
        }
    }
}
