package com.candle.fileexplorer.viewmodel;

import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.helpers.DirectoryStructure;
import com.candle.fileexplorer.model.observer.DataListener;
import com.candle.fileexplorer.model.FilesModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FileGridViewModel implements DataListener {
    //region Private Members

    /**
     * The data model containing information about the explorer's current directory.
     */
    FilesModel dataModel;

    /**
     * A list of file item view models to be exposed to the UI.
     */
    private final ObservableList<FileItem> items;

    /**
     * A boolean variable that is used to determine whether hidden files/folders should be displayed.
     */
    private boolean showHiddenItems = false;

    //endregion

    //region Constructors

    /**
     * Initializes the view model and sets the current directory to the user's home folder.
     */
    public FileGridViewModel(FilesModel dataModel) {
        items = FXCollections.observableArrayList();
        this.dataModel = dataModel;
        dataModel.addListener(this);
        //updateContents();
    }

    //endregion

    //region Accessors/Mutators

    public ObservableList<FileItem> getItems() {
        return items;
    }

    /**
     * Updates the data model's current directory with the new path.
     */
    public void setCurrentDirectory(String newDirectory) {
        dataModel.setCurrentDirectory(newDirectory);
    }

    public boolean getShowHiddenItems() {
        return showHiddenItems;
    }

    public void setShowHiddenItems(boolean value) {
        this.showHiddenItems = value;
    }

    //endregion

    //region Public Methods

    /**
     * Updates the file items in the view model using the new current directory value.
     */
    public void updateContents() {
        if (items.size() > 0)
            items.clear();

        // Get the files/folders.
        items.addAll(DirectoryStructure.getDirectoryContents(dataModel.getCurrentDirectory(), showHiddenItems));
    }

    @Override
    public void currentDirectoryChanged() {
        updateContents();
    }

    //endregion
}
