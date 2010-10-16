package net.opgenorth.yeg.vote;

import android.view.View;
import android.widget.TextView;
import net.opgenorth.yeg.vote.model.RawElectionResultRow;
import net.opgenorth.yeg.vote.model.WardResult;


class WardResultWrapper {
    private View row;
    private TextView candidateName;
    private TextView wardName;
    private TextView votes;
    private WardResult wardResult;

    WardResultWrapper(View row) {
        this.row = row;
    }

    public TextView getVotes() {
        if (votes == null) {
            votes = (TextView) row.findViewById(R.id.votesTextView);
        }
        return votes;
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

    public void populateFrom(RawElectionResultRow row) {
        populateFrom(new WardResult(row));
    }

    public WardResult getWardResult() {
        return wardResult;
    }

    public void populateFrom(WardResult w) {
        this.wardResult = w;
        getCandidateName().setText(w.candidateName);
        getWardName().setText(w.wardName + " / " + w.contest);
        getVotes().setText("Votes: " + w.votes);
    }

}
