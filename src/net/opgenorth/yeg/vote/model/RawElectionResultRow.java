package net.opgenorth.yeg.vote.model;

import java.util.Date;

/**
 * Just a wrapper around the CSV string that is a row.
 */
public class RawElectionResultRow {

    public static final int ENTITYID_IDX = 0;
    public static final int CONTEST_IDX =1;
    public static final int WARDNAME_IDX =2;
    public static final int ACCLAIMED_IDX = 3;
    public static final int REPORTING_IDX = 4;
    public static final int OUTOF_IDX = 5;
    public static final int VOTESCAST_IDX =6;
    public static final int CANDIDATENAME_IDX = 7;
    public static final int VOTES_IDX = 8;
    public static final int PERCENT_IDX = 9;
    private String[] tokens;
    private String originaRow;
    private Date created;

    public RawElectionResultRow(String row, Date created) {
        this.originaRow = row;
        this.created = created;
        this.tokens = row.split(",");
    }

    public String getContest() {
        return tokens[CONTEST_IDX];
    }
    public String getWardName() {
        return tokens[WARDNAME_IDX];
    }

    public String getCandidateName() {
        return tokens[CANDIDATENAME_IDX];
    }

    public int getVotes() {
        return Integer.parseInt(tokens[VOTES_IDX]);
    }

    public String getPercent() {
        return tokens[PERCENT_IDX];
    }

    public boolean getAcclaimed() {
        return Boolean.parseBoolean(tokens[ACCLAIMED_IDX]);
    }
}
