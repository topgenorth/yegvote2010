package net.opgenorth.yeg.vote.views;

import android.view.View;
import android.widget.TextView;
import net.opgenorth.yeg.vote.R;
import net.opgenorth.yeg.vote.model.RawElectionResultRow;

public class WardAndContestResultWrapper {
    private View row;
    private TextView candidateName;
    private TextView percentReporting;
    private TextView votes;

    public WardAndContestResultWrapper(View row) {
        this.row = row;
    }

        public TextView getCandidateName() {
        if (candidateName == null) {
            candidateName = (TextView) row.findViewById(R.id.candidateNameTextView);
        }
        return candidateName;
    }

    public TextView getPercentReport() {
        if (percentReporting == null) {
            percentReporting = (TextView) row.findViewById(R.id.percentReporting);
        }
        return percentReporting;
    }

    public TextView getVotes() {
        if (votes == null) {
            votes = (TextView) row.findViewById(R.id.votesTextView);
        }
        return votes;
    }

    public void populateFrom(RawElectionResultRow resultRow) {
        getCandidateName().setText(resultRow.getCandidateName());
        getPercentReport().setText(resultRow.getPercent() + "% of votes");
        getVotes().setText("Votes: " + resultRow.getVotes() );
    }
}
