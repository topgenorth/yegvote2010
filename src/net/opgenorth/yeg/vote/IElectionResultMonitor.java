package net.opgenorth.yeg.vote;

public interface IElectionResultMonitor {
    void register (IElectionResultListener callback);
    void unregister(IElectionResultListener callback);
}
