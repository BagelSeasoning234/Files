package com.candle.fileexplorer.core;

import com.candle.fileexplorer.view.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The class that is responsible for creating and changing between views.
 */
public class ViewHandler {
    //region Private Members

    /**
     * A reference to the application window.
     */
    private Stage stage;
    /**
     * A reference to the view model factory.
     */
    private ViewModelFactory viewModelFactory;

    //endregion

    /**
     * Initializes the stage and view model factory variables.
     */
    public ViewHandler(Stage stage, ViewModelFactory viewModelFactory) {
        this.stage = stage;
        this.viewModelFactory = viewModelFactory;
    }

    //region Public Methods

    /**
     * Starts the application by opening up the main view.
     *
     * @throws Exception If the view could not be found.
     */
    public void start() throws Exception {
        openView("Main");
    }

    /**
     * Opens a specified view
     *
     * @param viewToOpen The name of the view to open, WITHOUT the "View.fxml" part.
     * @throws IOException If the view could not be found.
     */
    public void openView(String viewToOpen) throws IOException {
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;

        if ("Main".equals(viewToOpen)) {
            String location = "/com/candle/fileexplorer/view/MainView.fxml";
            loader.setLocation(getClass().getResource(location));
            root = loader.load();

            MainController view = loader.getController();
            view.init(viewModelFactory.getMainViewModel());
            stage.setTitle("Files");
        }

        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //endregion

}
