package net.opgenorth.yeg.vote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SetOfElectionResults {
    private int pollResult;
    private String responseBody;
    private Date requestDate;

    public SetOfElectionResults(String responseBody, int pollResult) {
        this.pollResult = pollResult;
        this.responseBody = responseBody;
        this.requestDate = new Date();
    }

    public Date getRequestDate() {
        return requestDate;
    }


    public List<RawElectionResultRow> getRows() {
        String[] rows = responseBody.split("\\r\\n");
        List<RawElectionResultRow> values = new ArrayList<RawElectionResultRow>();
        // we're not interested in the first row, as that is the row that that contains the column names.
        for (int i = 1; i < rows.length; i++) {
            String row = rows[i];
            values.add(new RawElectionResultRow(row, requestDate));
        }
        return values;
    }

    public String getStatus() {
        if (pollResult == ErrorCodes.Polling.POLL_SUCCEEDED) {
            StringBuffer sb = new StringBuffer();
            sb.append("Found ");
            sb.append(getRows().size());
            sb.append(" rows at ");
            sb.append(requestDate);
            sb.append(".");
            return sb.toString();
        } else {
            return "No results.";
        }
    }
}
