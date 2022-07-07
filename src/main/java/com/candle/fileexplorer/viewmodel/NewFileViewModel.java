package com.candle.fileexplorer.viewmodel;

import com.candle.fileexplorer.model.FilesModel;
import com.candle.fileexplorer.model.data.FileType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NewFileViewModel {
    //region Public Members/Properties

    /**
     * The file/folder name property that is exposed to the view.
     */
    private final StringProperty itemNameProperty;

    //endregion

    //region Private Members

    private final FilesModel dataModel;

    /**
     * The type of item to be created (file/folder).
     */
    private FileType type = FileType.File;

    //endregion

    //region Constructors

    public NewFileViewModel(FilesModel dataModel) {
        this.dataModel = dataModel;

        itemNameProperty = new SimpleStringProperty();
    }

    //endregion

    //region Accessors/Mutators

    public void setType(FileType type) {
        this.type = type;
    }

    public StringProperty itemNameProperty() {
        return itemNameProperty;
    }

    //endregion

    //region Public Methods

    /**
     * Tells the data model to create a new file/folder at the specified
     * location.
     */
    public void createItem() {
        if (canCreateItem()) {
            dataModel.createItem(type, itemNameProperty.getValue());
            itemNameProperty.setValue("");
            type = FileType.File;
        }
    }

    //endregion

    //region Private Helper Methods

    /**
     * Checks to see if a new item can be created at this location
     */
    private boolean canCreateItem() {
        if (itemNameProperty.getValue() == null)
            return false;

        boolean nameIsNotEmpty = !itemNameProperty.getValue().equals("");
        boolean typeIsValid = !type.equals(FileType.Drive);
        boolean nameIsValid = !itemNameProperty.getValue().contains("/");

        return (nameIsNotEmpty && typeIsValid && nameIsValid);
    }

    //endregion
}
