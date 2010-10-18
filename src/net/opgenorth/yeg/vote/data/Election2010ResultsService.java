package net.opgenorth.yeg.vote.data;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import net.opgenorth.yeg.vote.Constants;
import net.opgenorth.yeg.vote.ErrorCodes;
import net.opgenorth.yeg.vote.views.IElectionResultMonitor;
import net.opgenorth.yeg.vote.model.SetOfElectionResults;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.concurrent.atomic.AtomicBoolean;

public class Election2010ResultsService extends Service {

    private static final String ELECTION_RESULTS_SAMPLE_URL = "http://data.edmonton.ca/DataBrowser/DownloadCsv?container=coe&entitySet=Election2010ResultsSamples&filter=NOFILTER";
    private static final String ELECTION_RESULTS_URL = "http://data.edmonton.ca/DataBrowser/DownloadCsv?container=coe&entitySet=Election2010Results&filter=NOFILTER";
    private AtomicBoolean active = new AtomicBoolean(true);
    private HttpClient client;
    private final Binder binder = new LocalBinder();
    private IElectionResultListener electionListener;


    @Override
    public IBinder onBind(Intent intent) {
        return (binder);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        client = new DefaultHttpClient();
        new Thread(threadBody).start();
    }

    @Override
    public void onDestroy() {
        active.set(false);
    }

    private Runnable threadBody = new Runnable() {
        public void run() {
            while (active.get()) {
                getElectionResults();
                SystemClock.sleep(Constants.DEFAULT_POLLING_INTERVAL);
            }
        }
    };

    private void getElectionResults() {
        String responseBody;
        SetOfElectionResults result;
        HttpGet getMethod = new HttpGet(ELECTION_RESULTS_URL);
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseBody = client.execute(getMethod, responseHandler);
            result = new SetOfElectionResults(responseBody, ErrorCodes.Polling.POLL_SUCCEEDED);
            Log.v(Constants.LOG_TAG, "Retrieved election response.");

        } catch (Throwable t) {
            Log.e(Constants.LOG_TAG, "Exception fetching election response.", t);
            result = new SetOfElectionResults("", ErrorCodes.Polling.POLL_FAILED);
        }
        electionListener.newSetOfElectionResults(result);
    }

    public class LocalBinder extends Binder implements IElectionResultMonitor {
        public void register(IElectionResultListener callback) {
            electionListener = callback;
        }

        public void unregister(IElectionResultListener callback) {
            electionListener = null;
        }
    }

}


