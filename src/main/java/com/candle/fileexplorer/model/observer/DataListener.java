package com.candle.fileexplorer.model.observer;

/**
 * A listener interface to be implemented by classes interested in changes to the data model.
 */
public interface DataListener
{
    /**
     * An event that fires whenever the current directory in the data model changes.
     */
    void currentDirectoryChanged();
}
