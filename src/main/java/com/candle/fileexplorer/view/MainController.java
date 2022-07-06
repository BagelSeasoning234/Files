package com.candle.fileexplorer.view;

import com.candle.fileexplorer.FilesApp;
import com.candle.fileexplorer.core.ViewHandler;
import com.candle.fileexplorer.view.enums.GridSortOrder;
import com.candle.fileexplorer.viewmodel.MainViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
        gridViews = new ArrayList<>();

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
     * Informs the view model to update the backend with the new current directory.
     */
    @FXML
    private void locationBarUpdated(ActionEvent event) {
        viewModel.userUpdatedDirectory();
    }

    // (The "create new item," "cut," "copy," and "paste" methods are located in the menu section.)

    //endregion

    //region Menu

    //region File

    /**
     * Shows the "create new item" page for the application.
     */
    @FXML
    private void createNewItem(ActionEvent event) {
        if (viewModel.canModifyItem(viewModel.currentDirectoryProperty().getValue())) {
            try {
                viewHandler.openSubView("NewFile", "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                viewHandler.openSubView("Error", "A file/folder cannot be created in this directory. It may be read-only.");
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    private void rename(ActionEvent event) {
        FileItemController fileItemView = getCurrentGridView().getSelectedFileItem();
        if (fileItemView == null)
            return;

        try {
            if (viewModel.canModifyItem(fileItemView.getItemDirectory()))
                viewHandler.openSubView("Rename", fileItemView.getItemDirectory());
            else
                viewHandler.openSubView("Error", "The selected file/folder could not be renamed. It may be read-only.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void trashItem(ActionEvent event) {
        FileItemController fileItemView = getCurrentGridView().getSelectedFileItem();
        if (fileItemView == null)
            return;

        if (viewModel.canModifyItem(fileItemView.getItemDirectory())) {
            try {
                viewModel.trashItem(fileItemView.getItemDirectory());
            } catch (IllegalStateException e) {
                try {
                    String dependencyError = """
                            The 'trash-cli' dependency could not be found.

                            Since you are using a Linux-based operating system, this package is necessary in order to perform operations involving the Trash Bin.
                            
                            The dependency should be installable via your package manager's repositories, or at 'https://github.com/andreafrancia/trash-cli'.
                            """;
                    viewHandler.openSubView("Error", dependencyError);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        else {
            try {
                viewHandler.openSubView("Error", "The selected file/folder could not be deleted. It may be read-only.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
        FileItemController itemView = getCurrentGridView().getSelectedFileItem();
        if (itemView == null)
            return;

        File file = itemView.getFile();
        if (file.canWrite())
            viewModel.cut(file);
        else {
            try {
                viewHandler.openSubView("Error", "The selected file/folder could not be cut. It may be read-only.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void copy(ActionEvent event) {
        FileItemController itemView = getCurrentGridView().getSelectedFileItem();
        if (itemView == null)
            return;

        File file = getCurrentGridView().getSelectedFileItem().getFile();
        viewModel.copy(file);
    }

    @FXML
    private void copyLocation(ActionEvent event) {
        FileItemController itemView = getCurrentGridView().getSelectedFileItem();
        if (itemView == null)
            return;

        String location = itemView.getItemDirectory();
        viewModel.copyLocation(location);
    }

    @FXML
    private void paste(ActionEvent event) {
        ArrayList<File> fileList;
        try {
            fileList = (ArrayList<File>) Clipboard.getSystemClipboard().getFiles();
        } catch (ClassCastException e) {
            return;
        }
        if (viewModel.canModifyItem(viewModel.currentDirectoryProperty().getValue())) {
            try {
                viewModel.paste(fileList);
            } catch (IllegalStateException e) {
                try {
                    String dependencyError = "There is a file/folder with an identical name in this directory.";
                    viewHandler.openSubView("Error", dependencyError);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        else {
            try {
                viewHandler.openSubView("Error", "The selected file/folder could not be pasted into this directory. It may be read-only.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //endregion

    //region View

    @FXML
    private void refresh(ActionEvent event) {
        getCurrentGridView().refresh();
    }

    @FXML
    private void sortByName(ActionEvent event) {
        getCurrentGridView().setSortOrder(GridSortOrder.Name);
    }

    @FXML
    private void sortByModified(ActionEvent event) {
        getCurrentGridView().setSortOrder(GridSortOrder.Modified);
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
            viewHandler.openSubView("About", "");
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
        FileGridController tabView = new FileGridController();
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
