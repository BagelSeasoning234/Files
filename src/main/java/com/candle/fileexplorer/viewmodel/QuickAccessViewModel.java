package com.candle.fileexplorer.viewmodel;

import com.candle.fileexplorer.model.FilesModel;
import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.data.DefaultFileItem;
import com.candle.fileexplorer.model.helpers.FileOperations;

import java.util.ArrayList;

/**
 * The view model class for the Quick Access view.
 */
public class QuickAccessViewModel {
    //region Private Members

    /**
     * The data model containing information about the drives on the computer.
     */
    FilesModel dataModel;

    /**
     * A list of drives on the computer.
     */
    private ArrayList<DirectoryButtonViewModel> drives;

    //endregion

    //region Constructor

    /**
     * Initializes the view model and gets a list of drives on the computer.
     */
    public QuickAccessViewModel(FilesModel dataModel) {
        this.dataModel = dataModel;
        updateDrives();
    }

    //endregion

    //region Public Methods

    /**
     * Returns a list of directory button view models for each drive found on the pc.
     */
    public ArrayList<DirectoryButtonViewModel> getDriveVms() {
        return drives;
    }

    /**
     * Creates a new directory button view model for the given directory name.
     * @param directory The name of the directory, such as "Pictures," "Documents," or "Trash."
     * @return The view model for the directory.
     */
    public DirectoryButtonViewModel getQuickAccessButtonViewModel(String directory) {
        String directoryPath;
        if (directory.equals("Home"))
            directoryPath = System.getProperty("user.home");
        else if (directory.equals("Trash"))
            directoryPath = FileOperations.getTrashDirectory();
        else
            directoryPath = System.getProperty("user.home") + "/" + directory;

        FileItem item = new DefaultFileItem(directoryPath);
        return new DirectoryButtonViewModel(dataModel, item);
    }

    //endregion

    //region Private Methods

    /**
     * Updates the drive array by getting a list of logical drives from the computer.
     */
    private void updateDrives() {
        if (drives == null)
            drives = new ArrayList<>();
        else
            drives.clear();

        for (FileItem drive : dataModel.getDrives()) {
            drives.add(new DirectoryButtonViewModel(dataModel, drive));
        }
    }

    //endregion
}
