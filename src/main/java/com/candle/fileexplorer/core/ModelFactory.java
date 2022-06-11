package com.candle.fileexplorer.core;

import com.candle.fileexplorer.model.FilesModel;
import com.candle.fileexplorer.model.DefaultFilesModel;

/**
 * The class that is responsible for creating the data models.
 */
public class ModelFactory {
    //region Private Members

    /**
     * A reference to the model that holds data about files.
     */
    private FilesModel filesModel;

    //endregion

    /**
     * Gets a reference to the files' data model.
     */
    public FilesModel getFilesModel() {
        if (filesModel == null)
            filesModel = new DefaultFilesModel();
        return filesModel;
    }
}
