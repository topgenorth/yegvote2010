package net.opgenorth.yeg.vote.data;

import net.opgenorth.yeg.vote.model.SetOfElectionResults;

public interface IElectionResultListener {
    void newSetOfElectionResults(SetOfElectionResults results);
}
