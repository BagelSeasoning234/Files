package com.candle.fileexplorer.viewmodel;

import com.candle.fileexplorer.model.FilesModel;
import com.candle.fileexplorer.model.data.ClipboardMode;
import com.candle.fileexplorer.model.helpers.FileUtilities;
import com.candle.fileexplorer.model.observer.DataListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.File;
import java.util.ArrayList;

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

    //region Constructors

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

    //region Accessors/Mutators

    public StringProperty currentDirectoryProperty() {
        return currentDirectoryProperty;
    }

    public FileGridViewModel getFileGridViewModel() {
        return fileGridViewModel;
    }

    public QuickAccessViewModel getQuickAccessViewModel() {
        return quickAccessViewModel;
    }

    public int getTabIndex() {
        return dataModel.getTabIndex();
    }

    public void setTabIndex(int index) {
        dataModel.setTabIndex(index);
    }

    //endregion

    //region Public Methods

    /**
     * Cuts the currently selected item.
     */
    public void cut(File item) {
        copy(item);
        dataModel.setClipboardMode(ClipboardMode.Cut);
    }

    public void copy(File item) {
        if (item != null) {
            ClipboardContent content = new ClipboardContent();
            ArrayList<File> fileList = new ArrayList<>();
            fileList.add(item);
            content.putFiles(fileList);
            Clipboard.getSystemClipboard().setContent(content);
        }
        dataModel.setClipboardMode(ClipboardMode.Copy);
    }

    public void copyLocation(String location) {
        if (!location.equals("")) {
            ClipboardContent content = new ClipboardContent();
            content.putString(location);
            Clipboard.getSystemClipboard().setContent(content);
        }
    }

    public void paste(ArrayList<File> itemList) {
        for (File item : itemList)
            dataModel.paste(item.getAbsolutePath());
    }

    /**
     * Checks to see if the given directory can be modified (i.e. item added, renamed, or deleted).
     * @param directory The directory to check
     */
    public boolean canModifyItem(String directory) {
        File currentDirectoryObject = new File(directory);
        return currentDirectoryObject.canWrite();
    }

    /**
     * Tells the data model to add another tab.
     */
    public void addTab() {
        dataModel.addTab(dataModel.getTabIndex() + 1);
    }

    /**
     * Tells the data model to remove the tab at the given index.
     */
    public void closeTab(int index) {
        dataModel.removeTab(index);
    }

    /**
     * Tells the data model to delete the item at the given directory.
     */
    public void trashItem(String path) {
        dataModel.trashItem(path);
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
     * The method used by the view to inform the view model that
     * the user is ready to go to the new directory.
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
