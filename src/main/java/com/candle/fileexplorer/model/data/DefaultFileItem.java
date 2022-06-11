package com.candle.fileexplorer.model.data;

import com.candle.fileexplorer.model.helpers.DirectoryStructure;

import java.io.File;
import java.util.ArrayList;

/**
 * Information about a directory item such as a drive, folder, or file.
 */
public class DefaultFileItem implements FileItem {
    //region Private Members

    /**
     * The type of item contained in this directory.
     */
    private FileType fileType;

    /**
     * The file object stored for internal use.
     */
    private File file;

    //endregion

    //region Constructor

    /**
     * Creates a new file item with the specified data values.
     *
     * @param fileType The type of file item being created, be it a drive, folder, or file.
     * @param path     The path to the file.
     */
    public DefaultFileItem(FileType fileType, String path) {
        file = new File(DirectoryStructure.sanitizePath(path));
        this.fileType = fileType;
    }

    /**
     * Creates a new file item and automatically determines if the given item returns a folder or a file.
     * NOTE: This function should not be used for drives.
     *
     * @param path The path to the file.
     */
    public DefaultFileItem(String path) {
        file = new File(DirectoryStructure.sanitizePath(path));
        if (file.isDirectory())
            fileType = FileType.Folder;
        else
            fileType = FileType.File;
    }

    //endregion

    //region Public Methods

    @Override
    public String getFileName() {
        // File.getName doesn't work for "/", so I have to manually return the name.
        if (file.equals(new File("/")))
            return "/";
        return file.getName();
    }

    @Override
    public String getItemDirectory() {
        return file.getAbsolutePath();
    }

    @Override
    public FileType getFileType() {
        return fileType;
    }

    @Override
    public boolean getIsHiddenFile() {
        return file.isHidden();
    }

    @Override
    public long getFileSize() {
        switch (fileType) {
            case File -> {
                return file.length();
            }
            case Folder, Drive -> {
                ArrayList<FileItem> contents = DirectoryStructure.getDirectoryContents(file.getAbsolutePath(), true);
                if (contents != null) {
                    long size = 0;
                    for (FileItem item : contents)
                        size += item.getFileSize();
                    return size;
                }
            }
        }
        return 0;
    }

    @Override
    public long getLastModifiedTime() {
        return file.lastModified();
    }

    /**
     * The objects should be equal if the file and type match.
     */
    @Override
    public boolean equals(Object obj) {
        DefaultFileItem otherItem = (DefaultFileItem) obj;
        if (otherItem != null) {
            boolean fileMatch = file.equals(otherItem.file);
            boolean typeMatch = fileType.equals(otherItem.fileType);
            return (fileMatch && typeMatch);
        }
        return false;
    }

    //endregion
}
