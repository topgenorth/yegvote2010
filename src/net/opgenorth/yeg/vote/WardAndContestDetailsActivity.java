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
import net.opgenorth.yeg.vote.model.IFilterRawElectionResultRows;
import net.opgenorth.yeg.vote.model.RawElectionResultRow;
import net.opgenorth.yeg.vote.model.SetOfElectionResults;

import java.util.ArrayList;
import java.util.Collection;

public class WardAndContestDetailsActivity extends PollingYegVoteListActivity {
    private String wardName;
    private String contest;

    private IElectionResultMonitor service = null;
    private ArrayList<RawElectionResultRow> electionResults;
    private TextView infoTextView;
    private RawElectionResultRowAdapter adapter;

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
    private IElectionResultListener listener = new IElectionResultListener() {

        public void newSetOfElectionResults(SetOfElectionResults results) {
            Log.v(Constants.LOG_TAG, results.toString());

            IFilterRawElectionResultRows filter = new GetRawElectionResultRowForWardAndContest(wardName, contest);
            final Collection<RawElectionResultRow> filteredResult = filter.getResults(results);

            final String message = "Last Updated: " + results.getRequestDate().toString();

            runOnUiThread(new Runnable() {
                public void run() {
                    infoTextView.setText(message);
                    electionResults.clear();
                    electionResults.addAll(filteredResult);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        electionResults = new ArrayList<RawElectionResultRow>();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.warddetails);
        infoTextView = (TextView) findViewById(R.id.lastUpdated);

        getWardNameAndContest();

        adapter = new RawElectionResultRowAdapter();
        setListAdapter(adapter);
    }

    private void getWardNameAndContest() {
        Bundle bundle = getIntent().getExtras();

        wardName = bundle.getString("wardName");
        contest =bundle.getString("contest");
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
    protected void onResume() {
        super.onResume();
        bindElectionResultsService();
    }

    @Override
    protected void onDestroy() {
        unbindElectionResultsService();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        unbindElectionResultsService();
        super.onPause();
    }

    class RawElectionResultRowAdapter extends ArrayAdapter<RawElectionResultRow> {
        public RawElectionResultRowAdapter() {
            super(WardAndContestDetailsActivity.this, R.layout.warddetails, electionResults);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            WardAndContestResultWrapper wrapper;
            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.contest_row, parent, false);
                wrapper = new WardAndContestResultWrapper(row);
                row.setTag(wrapper);
            } else {
                wrapper = (WardAndContestResultWrapper) row.getTag();
            }

            wrapper.populateFrom(electionResults.get(position));

            return (row);
        }
    }
}
