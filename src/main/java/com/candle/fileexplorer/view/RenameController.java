package com.candle.fileexplorer.view;

import com.candle.fileexplorer.viewmodel.RenameViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * The view class that is used to rename files and folders.
 */
public class RenameController {
    //region Private Members

    //region GUI Elements

    @FXML
    private TextField itemNameField;

    //endregion

    private RenameViewModel viewModel;

    //endregion

    //region Public Methods

    /**
     * Initializes the "rename view" with the given view model and item path.
     * @param renameViewModel The view model that this class will bind to.
     * @param path The absolute path to the file/folder to be renamed.
     */
    public void init(RenameViewModel renameViewModel, String path) {
        this.viewModel = renameViewModel;

        viewModel.setItemPath(path);
        itemNameField.textProperty().bindBidirectional(viewModel.itemNameProperty());
        itemNameField.textProperty().setValue(viewModel.getOriginalName());

        Platform.runLater(() -> itemNameField.requestFocus());
    }

    //endregion

    //region Private Helper Methods

    @FXML
    private void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case ENTER -> renameItem(new ActionEvent());
            case ESCAPE -> closeWindow();
        }
    }

    @FXML
    private void renameItem(ActionEvent event) {
        viewModel.renameItem();
        closeWindow();
    }

    @FXML
    private void cancel(ActionEvent event) {
        closeWindow();
    }

    /**
     * Closes the "Rename Item" window.
     */
    private void closeWindow() {
        Stage stage = (Stage) itemNameField.getScene().getWindow();
        stage.close();
    }

    //endregion

}
