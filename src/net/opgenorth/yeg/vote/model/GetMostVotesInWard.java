package net.opgenorth.yeg.vote.model;

import net.opgenorth.yeg.vote.model.*;

import java.util.*;

public class GetMostVotesInWard implements IGetWardResults {
    private class UniqueWardName extends WardResult implements IWardResultKey {
        UniqueWardName(RawElectionResultRow row) {
            super(row);
        }

        public WardResult getWardResult() {
            return (WardResult) this;
        }

        public String getKey() {
            return super.wardName;
        }
    }

    Comparator<WardResult> comparator = new SortWardResultsByVotes();

    public Collection<WardResult> getResults(SetOfElectionResults results) {
        Map<String, UniqueWardName> wards = new HashMap<String, UniqueWardName>();
        for (RawElectionResultRow row : results.getRows()) {
            UniqueWardName wr = new UniqueWardName(row);
            if (wards.containsKey(wr.getKey())) {
                WardResult existingWard = wards.get(wr.getKey());
                if (comparator.compare(wr, existingWard) > 0) {
                    wards.remove(wr.getKey());
                    wards.put(wr.getKey(), wr);
                }

            } else {
                wards.put(wr.getKey(), wr);
            }
        }


        WardResult[] wr = new WardResult[wards.size() ];

        wards.values().toArray(wr);
        return Arrays.asList(wr);
    }
}
