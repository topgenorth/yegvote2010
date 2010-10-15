package net.opgenorth.yeg.vote;

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

import java.util.*;

public class Main extends PollingYegVoteListActivity {
    private IElectionResultMonitor service = null;
    private TextView infoTextView;
    private List<WardResult> wardResults;
    private WardResultViewAdapter adapter;

    protected ServiceConnection svcConn = new ServiceConnection() {
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
        wardResults = new ArrayList<WardResult>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        infoTextView = (TextView) findViewById(R.id.info);
        bindService(new Intent(this, Election2010ResultsService.class), svcConn, BIND_AUTO_CREATE);
        adapter = new WardResultViewAdapter();
        setListAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(svcConn);
        Log.v(Constants.LOG_TAG, "Shutting down " + Constants.LOG_TAG);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(Constants.LOG_TAG, "onPause:  stop listening to service");
        unbindService(svcConn);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(Constants.LOG_TAG, "onResume:  start listening to service");
        bindService(new Intent(this, Election2010ResultsService.class), svcConn, BIND_AUTO_CREATE);
    }

    private IElectionResultListener listener = new IElectionResultListener() {
        private IGetWardResults getWardResults = new GetMostVotesInWard();
        public void newSetOfElectionResults(SetOfElectionResults results) {

            Log.v(Constants.LOG_TAG, results.toString());

            Collection<WardResult> unsortedWardResults =  getWardResults.getResults(results);
            Collections.sort((List<WardResult>) unsortedWardResults, new SortWardResultsByContests());

            final Collection<WardResult> newWardResults = new ArrayList<WardResult>(unsortedWardResults);
            final String message = "Last Updated: " + results.getRequestDate().toString();

            runOnUiThread(new Runnable() {
                public void run() {
                    infoTextView.setText(message);
                    wardResults.clear();
                    wardResults.addAll(newWardResults);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };

    /**
     * Used to display the WardResult.
     */
    class WardResultViewAdapter extends ArrayAdapter<WardResult> {
        WardResultViewAdapter() {
            super(Main.this, R.layout.row, wardResults);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            WardResultWrapper wrapper;
            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.row, parent, false);
                wrapper = new WardResultWrapper(row);
                row.setTag(wrapper);
            } else {
                wrapper = (WardResultWrapper) row.getTag();
            }

            wrapper.populateFrom(wardResults.get(position));

            return (row);
        }
    }
}
