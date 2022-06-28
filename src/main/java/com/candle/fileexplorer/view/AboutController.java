package com.candle.fileexplorer.view;

import com.candle.fileexplorer.viewmodel.AboutViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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

    //region Private Helper Methods

    /**
     * Sets up the keybindings for elements in this window.
     */
    @FXML
    private void onKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            closeAboutWindow(new ActionEvent());
        }
    }

    @FXML
    private void openJavaFx(MouseEvent event) {
        openUrl("https://openjfx.io/");
    }

    @FXML
    private void openJava(MouseEvent event) {
        openUrl("https://openjdk.org/");
    }

    @FXML
    private void openTrashcli(MouseEvent event) {
        openUrl("https://github.com/andreafrancia/trash-cli");
    }

    /**
     * Opens an url to the given website.
     * @param url The link to the website.
     */
    private void openUrl(String url) {
        String OS = System.getProperty("os.name");
        if (OS.equals("Linux")) {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + url);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                java.awt.Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    //endregion
}
