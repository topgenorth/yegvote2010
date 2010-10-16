package net.opgenorth.yeg.vote.model;

import net.opgenorth.yeg.vote.model.RawElectionResultRow;
import net.opgenorth.yeg.vote.model.SetOfElectionResults;

import java.util.Collection;

public interface IFilterRawElectionResultRows {
    public Collection<RawElectionResultRow> getResults(SetOfElectionResults setOfElectionResults);
}
