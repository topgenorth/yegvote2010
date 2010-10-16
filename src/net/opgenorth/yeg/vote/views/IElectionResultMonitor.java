package net.opgenorth.yeg.vote.views;

import net.opgenorth.yeg.vote.data.IElectionResultListener;

public interface IElectionResultMonitor {
    void register (IElectionResultListener callback);
    void unregister(IElectionResultListener callback);
}
