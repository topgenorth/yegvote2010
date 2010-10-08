package net.opgenorth.yeg.vote;

import java.util.Comparator;

public class SortWardResultsByContests implements Comparator<WardResult> {
    public int compare(WardResult o1, WardResult o2) {
        int result = o1.wardName.compareTo(o2.wardName);
        if (result == 0)
            return o1.contest.compareTo(o2.contest);
        return result;
    }
}
