package com.candle.fileexplorer.core;

import com.candle.fileexplorer.view.AboutController;
import com.candle.fileexplorer.view.MainController;
import com.candle.fileexplorer.view.NewFileController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
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
        openPrimaryView("Main");
    }

    /**
     * Opens a specified view in the main application.
     *
     * @param viewToOpen The name of the view to open, WITHOUT the "View.fxml" part.
     * @throws IOException If the view could not be found.
     */
    public void openPrimaryView(String viewToOpen) throws IOException {
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;

        if ("Main".equals(viewToOpen)) {
            String location = "/com/candle/fileexplorer/view/MainView.fxml";
            loader.setLocation(getClass().getResource(location));
            root = loader.load();

            MainController view = loader.getController();
            view.init(viewModelFactory.getMainViewModel(), this);
            stage.setTitle("Files");
        }

        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Opens a new window process with the specified view.
     * @param viewToOpen The name of the view to open, WITHOUT the "View.fxml" part.
     * @throws IOException If the view could not be found.
     */
    public void openSubView(String viewToOpen) throws IOException {
        Stage subStage = new Stage();
        Scene scene = null;
        Parent root = null;
        FXMLLoader loader = new FXMLLoader();

        if ("About".equals(viewToOpen)) {
            String location = "/com/candle/fileexplorer/view/AboutView.fxml";
            loader.setLocation(getClass().getResource(location));
            root = loader.load();

            AboutController view = loader.getController();
            view.init(viewModelFactory.getAboutViewModel());
            subStage.setTitle("About Files");
        }

        if ("NewFile".equals(viewToOpen)) {
            String location = "/com/candle/fileexplorer/view/NewFileView.fxml";
            loader.setLocation(getClass().getResource(location));
            root = loader.load();

            NewFileController view = loader.getController();
            view.init(viewModelFactory.getNewFileViewModel());
            subStage.setTitle("New Item");
        }

        scene = new Scene(root);
        subStage.setScene(scene);
        subStage.initModality(Modality.APPLICATION_MODAL);

        subStage.show();
    }

    //endregion

}
