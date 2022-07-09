package com.candle.fileexplorer.model.data;

import java.io.File;
import java.nio.file.FileSystemException;

public interface FileItem {

    /**
     * Moves the given file/folder to the target destination.
     *
     * @param targetPath The absolute path to the destination folder.
     */
    void moveTo(String targetPath) throws FileSystemException;

    /**
     * Copies the given file/folder to the target destination.
     *
     * @param targetPath The absolute path to the destination folder.
     */
    void copyTo(String targetPath) throws FileSystemException;

    /**
     * Creates a new file/folder at the path specified when the item was
     * first created.
     *
     * @return Whether the operation was successful.
     */
    boolean writeToDisk();

    /**
     * Renames the existing file/folder at the specified when the item was
     * first created.
     *
     * @param name The new name of the file/folder.
     * @return Whether the operation was successful.
     */
    boolean rename(String name);

    /**
     * Sends the file/folder at the path specified when the item was first
     * created to the recycle bin.
     */
    boolean sendToTrash();

    /**
     * Gets the name of the file item.
     */
    String getFileName();

    /**
     * Gets the absolute path for the file item.
     */
    String getItemDirectory();

    /**
     * Gets the type of file item, be it an actual file, a folder, or a drive.
     */
    FileType getFileType();

    /**
     * Checks to see if the given file should be hidden by default.
     */
    boolean getIsHiddenFile();

    /**
     * Gets the time at which this item was last modified.
     */
    long getLastModifiedTime();
}
