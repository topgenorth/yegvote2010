package net.opgenorth.yeg.vote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class Main extends Activity {
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
        textView = (TextView) findViewById(R.id.text1);

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
            final String message = results.getStatus();
            Log.v(Constants.LOG_TAG, message );
            runOnUiThread(new Runnable() {
                public void run() {
                    textView.setText(message);
                }
            });
        }
    };
}
