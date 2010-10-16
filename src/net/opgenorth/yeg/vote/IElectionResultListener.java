package net.opgenorth.yeg.vote;

import net.opgenorth.yeg.vote.model.SetOfElectionResults;

public interface IElectionResultListener {
    void newSetOfElectionResults(SetOfElectionResults results);
}
