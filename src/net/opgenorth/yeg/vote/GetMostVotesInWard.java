package net.opgenorth.yeg.vote;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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

    Comparator<WardResult> comparator = new WardResultVotesComparator();

    public Collection<WardResult> getResults(SetOfElectionResults results) {
        Map<String, WardResult> wards = new HashMap<String, WardResult>();
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

        return wards.values();

    }
}
