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
    private final StringProperty currentDirectoryProperty;

    /**
     * The data model containing information about the explorer's current directory.
     */
    private final FilesModel dataModel;

    /**
     * The view model for the grid of items.
     */
    private final FileGridViewModel fileGridViewModel;

    /**
     * The view model for the quick access items.
     */
    private final QuickAccessViewModel quickAccessViewModel;

    //endregion

    //region Constructor

    /**
     * Instantiates the main view model using the File Structure view model.
     *
     * @param gridVM        A reference to an existing file grid view model, which is created in the view model factory.
     * @param quickAccessViewModel A reference to an existing quick access view model, which is created in the view model factory.
     * @param dataModel     A reference to an existing data model, which is created in the model factory.
     */
    public MainViewModel(FileGridViewModel gridVM, QuickAccessViewModel quickAccessViewModel, FilesModel dataModel) {
        currentDirectoryProperty = new SimpleStringProperty();

        this.fileGridViewModel = gridVM;
        this.quickAccessViewModel = quickAccessViewModel;
        this.dataModel = dataModel;

        dataModel.addListener(this);
        currentDirectoryChanged();
    }

    //endregion

    //region Public Methods

    public FileGridViewModel getFileGridViewModel() {
        return fileGridViewModel;
    }

    public QuickAccessViewModel getQuickAccessViewModel() {
        return quickAccessViewModel;
    }

    public StringProperty currentDirectoryProperty() {
        return currentDirectoryProperty;
    }

    public void addTab() {
        dataModel.addTab(dataModel.getTabIndex() + 1);
    }

    public void setTabIndex(int index) {
        dataModel.setTabIndex(index);
    }

    /**
     * Tells the data model to go back one directory in history.
     */
    public void goBackDirectory() {
        dataModel.goBackwardInDirectoryHistory();
    }

    /**
     * Tells the data model to go "forward" in history to the last selected directory.
     */
    public void goForwardDirectory() {
        dataModel.goForwardInDirectoryHistory();
    }

    /**
     * Tells the data model to go to the home directory.
     */
    public void goHomeDirectory() {
        dataModel.setCurrentDirectory(System.getProperty("user.home"));
    }

    /**
     * Tells the grid view to toggle the appearance of hidden items.
     */
    public void toggleHiddenItems() {
        getFileGridViewModel().setShowHiddenItems(!getFileGridViewModel().getShowHiddenItems());
        getFileGridViewModel().updateContents();
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

    //region Private Helper Methods

    //endregion
}
