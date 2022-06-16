package com.candle.fileexplorer.model;

import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.helpers.DirectoryStructure;
import com.candle.fileexplorer.model.observer.DataListener;
import com.candle.fileexplorer.model.data.DefaultFileItem;
import com.candle.fileexplorer.model.data.FileType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The implementation of the files' data model.
 */
public class DefaultFilesModel implements FilesModel {

    //region Private Members

    /**
     * A list of objects to notify whenever changes are made in the model.
     */
    private List<DataListener> listeners;

    /**
     * A string representing the current file path.
     */
    private String currentDirectory;

    /**
     * The "memory list" of the past 10 directory items.
     */
    private ArrayList<String> directoryHistory;

    /**
     * The index of the currently viewed directory in history.
     */
    private int historyIndex;

    /**
     * The maximum number of directories to store in memory.
     */
    private final int MAX_STORED_DIRECTORIES = 10;

    //endregion

    //region Constructor

    public DefaultFilesModel() {
        listeners = new ArrayList<>();
        directoryHistory = new ArrayList<>(10);
        historyIndex = -1;
        setCurrentDirectory(System.getProperty("user.home"));
    }

    //endregion

    //region Public Methods

    @Override
    public ArrayList<FileItem> getDrives() {
        return DirectoryStructure.getDrives();
    }

    @Override
    public String getCurrentDirectory() {
        return currentDirectory;
    }

    @Override
    public void setCurrentDirectory(String newDirectory) {
        if (Objects.equals(newDirectory, currentDirectory))
            return;

        FileItem testItem = new DefaultFileItem(newDirectory);
        if (testItem.getFileType() != FileType.File) {
            // We use the test item so that we'll get a clean user path back.
            this.currentDirectory = testItem.getItemDirectory();
            notifyDirectoryChange();
            addDirectoryToHistory();
        }
    }

    @Override
    public void addListener(DataListener newListener) {
        listeners.add(newListener);
    }

    public void goForwardInDirectoryHistory() {
        if (historyIndex < directoryHistory.size() - 1) {
            historyIndex++;
            currentDirectory = directoryHistory.get(historyIndex);
            notifyDirectoryChange();
        }
    }

    public void goBackwardInDirectoryHistory() {
        if (historyIndex != 0) {
            historyIndex--;
            currentDirectory = directoryHistory.get(historyIndex);
            notifyDirectoryChange();
        }
    }

    //endregion

    //region Private Methods

    /**
     * Notifies all listeners that the current directory has been updated.
     */
    private void notifyDirectoryChange() {
        for (DataListener listener : listeners)
            listener.currentDirectoryChanged();
    }

    /**
     * Makes sure that the directory history data is up-to-date.
     */
    private void addDirectoryToHistory() {
        if (historyIndex < MAX_STORED_DIRECTORIES - 1)
            historyIndex++;

        // At first, just add the item.
        if (historyIndex == 0) {
            directoryHistory.add(historyIndex, currentDirectory);
            return;
        }

        // Is the index smaller than the array?
        if (historyIndex <= directoryHistory.size() - 1) {      // Something's not right here.
            // We'll have to overwrite future directories.
            directoryHistory.set(historyIndex, currentDirectory);
            directoryHistory.subList(historyIndex, directoryHistory.size()).clear();
            return;
        }

        // Is the size smaller than the max?
        if (directoryHistory.size() < MAX_STORED_DIRECTORIES) {
            // Since it's less than the max, we can just add a directory.
            directoryHistory.add(historyIndex, currentDirectory);
            return;
        }

        // If we're here, then both are at the max, so we'll overwrite the earliest directory.
        Collections.rotate(directoryHistory, -1);
        directoryHistory.set(historyIndex, currentDirectory);
    }

    //endregion
}
