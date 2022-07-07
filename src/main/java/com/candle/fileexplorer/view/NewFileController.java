package com.candle.fileexplorer.view;

import com.candle.fileexplorer.model.data.FileType;
import com.candle.fileexplorer.viewmodel.NewFileViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class NewFileController {
    //region Public Members/Properties

    //endregion

    //region Private Members

    @FXML
    private TextField itemNameField;

    private NewFileViewModel viewModel;

    //endregion

    //region Constructors

    //endregion

    //region Accessors/Mutators

    //endregion

    //region Public Methods

    /**
     * Initializes the New Item sub-window with the given instance of the view model.
     */
    public void init(NewFileViewModel viewModel) {
        this.viewModel = viewModel;

        itemNameField.textProperty().bindBidirectional(viewModel.itemNameProperty());
        Platform.runLater(() -> itemNameField.requestFocus());
    }

    //endregion

    //region Private Helper Methods

    @FXML
    private void createItem(ActionEvent event) {
        viewModel.createItem();
        closeWindow();
    }

    @FXML
    private void cancel(ActionEvent event) {
        closeWindow();
    }

    @FXML
    private void setFile(ActionEvent event) {
        viewModel.setType(FileType.File);
    }

    @FXML
    private void setFolder(ActionEvent event) {
        viewModel.setType(FileType.Folder);
    }

    @FXML
    private void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case ENTER -> createItem(new ActionEvent());
            case ESCAPE -> closeWindow();
        }
    }

    /**
     * Closes the "New Item" window.
     */
    private void closeWindow() {
        Stage stage = (Stage) itemNameField.getScene().getWindow();
        stage.close();
    }

    //endregion
}
