package com.candle.fileexplorer.model.data;

import java.nio.file.attribute.FileTime;

public interface FileItem
{
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
    FileTime getLastModifiedTime();
}
