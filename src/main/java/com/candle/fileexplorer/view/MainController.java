package com.candle.fileexplorer.view;

import com.candle.fileexplorer.viewmodel.MainViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * The view class for the main view.
 */
public class MainController
{

    //region GUI Elements

    /**
     * The grid of file items.
     */
    @FXML
    FileGridController fileGridView;

    /**
     * The text field containing the current directory.
     */
    @FXML
    TextField locationBar;

    //endregion

    //region Private Members

    private MainViewModel viewModel;

    //endregion

    //region Public Methods

    /**
     * Initializes the view by binding the view model data to the GUI.
     * @param viewModel A reference to the main view model.
     */
    public void init(MainViewModel viewModel)
    {
        this.viewModel = viewModel;

        // Bind data here
        fileGridView.init(viewModel.getFileStructureViewModel());
        locationBar.textProperty().bindBidirectional(viewModel.currentDirectoryProperty());
    }

    // "On Clicked" functions go here.

    /**
     * Informs the view model to update the backend with the new current directory.
     */
    public void locationBarUpdated(ActionEvent event)
    {
        viewModel.userUpdatedDirectory();
    }

    //endregion

}
