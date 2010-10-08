package net.opgenorth.yeg.vote;


class WardResult {
    WardResult(RawElectionResultRow row) {
        wardName = row.getWardName();
        candidateName = row.getCandidateName();
        votes = row.getVotes();
        acclaimed = row.getAcclaimed();

    }

    int votes;
    String wardName;
    String candidateName;
    boolean acclaimed;

    public String getKey() {
        return wardName + "_" + candidateName;
    }
}
