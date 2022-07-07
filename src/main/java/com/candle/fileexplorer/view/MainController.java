package com.candle.fileexplorer.view;

import com.candle.fileexplorer.FilesApp;
import com.candle.fileexplorer.core.ViewHandler;
import com.candle.fileexplorer.view.enums.GridSortOrder;
import com.candle.fileexplorer.view.helpers.ContextMenuActions;
import com.candle.fileexplorer.viewmodel.MainViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The view class for the main view.
 */
public class MainController {
    //region Private Members

    //region GUI Elements

    @FXML
    private BorderPane root;

    @FXML
    private MenuBar menuBar;

    @FXML
    private TabPane tabPane;

    private ArrayList<FileGridController> gridViews;

    /**
     * A collection of "quick access" directory paths, along with the current
     * drives.
     */
    @FXML
    private QuickAccessController quickAccessView;

    @FXML
    private TextField locationBar;

    @FXML
    private ToggleGroup sortByGroup;

    //endregion

    /**
     * The class that's used to call "new, rename, paste, etc" operations.
     */
    private ContextMenuActions contextMenuActions;

    /**
     * The view model class for this object.
     */
    private MainViewModel viewModel;

    //endregion

    //region Public Methods

    /**
     * Initializes the view by binding the view model data to the GUI.
     *
     * @param viewModel A reference to the main view model.
     */
    public void init(MainViewModel viewModel) {
        this.viewModel = viewModel;
        gridViews = new ArrayList<>();
        contextMenuActions = new ContextMenuActions();

        // Bind data here
        quickAccessView.init(viewModel.getQuickAccessViewModel());
        locationBar.textProperty().bindBidirectional(viewModel.currentDirectoryProperty());

        // Setup tabs
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> tabChanged());
        addTab(new ActionEvent());
    }

    /**
     * The global shortcut key filter.
     * This gets called whenever the user presses a key on the main window.
     */
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.TAB)) {
            locationBar.requestFocus();
            event.consume();
        }
    }

    //endregion

    //region On Clicked Methods

    /**
     * Handles the back and forward navigational buttons on the mouse.
     */
    @FXML
    private void handleNavButtons(MouseEvent event) {
        switch (event.getButton()) {
            case FORWARD -> {
                goForwardDirectory(new ActionEvent());
                event.consume();
            }
            case BACK -> {
                goBackDirectory(new ActionEvent());
                event.consume();
            }
        }
    }

    //region Toolbar

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

    /**
     * Informs the view model to update the backend with the new current
     * directory.
     */
    @FXML
    private void locationBarUpdated(ActionEvent event) {
        viewModel.userUpdatedDirectory();
    }

    // (The "create new item," "cut," "copy," and "paste" methods are located
    // in the menu section.)

    //endregion

    //region Menu

    //region File

    /**
     * Shows the "create new item" page for the application.
     */
    @FXML
    private void createNewItem(ActionEvent event) {
        contextMenuActions.createNewItem(viewModel.currentDirectoryProperty().getValue());
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
    private void rename(ActionEvent event) {
        FileItemController fileItemView =
                getCurrentGridView().getSelectedFileItem();
        if (fileItemView == null)
            return;

        contextMenuActions.renameItem(fileItemView.getItemDirectory());
    }

    @FXML
    private void trashItem(ActionEvent event) {
        FileItemController fileItemView =
                getCurrentGridView().getSelectedFileItem();
        if (fileItemView == null)
            return;

        contextMenuActions.trashItem(fileItemView.getItemDirectory(),
                viewModel.getFilesModel());
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

    //endregion

    //region Edit

    @FXML
    private void cut(ActionEvent event) {
        FileItemController itemView =
                getCurrentGridView().getSelectedFileItem();
        if (itemView == null)
            return;

        contextMenuActions.cutItem(itemView.getItemDirectory(),
                viewModel.getFilesModel());
    }

    @FXML
    private void copy(ActionEvent event) {
        FileItemController itemView =
                getCurrentGridView().getSelectedFileItem();
        if (itemView == null)
            return;

        contextMenuActions.copyItem(itemView.getItemDirectory(),
                viewModel.getFilesModel());
    }

    @FXML
    private void copyLocation(ActionEvent event) {
        FileItemController itemView =
                getCurrentGridView().getSelectedFileItem();
        if (itemView == null)
            return;

        contextMenuActions.copyItemLocation(itemView.getItemDirectory());
    }

    @FXML
    private void paste(ActionEvent event) {
        contextMenuActions.pasteItem(viewModel.getFilesModel());
    }

    //endregion

    //region View

    @FXML
    private void refresh(ActionEvent event) {
        getCurrentGridView().refresh();
    }

    @FXML
    private void sortByName(ActionEvent event) {
        contextMenuActions.sortBy(getCurrentGridView(), GridSortOrder.Name);
    }

    @FXML
    private void sortByModified(ActionEvent event) {
        contextMenuActions.sortBy(getCurrentGridView(), GridSortOrder.Modified);
    }

    /**
     * Toggles the presence of hidden file items.
     */
    @FXML
    private void toggleHiddenItems(ActionEvent event) {
        viewModel.toggleHiddenItems();
    }

    //endregion

    //region Go

    // (Go methods are handled in the toolbar region)

    //endregion

    //region Help

    /**
     * Shows the about page for the application.
     */
    @FXML
    private void aboutFilesApp(ActionEvent event) {
        try {
            ViewHandler.getInstance().openSubView("About", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //endregion

    //endregion

    //endregion

    //region Private Helper Methods

    /**
     * A helper method that creates a new tab object with a file grid view.
     */
    private Tab createTabView() {
        Tab newTab = new Tab();
        newTab.textProperty().bind(viewModel.getLastTabNameProperty());
        FileGridController tabView = new FileGridController(contextMenuActions);
        gridViews.add(tabView);

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
     *
     * @param tab The tab that will be closed.
     */
    private void attemptToCloseTab(Tab tab) {
        if (tabPane.getTabs().size() > 1) {
            int index = tabPane.getTabs().indexOf(tab);
            viewModel.closeTab(index);
            tabPane.getTabs().remove(tab);
        }
    }

    /**
     * Tells the view model that the user has changed tabs.
     */
    private void tabChanged() {
        int newIndex = tabPane.getSelectionModel().getSelectedIndex();
        viewModel.setTabIndex(newIndex);
    }

    /**
     * A helper method that returns the currently viewed grid controller.
     */
    private FileGridController getCurrentGridView() {
        return gridViews.get(viewModel.getTabIndex());
    }

    //endregion

}
