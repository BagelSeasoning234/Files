package com.candle.fileexplorer.viewmodel;

import com.candle.fileexplorer.model.FilesModel;
import com.candle.fileexplorer.model.observer.DataListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The view model used by the main view.
 */
public class MainViewModel implements DataListener {
    //region Private Members

    /**
     * The current directory property for use in the GUI.
     */
    private StringProperty currentDirectoryProperty;

    /**
     * The data model containing information about the explorer's current directory.
     */
    FilesModel dataModel;

    /**
     * The view model for the grid of items.
     */
    private FileGridViewModel fileGridViewModel;

    /**
     * The view model for the quick access items.
     */
    private QuickAccessViewModel quickAccessVM;

    //endregion

    //region Constructor

    /**
     * Instantiates the main view model using the File Structure view model.
     *
     * @param gridVM        A reference to an existing file grid view model, which is created in the view model factory.
     * @param quickAccessVM A reference to an existing quick access view model, which is created in the view model factory.
     * @param dataModel     A reference to an existing data model, which is created in the model factory.
     */
    public MainViewModel(FileGridViewModel gridVM, QuickAccessViewModel quickAccessVM, FilesModel dataModel) {
        this.fileGridViewModel = gridVM;
        this.quickAccessVM = quickAccessVM;
        this.dataModel = dataModel;
        currentDirectoryProperty = new SimpleStringProperty();

        dataModel.addListener(this);
        currentDirectoryChanged();
    }

    //endregion

    //region Public Methods

    public FileGridViewModel getFileStructureViewModel() {
        return fileGridViewModel;
    }

    public QuickAccessViewModel getQuickAccessViewModel() {
        return quickAccessVM;
    }

    public StringProperty currentDirectoryProperty() {
        return currentDirectoryProperty;
    }

    /**
     * The method used by the view to inform the view model that the user is ready to go to the new directory.
     */
    public void userUpdatedDirectory() {
        dataModel.setCurrentDirectory(currentDirectoryProperty.getValue());
    }

    @Override
    public void currentDirectoryChanged() {
        currentDirectoryProperty.setValue(dataModel.getCurrentDirectory());
    }

    //endregion

    //region Private Methods

    //endregion
}
