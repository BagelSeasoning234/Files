package com.candle.fileexplorer.model;

import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.data.FileType;
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
     * Gets the index of the currently viewed tab.
     */
    int getTabIndex();

    /**
     * Sets the index representing the currently viewed tab to the specified value.
     */
    void setTabIndex(int newIndex);

    /**
     * Tells the model to set up additional values in the array lists for the new tab.
     */
    void addTab(int tabLocationIndex);

    /**
     * Tells the model to discard the values in the array lists associated with the given tab.
     */
    void removeTab(int tabLocationIndex);

    /**
     * Creates a new file/folder at the current directory.
     * @param type Lets the model know to create a file or folder
     * @param name The name of the file with its extension (i.e. Image.png) or folder.
     */
    void createItem(FileType type, String name);

    /**
     * Returns a string with the current directory.
     */
    String getCurrentDirectory();

    /**
     * Sets the current directory at the given index to the given value.
     * @param path The new directory to go to.
     */
    void setCurrentDirectory(String path);

    /**
     * Gets a list of drive names on the computer.
     */
    ArrayList<FileItem> getDrives();

    /**
     * Sets the current directory to the location in history that was last selected before going back.
     * (Similar to going "forward" on a web browser).
     */
    void goForwardInDirectoryHistory();

    /**
     * Sets the current directory to the location in history that was last selected.
     * (Similar to going "backward" on a web browser).
     */
    void goBackwardInDirectoryHistory();
}
