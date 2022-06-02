package com.candle.fileexplorer.model;

import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.data.FileItemManager;
import com.candle.fileexplorer.model.data.FileType;
import com.candle.fileexplorer.model.observer.DataListener;

import java.util.ArrayList;
import java.util.List;

/**
 * The implementation of the files' data model.
 */
public class FilesModelManager implements FilesModel
{

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

    public FilesModelManager()
    {
        listeners = new ArrayList<>();
        setCurrentDirectory(System.getProperty("user.home"));
    }

    //endregion

    //region Public Methods

    @Override
    public String getCurrentDirectory()
    {
        return currentDirectory;
    }

    @Override
    public void setCurrentDirectory(String newDirectory)
    {
        // Make sure we're not looking at a file.
        FileItem testItem = new FileItemManager(newDirectory);
        if (testItem.getFileType() != FileType.File)
        {
            // We use the test item so that we'll get a clean user path back.
            this.currentDirectory = testItem.getItemDirectory();
            notifyDirectoryChange();
        }
    }

    @Override
    public void addListener(DataListener newListener)
    {
        listeners.add(newListener);
    }

    //endregion

    //region Private Methods

    /**
     * Notifies all listeners that the current directory has been updated.
     */
    private void notifyDirectoryChange()
    {
        for (DataListener listener : listeners)
            listener.currentDirectoryChanged();
    }

    //endregion
}
