package net.opgenorth.yeg.vote;

import net.opgenorth.yeg.vote.model.IFilterRawElectionResultRows;
import net.opgenorth.yeg.vote.model.RawElectionResultRow;
import net.opgenorth.yeg.vote.model.SetOfElectionResults;

import java.util.ArrayList;
import java.util.Collection;

public class GetRawElectionResultRowForWardAndContest implements IFilterRawElectionResultRows {
    private String wardName;
    private String contest;

    public GetRawElectionResultRowForWardAndContest(String wardName, String contest) {
        this.wardName = wardName;
        this.contest = contest;
    }

    public Collection<RawElectionResultRow> getResults(SetOfElectionResults setOfElectionResults) {
        ArrayList<RawElectionResultRow> filteredRows = new ArrayList<RawElectionResultRow>();
        for (RawElectionResultRow row : setOfElectionResults.getRows()) {
            if (wardName.equalsIgnoreCase(row.getWardName()) && contest.equalsIgnoreCase(row.getContest())) {
                filteredRows.add(row);
            }
        }

        return filteredRows;
    }
}
