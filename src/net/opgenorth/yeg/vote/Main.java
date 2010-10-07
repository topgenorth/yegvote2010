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
import android.widget.ListView;
import android.widget.TextView;

import java.util.*;

public class Main extends ListActivity {
    private IElectionResultMonitor service = null;
    private TextView infoTextView;
    private List<WardResult> wardResults;
    private WardResultViewAdapter adapter;

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

    private void earthShatteringKaboom(Throwable t) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exception!")
                .setMessage(t.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    private IElectionResultListener listener = new IElectionResultListener() {
        public void newSetOfElectionResults(SetOfElectionResults results) {
            Map<String, RawElectionResultRow> uniqueWards = new HashMap<String, RawElectionResultRow>();
            for (RawElectionResultRow row : results.getRows()) {
                if (!uniqueWards.containsKey(row.getWardName()) ) {
                    uniqueWards.put(row.getWardName(), row);
                }
            }
            Collection<RawElectionResultRow> col = uniqueWards.values() ;
            final RawElectionResultRow[] rows = new RawElectionResultRow[col.size()];
            col.toArray(rows);
            final WardResult[] wr = new WardResult[col.size() ];
            for (int i = 0; i < rows.length; i++) {
                RawElectionResultRow rerr = rows[i];
                wr[i] = new WardResult(rerr);
            }

            Log.v(Constants.LOG_TAG, results.toString());


            final String message = "Last Updated: " + results.getRequestDate().toString();

            runOnUiThread(new Runnable() {
                public void run() {
                    infoTextView.setText(message);
                    wardResults.clear();
                    for (int i = 0; i < rows.length; i++) {
                        wardResults.add(wr[i]);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };

    class WardResult {
        WardResult(RawElectionResultRow row) {
            wardName = row.getWardName();
            candidateName = row.getCandidateName();
        }

        String wardName;
        String candidateName;
    }

    class WardResultWrapper {
        private View row;
        private TextView candidateName;
        private TextView wardName;

        WardResultWrapper(View row) {
            this.row = row;
        }

        public TextView getCandidateName() {
            if (candidateName == null) {
                candidateName = (TextView) row.findViewById(R.id.candidateNameTextView);
            }
            return candidateName;
        }

        public TextView getWardName() {
            if (wardName == null) {
                wardName = (TextView) row.findViewById(R.id.wardTextView);
            }
            return wardName;
        }

        public void populateFrom(WardResult w) {
            getCandidateName().setText(w.candidateName);
            getWardName().setText(w.wardName);
        }

    }

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
