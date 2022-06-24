package com.candle.fileexplorer.view;

import com.candle.fileexplorer.viewmodel.AboutViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AboutController {
    //region Private Members

    @FXML
    private VBox backgroundLayout;

    /**
     * The version of the application
     */
    private String appVersion;

    /**
     * The version of JavaFx that is being used.
     */
    private String javaFxVersion;

    /**
     * The version of java that is being used.
     */
    private String javaVersion;

    //endregion

    //region Public Methods

    public void init(AboutViewModel viewModel) {
        appVersion = viewModel.getFilesVersion();
        javaFxVersion = viewModel.getJavaFxVersion();
        javaVersion = viewModel.getJavaVersion();
    }

    public String getAppVersion() {
        return "Version: " + appVersion;
    }

    public String getJavaFxVersion() {
        return "JavaFX Version: " + javaFxVersion;
    }

    public String getJavaVersion() {
        return "Java Version: " + javaVersion;
    }

    public void closeAboutWindow(ActionEvent event) {
        Stage stage = (Stage) backgroundLayout.getScene().getWindow();
        stage.close();
    }

    //endregion
}
