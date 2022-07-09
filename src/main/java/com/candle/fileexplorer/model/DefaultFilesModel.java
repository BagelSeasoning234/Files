package com.candle.fileexplorer.model;

import com.candle.fileexplorer.model.data.ClipboardMode;
import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.helpers.DirectoryStructure;
import com.candle.fileexplorer.model.helpers.FileOperations;
import com.candle.fileexplorer.model.observer.DataListener;
import com.candle.fileexplorer.model.data.DefaultFileItem;
import com.candle.fileexplorer.model.data.FileType;

import java.nio.file.FileSystemException;
import java.util.*;
import java.util.List;

/**
 * The implementation of the files' data model.
 */
public class DefaultFilesModel implements FilesModel {
    //region Private Members

    /**
     * A list of objects to notify whenever changes are made in the model.
     */
    private final List<DataListener> listeners;

    /**
     * A two-dimensional array that represents the directory histories for
     * each tab.
     */
    private final ArrayList<ArrayList<String>> directoryHistories;

    /**
     * An array of strings representing the current file path for each tab.
     */
    private final ArrayList<String> currentDirectories;

    /**
     * An array of indices for the currently viewed directory in history for
     * each tab.
     */
    private final ArrayList<Integer> historyIndices;

    /**
     * The index of the currently viewed tab.
     */
    private int tabIndex;

    /**
     * Determines whether pasted items should be moved or duplicated.
     */
    private ClipboardMode clipboardMode;

    //endregion

    //region Constructors

    public DefaultFilesModel() {
        listeners = new ArrayList<>();
        historyIndices = new ArrayList<>();
        currentDirectories = new ArrayList<>();
        directoryHistories = new ArrayList<>();
        clipboardMode = ClipboardMode.Copy;
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
        return currentDirectories.get(tabIndex);
    }

    @Override
    public void setCurrentDirectory(String newDirectory) {
        if (Objects.equals(newDirectory, getCurrentDirectory()))
            return;

        String cleanPath = FileOperations.sanitizePath(newDirectory);
        if (FileOperations.determineType(cleanPath) != FileType.File) {
            currentDirectories.set(tabIndex, cleanPath);
            notifyDirectoryChange();
            addDirectoryToHistory();
        } else {
            FileOperations.openFileInDefaultApp(newDirectory);
        }
    }

    @Override
    public ClipboardMode getClipboardMode() {
        return clipboardMode;
    }

    @Override
    public void setClipboardMode(ClipboardMode mode) {
        this.clipboardMode = mode;
    }

    //endregion

    //region Public Methods

    @Override
    public void addListener(DataListener newListener) {
        listeners.add(newListener);
    }

    @Override
    public void addTab() {
        int tabLocationIndex = (currentDirectories.size() == 0) ? 0 :
                currentDirectories.size();
        String defaultLocation = System.getProperty("user.home");

        directoryHistories.add(tabLocationIndex, new ArrayList<>(10));
        directoryHistories.get(tabLocationIndex).add(defaultLocation);

        currentDirectories.add(defaultLocation);
        historyIndices.add(0);

        setTabIndex(tabLocationIndex);
    }

    @Override
    public void removeTab(int tabLocationIndex) {
        currentDirectories.remove(tabLocationIndex);
        directoryHistories.remove(tabLocationIndex);
        historyIndices.remove(tabLocationIndex);

        if (getTabIndex() == tabLocationIndex)
            setTabIndex(0);
        else {
            if (tabIndex > 0)
                setTabIndex(tabIndex - 1);
        }
    }

    @Override
    public void createItem(FileType type, String name) {
        FileItem item = new DefaultFileItem(type,
                getCurrentDirectory() + "/" + name);
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
            currentDirectories.set(tabIndex, getHistory().get(getHistoryIndex()));
            notifyDirectoryChange();
        }
    }

    @Override
    public void goBackwardInDirectoryHistory() {
        if (getHistoryIndex() != 0) {
            setHistoryIndex(getHistoryIndex() - 1);
            currentDirectories.set(tabIndex, getHistory().get(getHistoryIndex()));
            notifyDirectoryChange();
        }
    }

    @Override
    public void paste(String sourcePath) throws FileSystemException {
        switch (clipboardMode) {
            case Cut -> {
                FileItem cutItem = new DefaultFileItem(sourcePath);
                cutItem.moveTo(getCurrentDirectory());
                notifyDirectoryChange();
            }
            case Copy -> {
                FileItem copyItem = new DefaultFileItem(sourcePath);
                copyItem.copyTo(getCurrentDirectory());
                notifyDirectoryChange();
            }
        }
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
        int maxStoredDirectories = 10;

        // Is the index smaller than the number of stored dirs?
        if (getHistoryIndex() < getHistory().size() - 1) {
            // We'll have to overwrite future directories.
            setHistoryIndex(getHistoryIndex() + 1);
            getHistory().set(getHistoryIndex(), getCurrentDirectory());
            getHistory().subList(getHistoryIndex() + 1, getHistory().size()).clear();
            return;
        }

        // Is the number of stored dirs smaller than the max?
        if (getHistory().size() < maxStoredDirectories) {
            // We can just add a directory.
            setHistoryIndex(getHistoryIndex() + 1);
            getHistory().add(currentDirectories.get(tabIndex));
            return;
        }

        // If we're here, then both are at the max, so we'll overwrite the
        // earliest directory.
        Collections.rotate(getHistory(), -1);
        getHistory().set(getHistoryIndex(), currentDirectories.get(tabIndex));
    }

    /**
     * A helper method that returns the directory history for the currently
     * viewed tab.
     */
    private ArrayList<String> getHistory() {
        return directoryHistories.get(tabIndex);
    }

    /**
     * A helper method that returns the history index for the currently
     * viewed tab.
     */
    private int getHistoryIndex() {
        return historyIndices.get(tabIndex);
    }

    /**
     * A helper method that sets the history index for the currently viewed tab.
     */
    private void setHistoryIndex(int newValue) {
        historyIndices.set(tabIndex, newValue);
    }

    //endregion
}
