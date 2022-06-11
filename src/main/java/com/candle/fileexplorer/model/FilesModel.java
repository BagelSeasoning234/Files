package com.candle.fileexplorer.model;

import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.observer.DataListener;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The interface for the files' data model.
 */
public interface FilesModel {
    /**
     * Subscribes the specified object to be notified whenever changes are made to the data model.
     */
    void addListener(DataListener newListener);

    /**
     * Returns a string with the current directory.
     */
    String getCurrentDirectory();

    /**
     * Sets the current directory to the given value.
     *
     * @param path The new directory to go to.
     * @throws IOException If the path could not be found.
     */
    void setCurrentDirectory(String path);

    /**
     * Gets a list of drive names on the computer.
     */
    ArrayList<FileItem> getDrives();
}
