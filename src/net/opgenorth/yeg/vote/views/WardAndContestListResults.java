package net.opgenorth.yeg.vote.views;

import android.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.TextView;
import net.opgenorth.yeg.vote.*;
import net.opgenorth.yeg.vote.data.Election2010ResultsService;
import net.opgenorth.yeg.vote.data.IElectionResultListener;
import net.opgenorth.yeg.vote.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WardAndContestListResults extends PollingYegVoteListActivity {
    private IElectionResultMonitor service = null;
    private TextView infoTextView;
    private List<WardResult> wardResults;
    private WardResultViewAdapter adapter;
    private AlertDialog alertDialog;

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
        unbindElectionResultsService();
        Log.v(Constants.LOG_TAG, "Shutting down " + Constants.LOG_TAG);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindElectionResultsService();
        Log.v(Constants.LOG_TAG, "onPause:  stop listening to service");
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindElectionResultsService();
        Log.v(Constants.LOG_TAG, "onResume:  start listening to service");
    }

    private void unbindElectionResultsService() {
        if (isSvcConnBound)
            unbindService(svcConn);
        isSvcConnBound = false;
    }

    private void bindElectionResultsService() {
        bindService(new Intent(this, Election2010ResultsService.class), svcConn, BIND_AUTO_CREATE);
        isSvcConnBound = true;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        WardResultWrapper wrapper = (WardResultWrapper) v.getTag();
        Intent intentToSeeDetails = new Intent(WardAndContestListResults.this, WardAndContestDetailsActivity.class);
        intentToSeeDetails.putExtra("contest", wrapper.getWardResult().contest);
        intentToSeeDetails.putExtra("wardName", wrapper.getWardResult().wardName);

        startActivity(intentToSeeDetails);
    }

    private IElectionResultListener listener = new IElectionResultListener() {
        private IGetWardResults getWardResults = new GetMostVotesInWard();

        public void newSetOfElectionResults(SetOfElectionResults results) {

            Log.v(Constants.LOG_TAG, results.toString());

            Collection<WardResult> unsortedWardResults = getWardResults.getResults(results);
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

    class WardResultViewAdapter extends ArrayAdapter<WardResult> {
        WardResultViewAdapter() {
            super(WardAndContestListResults.this, R.layout.row, wardResults);
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
