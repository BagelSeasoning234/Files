package com.candle.fileexplorer.core;

import com.candle.fileexplorer.view.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The class that is responsible for creating and changing between views.
 */
public class ViewHandler {
    //region Private Members

    private static ViewHandler instance;

    /**
     * A reference to the application window.
     */
    private Stage stage;
    /**
     * A reference to the view model factory.
     */
    private ViewModelFactory viewModelFactory;

    //endregion

    //region Constructor

    private ViewHandler() {
    }

    //endregion

    //region Public Methods

    /**
     * Initializes the stage and view model factory variables.
     */
    public void init(Stage stage, ViewModelFactory viewModelFactory) {
        this.stage = stage;
        this.viewModelFactory = viewModelFactory;
    }

    /**
     * Starts the application by opening up the main view.
     *
     * @throws Exception If the view could not be found.
     */
    public void start() throws Exception {
        openPrimaryView("Main");
    }

    /**
     * Returns the static instance of the view handler.
     */
    public static ViewHandler getInstance() {
        if (instance == null)
            instance = new ViewHandler();

        return instance;
    }

    /**
     * Opens a specified view in the main application.
     *
     * @param viewToOpen The name of the view to open, WITHOUT the "View
     *                   .fxml" part.
     * @throws IOException If the view could not be found.
     */
    public void openPrimaryView(String viewToOpen) throws IOException {
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader();
        Parent root;

        if ("Main".equals(viewToOpen)) {
            String location = "/com/candle/fileexplorer/view/MainView.fxml";
            loader.setLocation(ViewHandler.class.getResource(location));
            root = loader.load();
            scene = new Scene(root);

            MainController view = loader.getController();
            view.init(viewModelFactory.getMainViewModel());
            stage.setTitle("Files");

            stage.setMinHeight(350);
            stage.setMinWidth(550);

            scene.addEventFilter(KeyEvent.KEY_PRESSED, view::onKeyPressed);
        }

        Image filesIcon = new Image(ViewHandler.class.getResourceAsStream(
                "/com/candle/fileexplorer/images/32/Folder.png"));
        stage.getIcons().add(filesIcon);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Opens a new window process with the specified view.
     *
     * @param viewToOpen The name of the view to open, WITHOUT the "View
     *                   .fxml" part.
     * @param arg        An optional additional argument, if the view
     *                   supports it.
     * @throws IOException If the view could not be found.
     */
    public void openSubView(String viewToOpen, String arg) throws IOException {
        Stage subStage = new Stage();
        Scene scene;
        Parent root = null;
        FXMLLoader loader = new FXMLLoader();

        if ("About".equals(viewToOpen)) {
            String location = "/com/candle/fileexplorer/view/AboutView.fxml";
            loader.setLocation(ViewHandler.class.getResource(location));
            root = loader.load();

            AboutController view = loader.getController();
            view.init(viewModelFactory.getAboutViewModel());
            subStage.setTitle("About Files");
        }

        if ("NewFile".equals(viewToOpen)) {
            String location = "/com/candle/fileexplorer/view/NewFileView.fxml";
            loader.setLocation(ViewHandler.class.getResource(location));
            root = loader.load();

            NewFileController view = loader.getController();
            view.init(viewModelFactory.getNewFileViewModel());
            subStage.setTitle("New Item");
        }

        if ("Rename".equals(viewToOpen)) {
            String location = "/com/candle/fileexplorer/view/RenameView.fxml";
            loader.setLocation(ViewHandler.class.getResource(location));
            root = loader.load();

            RenameController view = loader.getController();
            view.init(viewModelFactory.getRenameViewModel(), arg);
            subStage.setTitle("Rename Item");
        }

        if ("Error".equals(viewToOpen)) {
            String location = "/com/candle/fileexplorer/view/ErrorView.fxml";
            loader.setLocation(ViewHandler.class.getResource(location));
            root = loader.load();

            ErrorController view = loader.getController();
            view.init(arg);
            subStage.setTitle("Error");
        }

        scene = new Scene(root);
        subStage.setScene(scene);
        subStage.initModality(Modality.APPLICATION_MODAL);

        subStage.show();
    }

    //endregion
}
