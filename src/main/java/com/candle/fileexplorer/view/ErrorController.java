package com.candle.fileexplorer.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorController {
    //region Private Members

    @FXML
    private Label errorText;

    //endregion

    //region Public Methods

    /**
     * Initializes the view and sets the error message to the given text.
     */
    public void init(String errorMessage) {
        errorText.setText(errorMessage);
    }

    //endregion

    //region On Clicked Methods

    /**
     * Closes the error window when the user hits the close button.
     */
    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) errorText.getScene().getWindow();
        stage.close();
    }

    //endregion
}
