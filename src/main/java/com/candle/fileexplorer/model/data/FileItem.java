package com.candle.fileexplorer.model.data;

public interface FileItem {
    /**
     * Creates a new file/folder at the path specified when the item was first created.
     * @return Whether the operation was successful.
     */
    boolean writeToDisk();

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
     * Gets the size of the file.
     */
    long getFileSize();

    /**
     * Gets the time at which this item was last modified.
     */
    long getLastModifiedTime();
}
