package net.opgenorth.yeg.vote.model;

import java.util.Comparator;

public class SortRawElectionResultRowByVotes implements Comparator<RawElectionResultRow> {
    public int compare(RawElectionResultRow o1, RawElectionResultRow o2) {
        if ((o1 == null) && (o2 == null))
            return 0;
        if (o1 == null)
            return -1;
        if (o2 == null)
            return 1;

        if (o1.getVotes() == o2.getVotes()) {
            return 0;
        }
        if (o1.getVotes() > o2.getVotes())
            return 1;


        return -1;
    }
}
