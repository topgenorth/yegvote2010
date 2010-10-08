package net.opgenorth.yeg.vote;

import java.util.Collection;

/**
 * Implemented by classes that will return a specific list of WardResults from
 * a collection of RawElectionResultRow.
 */
public interface IGetWardResults {
    Collection<WardResult> getResults(SetOfElectionResults results);
}
