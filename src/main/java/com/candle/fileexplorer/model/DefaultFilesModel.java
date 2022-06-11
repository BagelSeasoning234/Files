package com.candle.fileexplorer.model;

import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.data.DefaultFileItem;
import com.candle.fileexplorer.model.data.FileType;
import com.candle.fileexplorer.model.helpers.DirectoryStructure;
import com.candle.fileexplorer.model.observer.DataListener;

import java.util.ArrayList;
import java.util.List;

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

    //endregion

    //region Constructor

    public DefaultFilesModel() {
        listeners = new ArrayList<>();
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
        // Make sure we're not looking at a file.
        FileItem testItem = new DefaultFileItem(newDirectory);
        if (testItem.getFileType() != FileType.File) {
            // We use the test item so that we'll get a clean user path back.
            this.currentDirectory = testItem.getItemDirectory();
            notifyDirectoryChange();
        }
    }

    @Override
    public void addListener(DataListener newListener) {
        listeners.add(newListener);
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

    //endregion
}
