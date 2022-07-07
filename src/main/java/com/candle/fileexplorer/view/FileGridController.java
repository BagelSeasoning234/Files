package com.candle.fileexplorer.view;

import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.data.FileType;
import com.candle.fileexplorer.model.helpers.FileOperations;
import com.candle.fileexplorer.view.enums.GridSortOrder;
import com.candle.fileexplorer.view.helpers.ContextMenuActions;
import com.candle.fileexplorer.viewmodel.FileGridViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;

/**
 * The view class for the grid of files/folders in the GUI.
 */
public class FileGridController extends ScrollPane {

    //region Private Members

    //region GUI Elements

    @FXML
    private ContextMenu contextMenu;

    @FXML
    private GridPane gridPane;

    //endregion

    /**
     * The UI element that is currently focused.
     */
    private Node focusedNode;

    private final ContextMenuActions contextMenuActions;

    /**
     * The number of columns to be used in the grid pane.
     */
    private int maxColumns;

    /**
     * The order used to sort grid items.
     */
    private GridSortOrder sortOrder = GridSortOrder.Name;

    /**
     * A reference to the view model responsible for getting the file items.
     */
    private FileGridViewModel viewModel;

    /**
     * The file thumbnail image for use in file items that are of the type
     * "File."
     */
    Image fileThumbnail = new Image("/com/candle/fileexplorer/images/64/File" +
            ".png", 64, 64, true, false, true);
    /**
     * The folder thumbnail image for use in file items that are of the type
     * "Folder."
     */
    Image folderThumbnail = new Image("/com/candle/fileexplorer/images/64" +
            "/Folder.png", 64, 64, true, false, true);

    //endregion

    //region Constructors

    public FileGridController(ContextMenuActions contextMenuActions) {
        this.contextMenuActions = contextMenuActions;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com" +
                "/candle/fileexplorer/view/FileGridView.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //endregion

    //region Accessors/Mutators

    public void setSortOrder(GridSortOrder sortOrder) {
        this.sortOrder = sortOrder;
        updateGridContents();
    }

    //endregion

    //region Public Methods

    /**
     * Initializes the view by binding the view model data to the grid.
     *
     * @param viewModel A reference to the file structure view model.
     */
    public void init(FileGridViewModel viewModel) {
        this.viewModel = viewModel;

        // Bind and setup contents
        setWidthEventHandlers();
        viewModel.getItems().addListener(this::listListener);
        updateGridContents();

        // Create context menu
        this.setContextMenu(contextMenu);
    }

    public FileItemController getSelectedFileItem() {
        Node focusedItem = gridPane.getScene().getFocusOwner();
        if (focusedItem instanceof FileItemController)
            return (FileItemController) focusedItem;
        else
            return null;
    }

    /**
     * Refreshes the contents of the view.
     */
    public void refresh() {
        viewModel.updateContents();
        updateGridContents();
    }

    //endregion

    //region On Clicked Methods

    //region Context Menu

    /**
     * Opens up the "create new item" dialog window.
     */
    @FXML
    private void createNewItem(ActionEvent event) {
        contextMenuActions.createNewItem(viewModel.getCurrentDirectory());
    }

    /**
     * Opens the "rename item" dialog window.
     */
    @FXML
    private void renameItem(ActionEvent event) {
        contextMenuActions.renameItem(getFocusedItemPath());
    }

    /**
     * Sends the currently selected item to the trash bin.
     */
    @FXML
    private void trashItem(ActionEvent event) {
        contextMenuActions.trashItem(getFocusedItemPath(),
                viewModel.getFilesModel());
    }

    /**
     * Adds the currently selected item to the clipboard and sets up the data
     * model to paste it somewhere
     * with the "cut" operation.
     */
    @FXML
    private void cutItem(ActionEvent event) {
        contextMenuActions.cutItem(getFocusedItemPath(),
                viewModel.getFilesModel());
    }

    /**
     * Adds the currently selected item to the clipboard and sets up the data
     * model to paste it somewhere
     * with the "copy" operation.
     */
    @FXML
    private void copyItem(ActionEvent event) {
        contextMenuActions.copyItem(getFocusedItemPath(),
                viewModel.getFilesModel());
    }

    /**
     * Gets the currently selected item and adds its location to the clipboard.
     */
    @FXML
    private void copyItemLocation(ActionEvent event) {
        contextMenuActions.copyItemLocation(getFocusedItemPath());
    }

    /**
     * Pastes the item that's currently on the clipboard to the current
     * directory.
     */
    @FXML
    private void pasteItem(ActionEvent event) {
        contextMenuActions.pasteItem(viewModel.getFilesModel());
    }

    /**
     * Opens the focused file/folder in the user's default application.
     */
    @FXML
    private void openItemInDefaultApp(ActionEvent event) {
        String itemPath = getFocusedItemPath();
        if (!itemPath.equals(""))
            contextMenuActions.openItemInDefaultApp(itemPath);
        else
            contextMenuActions.openItemInDefaultApp(viewModel.getCurrentDirectory());
    }

    //endregion

    /**
     * Handles mouse clicks on nodes in the grid.
     */
    @FXML
    private void handleMouseClick(MouseEvent event) {
        focusedNode = getGridFileItem(event);
        if (focusedNode == null)
            return;

        int openItemClickCount = 2;
        int selectedItemClickCount = 1;

        // Select the item.
        if (event.getClickCount() == selectedItemClickCount) {
            focusedNode.requestFocus();
        }
        // Open the item.
        else if (event.getClickCount() == openItemClickCount) {
            viewModel.setCurrentDirectory(getFocusedItemPath());
        }
    }

    //endregion

    //region Private Helper Methods

    /**
     * Gets the absolute path of the item that is currently focused.
     *
     * @return The path, if found. Returns an empty string otherwise.
     */
    private String getFocusedItemPath() {
        FileItemController item = (FileItemController) focusedNode;
        if (item != null)
            return item.getItemDirectory();
        else
            return "";
    }

    /**
     * The function that runs whenever items in the view model get updated.
     *
     * @param c The list change
     */
    private void listListener(ListChangeListener.Change<? extends FileItem> c) {
        updateGridContents();
    }

    /**
     * Iterates through elements in the view model as it adds them to the
     * grid view.
     * Currently, the function re-adds everything when updated.
     * TODO: Optimize the grid construction in the future.
     */
    private void updateGridContents() {
        gridPane.getChildren().clear();

        int columnNumber = 0;
        int rowNumber = 0;

        for (FileItem item : sortItems(viewModel.getItems())) {
            // For each item, create a new view and add it to the grid at the
            // specified column and row indexes.
            FileItemController fileItemView = createFileView();
            fileItemView.init(item, (item.getFileType() == FileType.File) ?
                    fileThumbnail : folderThumbnail);
            gridPane.add(fileItemView, columnNumber, rowNumber);

            // Increment the column indexes.
            columnNumber++;
            if (columnNumber == maxColumns) {
                columnNumber = 0;
                rowNumber++;
            }
        }

    }

    /**
     * Using the defined sort order, this function reorganizes a list of
     * FileItemViewModels.
     */
    private ObservableList<FileItem> sortItems(ObservableList<FileItem> items) {
        // I duplicate the array to prevent the function from making changes
        // to the underlying view model's data. (Otherwise, this causes a
        // stack overflow error).
        ObservableList<FileItem> newItemList =
                FXCollections.observableArrayList(items);

        switch (sortOrder) {
            // Sorts based on the alphabetical name of each item.
            case Name ->
                    newItemList.sort((o1, o2) -> o1.getFileName().compareToIgnoreCase(o2.getFileName()));
            // Sorts based on the last modified date of each item.
            case Modified -> newItemList.sort(Comparator.comparing(FileItem::getLastModifiedTime));
        }
        return newItemList;
    }

    /**
     * Creates a new instance of the file view class.
     */
    private FileItemController createFileView() {
        // Create the view
        FileItemController fileItemView = new FileItemController();
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/candle/fileexplorer" +
                "/view/FileItemView.fxml"));
        loader.setRoot(fileItemView);
        // For some reason, the FXML elements aren't initialized properly
        // unless the controller is set here.
        loader.setController(fileItemView);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileItemView;
    }

    /**
     * A helper function that gets the clicked file item from the grid using
     * the clicked mouse event.
     */
    private Node getGridFileItem(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        // Get a child element of the grid.
        if (clickedNode != gridPane) {
            // Walk up the hierarchy to get a direct sub-item of the grid
            Node parent = clickedNode.getParent();
            while (parent != gridPane) {
                clickedNode = parent;
                parent = clickedNode.getParent();
            }
            return clickedNode;
        }
        return null;
    }

    /**
     * Sets up the "responsive ui" handlers that ensure the grid
     * always has the correct number of columns.
     */
    private void setWidthEventHandlers() {
        this.widthProperty().addListener((observable, oldValue, newValue) -> {
            maxColumns = (int) (newValue.doubleValue() / 145);
            updateGridContents();
        });
    }

    //endregion
}
