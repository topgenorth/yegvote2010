package net.opgenorth.yeg.vote;

import android.view.View;
import android.widget.TextView;

public class ContestResultWrapper {
    private View row;
    private TextView candidateName;
    private TextView percentReporting;
    private TextView votes;

    public ContestResultWrapper(View row) {
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

        getVotes().setText("Votes: " + resultRow.getVotes() + " // " + resultRow.getVotesCast() );
    }
}
