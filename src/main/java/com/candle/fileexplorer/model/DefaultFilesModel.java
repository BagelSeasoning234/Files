package com.candle.fileexplorer.model;

import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.helpers.DirectoryStructure;
import com.candle.fileexplorer.model.helpers.FileOpener;
import com.candle.fileexplorer.model.observer.DataListener;
import com.candle.fileexplorer.model.data.DefaultFileItem;
import com.candle.fileexplorer.model.data.FileType;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * The implementation of the files' data model.
 */
public class DefaultFilesModel implements FilesModel {
    //region Private Members

    /**
     * The maximum number of directories to store in memory.
     */
    private final int MAX_STORED_DIRECTORIES = 10;

    /**
     * A list of objects to notify whenever changes are made in the model.
     */
    private final List<DataListener> listeners;

    /**
     * A hashmap that represents the directory histories for each tab.
     */
    private final HashMap<Integer, ArrayList<String>> directoryHistory;

    /**
     * An array of strings representing the current file path for each tab.
     */
    private final ArrayList<String> currentDirectory;

    /**
     * An array of indices for the currently viewed directory in history for each tab.
     */
    private final ArrayList<Integer> historyIndex;

    /**
     * The index of the currently viewed tab.
     */
    private int tabIndex;

    //endregion

    //region Constructors

    public DefaultFilesModel() {
        listeners = new ArrayList<>();
        historyIndex = new ArrayList<>();
        currentDirectory = new ArrayList<>();
        directoryHistory = new HashMap<>();
        addTab(0);
    }

    //endregion

    //region Accessors/Mutators

    @Override
    public int getTabIndex() {
        return tabIndex;
    }

    @Override
    public void setTabIndex(int newIndex) {
        this.tabIndex = newIndex;
        notifyDirectoryChange();
    }

    @Override
    public ArrayList<FileItem> getDrives() {
        return DirectoryStructure.getDrives();
    }

    @Override
    public String getCurrentDirectory() {
        return currentDirectory.get(tabIndex);
    }

    @Override
    public void setCurrentDirectory(String newDirectory) {
        if (Objects.equals(newDirectory, getCurrentDirectory()))
            return;

        FileItem testItem = new DefaultFileItem(newDirectory);
        if (testItem.getFileType() != FileType.File) {
            // We use the test item so that we'll get a clean user path back.
            currentDirectory.set(tabIndex, testItem.getItemDirectory());
            notifyDirectoryChange();
            addDirectoryToHistory();
        }
        else {
            FileOpener.openFileInDefaultApp(newDirectory);
        }
    }

    //endregion

    //region Public Methods

    @Override
    public void addListener(DataListener newListener) {
        listeners.add(newListener);
    }

    @Override
    public void addTab(int tabLocationIndex) {
        String defaultLocation = System.getProperty("user.home");

        directoryHistory.put(tabLocationIndex, new ArrayList<>(10));
        directoryHistory.get(tabLocationIndex).add(defaultLocation);

        currentDirectory.add(defaultLocation);
        historyIndex.add(0);
    }

    @Override
    public void removeTab(int tabLocationIndex) {
        currentDirectory.remove(tabLocationIndex);
        directoryHistory.remove(tabLocationIndex);
        historyIndex.remove(tabLocationIndex);
    }

    @Override
    public void createItem(FileType type, String name) {
        FileItem item = new DefaultFileItem(type, getCurrentDirectory() + "/" + name);
        if (item.writeToDisk())
            notifyDirectoryChange();
    }

    @Override
    public void renameItem(String path, String name) {
        FileItem item = new DefaultFileItem(path);
        if (item.rename(name))
            notifyDirectoryChange();
    }

    @Override
    public void trashItem(String path) {
        FileItem item = new DefaultFileItem(path);
        if (item.sendToTrash())
            notifyDirectoryChange();
    }

    @Override
    public void goForwardInDirectoryHistory() {
        if (getHistoryIndex() < getHistory().size() - 1) {
            setHistoryIndex(getHistoryIndex() + 1);
            currentDirectory.set(tabIndex, getHistory().get(getHistoryIndex()));
            notifyDirectoryChange();
        }
    }

    @Override
    public void goBackwardInDirectoryHistory() {
        if (getHistoryIndex() != 0) {
            setHistoryIndex(getHistoryIndex() - 1);
            currentDirectory.set(tabIndex, getHistory().get(getHistoryIndex()));
            notifyDirectoryChange();
        }
    }

    @Override
    public void forceUpdate() {
        notifyDirectoryChange();
    }

    //endregion

    //region Private Helper Methods

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
        // Is the index smaller than the number of stored dirs?
        if (getHistoryIndex() < getHistory().size() - 1) {
            // We'll have to overwrite future directories.
            setHistoryIndex(getHistoryIndex() + 1);
            getHistory().set(getHistoryIndex(), getCurrentDirectory());
            getHistory().subList(getHistoryIndex() + 1, getHistory().size()).clear();
            return;
        }

        // Is the number of stored dirs smaller than the max?
        if (getHistory().size() < MAX_STORED_DIRECTORIES) {
            // We can just add a directory.
            setHistoryIndex(getHistoryIndex() + 1);
            getHistory().add(currentDirectory.get(tabIndex));
            return;
        }

        // If we're here, then both are at the max, so we'll overwrite the earliest directory.
        Collections.rotate(getHistory(), -1);
        getHistory().set(getHistoryIndex(), currentDirectory.get(tabIndex));
    }

    /**
     * A helper method that returns the directory history for the currently viewed tab.
     */
    private ArrayList<String> getHistory() {
        return directoryHistory.get(tabIndex);
    }

    /**
     * A helper method that returns the history index for the currently viewed tab.
     */
    private int getHistoryIndex() {
        return historyIndex.get(tabIndex);
    }

    /**
     * A helper method that sets the history index for the currently viewed tab.
     */
    private void setHistoryIndex(int newValue) {
        historyIndex.set(tabIndex, newValue);
    }

    //endregion
}
