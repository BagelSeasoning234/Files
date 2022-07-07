package com.candle.fileexplorer.model;

import com.candle.fileexplorer.model.data.ClipboardMode;
import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.data.FileType;
import com.candle.fileexplorer.model.observer.DataListener;

import java.util.ArrayList;

/**
 * The interface for the files' data model.
 */
public interface FilesModel {
    //region Accessors/Mutators

    /**
     * Gets the index of the currently viewed tab.
     */
    int getTabIndex();

    /**
     * Sets the index representing the currently viewed tab to the specified
     * value.
     */
    void setTabIndex(int newIndex);

    /**
     * Gets a list of drive names on the computer.
     */
    ArrayList<FileItem> getDrives();

    /**
     * Returns a string with the current directory.
     */
    String getCurrentDirectory();

    /**
     * Sets the current directory at the given index to the given value.
     *
     * @param path The new directory to go to.
     */
    void setCurrentDirectory(String path);

    /**
     * Gets the current setting for data retrieved from the clipboard.
     */
    ClipboardMode getClipboardMode();

    /**
     * Updates the setting for data retrieved from the clipboard.
     */
    void setClipboardMode(ClipboardMode mode);

    //endregion

    //region Public Methods

    /**
     * Subscribes the specified object to be notified whenever changes are
     * made to the data model.
     */
    void addListener(DataListener newListener);

    /**
     * Tells the model to set up additional values in the array lists for the
     * new tab.
     */
    void addTab();

    /**
     * Tells the model to discard the values in the array lists associated
     * with the given tab.
     */
    void removeTab(int tabLocationIndex);

    /**
     * Creates a new file/folder at the current directory.
     *
     * @param type Lets the model know to create a file or folder
     * @param name The name of the file with its extension (i.e. Image.png)
     *             or folder.
     */
    void createItem(FileType type, String name);

    /**
     * Renames the file/folder at the given directory.
     *
     * @param path The absolute path to the file/folder.
     * @param name The new name for the file/folder.
     */
    void renameItem(String path, String name);

    /**
     * Deletes the item at the given file/folder path.
     */
    void trashItem(String path);

    /**
     * Sets the current directory to the location in history that was last
     * selected before going back.
     * (Similar to going "forward" on a web browser).
     */
    void goForwardInDirectoryHistory();

    /**
     * Sets the current directory to the location in history that was last
     * selected.
     * (Similar to going "backward" on a web browser).
     */
    void goBackwardInDirectoryHistory();

    /**
     * Pastes a given file/folder to the current directory based on the
     * current clipboard mode.
     *
     * @param sourcePath The original path of the file/folder.
     */
    void paste(String sourcePath);

    //endregion
}
