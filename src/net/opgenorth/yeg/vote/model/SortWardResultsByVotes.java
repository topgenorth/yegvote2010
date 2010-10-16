package net.opgenorth.yeg.vote.model;

import net.opgenorth.yeg.vote.model.WardResult;

import java.util.Comparator;

public class SortWardResultsByVotes implements Comparator<WardResult> {
    public int compare(WardResult o1, WardResult o2) {
        if ((o1 == null) && (o2 == null))
            return 0;
        if (o1 == null)
            return -1;
        if (o2 == null)
            return 1;

        if (o1.votes == o2.votes) {
            return 0;
        }
        if (o1.votes > o2.votes)
            return 1;


        return -1;
    }
}
