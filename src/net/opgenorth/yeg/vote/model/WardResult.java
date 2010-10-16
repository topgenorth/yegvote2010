package net.opgenorth.yeg.vote.model;


public class WardResult {
    public WardResult(RawElectionResultRow row) {
        wardName = row.getWardName();
        candidateName = row.getCandidateName();
        votes = row.getVotes();
        acclaimed = row.getAcclaimed();
        contest = row.getContest();
    }

    public String contest;
    public int votes;
    public String wardName;
    public String candidateName;
    public boolean acclaimed;

    public String getKey() {
        return wardName + "_" + candidateName;
    }
}
