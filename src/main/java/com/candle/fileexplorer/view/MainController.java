package com.candle.fileexplorer.view;

import com.candle.fileexplorer.FilesApp;
import com.candle.fileexplorer.core.ViewHandler;
import com.candle.fileexplorer.viewmodel.MainViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The view class for the main view.
 */
public class MainController {
    //region Private Members

    //region GUI Elements
    @FXML
    private MenuBar menuBar;

    @FXML
    private TabPane tabPane;

    /**
     * A collection of "quick access" directory paths, along with the current drives.
     */
    @FXML
    private QuickAccessController quickAccessView;

    /**
     * The text field containing the current directory.
     */
    @FXML
    private TextField locationBar;

    //endregion

    /**
     * The view model class for this object.
     */
    private MainViewModel viewModel;

    /**
     * A reference to the management class for view objects.
     */
    private ViewHandler viewHandler;

    //endregion

    //region Public Methods

    /**
     * Initializes the view by binding the view model data to the GUI.
     *
     * @param viewModel A reference to the main view model.
     */
    public void init(MainViewModel viewModel, ViewHandler viewHandler) {
        this.viewModel = viewModel;
        this.viewHandler = viewHandler;

        // Bind data here
        quickAccessView.init(viewModel.getQuickAccessViewModel());
        locationBar.textProperty().bindBidirectional(viewModel.currentDirectoryProperty());

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> tabChanged());
        addTab(new ActionEvent());
    }

    //endregion

    //region On Clicked Methods

    //region Toolbar

    /**
     * Informs the view model to update the backend with the new current directory.
     */
    @FXML
    private void locationBarUpdated(ActionEvent event) {
        viewModel.userUpdatedDirectory();
    }

    /**
     * Informs the view model to go back in history.
     */
    @FXML
    private void goBackDirectory(ActionEvent event) {
        viewModel.goBackDirectory();
    }

    /**
     * Informs the view model to go forward in history.
     */
    @FXML
    private void goForwardDirectory(ActionEvent event) {
        viewModel.goForwardDirectory();
    }

    /**
     * Informs the view model to go to the home directory.
     */
    @FXML
    private void goHomeDirectory(ActionEvent event) {
        viewModel.goHomeDirectory();
    }

    //endregion

    //region Menu

    /**
     * Shows the "create new item" page for the application.
     */
    @FXML
    private void createNewItem(ActionEvent event) {
        try {
            viewHandler.openSubView("NewFile");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new instance of the application.
     */
    @FXML
    private void openNewWindow(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                new FilesApp().start(new Stage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Opens a new tab.
     */
    @FXML
    private void addTab(ActionEvent event) {
        viewModel.addTab();
        Tab newTab = createTabView();
        tabPane.getTabs().add(newTab);

        tabPane.getSelectionModel().select(newTab);
    }

    @FXML
    private void closeTab(ActionEvent event) {
        attemptToCloseTab(tabPane.getSelectionModel().getSelectedItem());
    }

    /**
     * Closes the application.
     */
    @FXML
    private void closeApp(ActionEvent event) {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void refresh(ActionEvent event) {
        // TODO: Create the refresh function.
    }

    /**
     * Toggles the presence of hidden file items.
     */
    @FXML
    private void toggleHiddenItems(ActionEvent event) {
        viewModel.toggleHiddenItems();
    }

    /**
     * Shows the about page for the application.
     */
    @FXML
    private void aboutFilesApp(ActionEvent event) {
        try {
            viewHandler.openSubView("About");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //endregion

    //endregion

    //region Private Helper Methods

    /**
     * A helper method that creates a new tab object with a file grid view.
     */
    private Tab createTabView() {
        // TODO: Keep track of the current directory names on the view model/model so the tab can bind to them.
        Tab newTab = new Tab("Home");
        FileGridController tabView = new FileGridController();

        tabView.init(viewModel.getFileGridViewModel());
        newTab.setOnCloseRequest(e -> {
            attemptToCloseTab(newTab);
            e.consume();
        });
        newTab.setContent(tabView);
        return newTab;
    }

    /**
     * A helper method that runs when the user attempts to close a tab.
     * @param tab The tab that will be closed
     */
    private void attemptToCloseTab(Tab tab) {
        if (tabPane.getTabs().size() > 1)
            tabPane.getTabs().remove(tab);
    }

    /**
     * Tells the view model that the user has changed tabs.
     */
    private void tabChanged() {
        int newIndex = tabPane.getSelectionModel().getSelectedIndex();
        viewModel.setTabIndex(newIndex);
    }

    //endregion

}
