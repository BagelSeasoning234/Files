package com.candle.fileexplorer.viewmodel;

import com.candle.fileexplorer.model.FilesModel;
import com.candle.fileexplorer.model.helpers.FileOperations;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The class that allows the 'rename' view to communicate with the database.
 */
public class RenameViewModel {
    //region Public Members/Properties

    /**
     * The file/folder name property that is exposed to the view.
     */
    private final StringProperty itemNameProperty;

    //endregion

    //region Private Members

    /**
     * The directory path for the file/folder to rename.
     */
    private String itemPath;

    FilesModel dataModel;

    //endregion

    //region Constructors

    /**
     * Constructs a new instance of the class with the given reference to the
     * data model.
     */
    public RenameViewModel(FilesModel dataModel) {
        this.dataModel = dataModel;

        itemNameProperty = new SimpleStringProperty();
    }

    //endregion

    //region Accessors/Mutators

    public void setItemPath(String path) {
        this.itemPath = path;
    }

    public StringProperty itemNameProperty() {
        return itemNameProperty;
    }

    //endregion

    //region Public Methods

    /**
     * Returns the original name of the file/folder.
     */
    public String getOriginalName() {
        return FileOperations.getPathName(itemPath);
    }

    /**
     * Tells the data model to rename the given file/folder at the specified
     * location.
     */
    public void renameItem() {
        if (canRenameItem()) {
            dataModel.renameItem(itemPath, itemNameProperty.getValue());
            itemNameProperty.setValue("");
            itemPath = "";
        }
    }

    //endregion

    //region Private Helper Methods

    /**
     * Checks to see if the given item can be renamed.
     */
    private boolean canRenameItem() {
        if (itemNameProperty.getValue() == null)
            return false;

        boolean nameIsNotEmpty = !itemNameProperty.getValue().equals("");
        boolean nameIsValid = !itemNameProperty.getValue().contains("/");
        boolean pathIsValid = !itemPath.equals("");

        return (nameIsNotEmpty && nameIsValid && pathIsValid);
    }

    //endregion
}
