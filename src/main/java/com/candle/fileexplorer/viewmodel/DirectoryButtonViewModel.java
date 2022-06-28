package com.candle.fileexplorer.viewmodel;

import com.candle.fileexplorer.model.FilesModel;
import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.helpers.DirectoryStructure;

/**
 * The view model for the directory button class.
 */
public class DirectoryButtonViewModel {
    //region Private Members

    /**
     * A reference to the data model for this application.
     */
    private final FilesModel dataModel;
    /**
     * A reference to the file item associated with this directory button view model.
     */
    private final FileItem fileItem;

    //endregion

    //region Constructor

    /**
     * Constructs a new instance of the directory button view model with a reference to the data model.
     */
    public DirectoryButtonViewModel(FilesModel dataModel, FileItem item) {
        this.dataModel = dataModel;
        this.fileItem = item;
    }

    //endregion

    //region Public Methods

    /**
     * Gets the directory name for this view model (i.e. Pictures, Desktop)
     */
    public String getDirectoryName() {
        String name = fileItem.getFileName();
        if (name.equals(DirectoryStructure.getHomeDirectoryName()))
            return "Home";
        else
            return fileItem.getFileName();
    }

    /**
     * Tells the data model to take the user to the specified directory.
     */
    public void goToDirectory() {
        dataModel.setCurrentDirectory(fileItem.getItemDirectory());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        DirectoryButtonViewModel otherObject;
        if (!(obj instanceof DirectoryButtonViewModel))
            return false;

        otherObject = (DirectoryButtonViewModel) obj;
        return (fileItem.equals(otherObject.fileItem) && dataModel.equals(otherObject.dataModel));
    }

    //endregion
}
