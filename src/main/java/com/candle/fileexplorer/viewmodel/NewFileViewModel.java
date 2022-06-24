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
    public StringProperty itemNameProperty;

    //endregion

    //region Private Members

    private FilesModel dataModel;

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
     * Tells the data model to create a new file/folder at the specified location.
     */
    public void createItem() {
        if (itemNameProperty.getValue().equals(""))
            return;

        dataModel.createItem(type, itemNameProperty.getValue());
        itemNameProperty.setValue("");
    }

    //endregion

    //region Private Helper Methods

    //endregion
}
