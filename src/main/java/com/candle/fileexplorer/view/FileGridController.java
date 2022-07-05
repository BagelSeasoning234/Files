package com.candle.fileexplorer.view;

import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.data.FileType;
import com.candle.fileexplorer.view.enums.GridSortOrder;
import com.candle.fileexplorer.viewmodel.FileGridViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Comparator;

/**
 * The view class for the grid of files/folders in the GUI.
 */
public class FileGridController extends ScrollPane {

    //region Private Members

    //region GUI Elements

    /**
     * The grid of items for this view.
     */
    @FXML
    private GridPane gridPane;

    //endregion

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
     * The file thumbnail image for use in file items that are of the type "File."
     */
    Image fileThumbnail = new Image("/com/candle/fileexplorer/images/64/File.png", 64, 64, true, false, true);
    /**
     * The folder thumbnail image for use in file items that are of the type "Folder."
     */
    Image folderThumbnail = new Image("/com/candle/fileexplorer/images/64/Folder.png", 64, 64, true, false, true);

    //endregion

    //region Constructors

    public FileGridController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/candle/fileexplorer/view/FileGridView.fxml"));
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

    //region Private Helper Methods

    //region On Clicked Methods

    /**
     * Handles mouse clicks on nodes in the grid.
     */
    @FXML
    private void handleMouseClick(MouseEvent event) {
        Node clickedNode = getGridFileItem(event);
        if (clickedNode == null)
            return;

        int openItemClickCount = 2;
        int selectedItemClickCount = 1;

        // Select the item.
        if (event.getClickCount() == selectedItemClickCount) {
            clickedNode.requestFocus();
        }
        // Open the item.
        else if (event.getClickCount() == openItemClickCount) {
            String newCurrentDirectory = ((FileItemController) clickedNode).getItemDirectory();
            viewModel.setCurrentDirectory(newCurrentDirectory);
        }
    }

    //endregion

    /**
     * The function that runs whenever items in the view model get updated.
     *
     * @param c The list change
     */
    private void listListener(ListChangeListener.Change<? extends FileItem> c) {
        updateGridContents();
    }

    /**
     * Iterates through elements in the view model as it adds them to the grid view.
     * Currently, the function re-adds everything when updated.
     * TODO: Optimize the grid construction in the future.
     */
    private void updateGridContents() {
        gridPane.getChildren().clear();

        int columnNumber = 0;
        int rowNumber = 0;

        for (FileItem item : sortItems(viewModel.getItems())) {
            // For each item, create a new view and add it to the grid at the specified column and row indexes.
            FileItemController fileItemView = createFileView();
            fileItemView.init(item, (item.getFileType() == FileType.File) ? fileThumbnail : folderThumbnail);
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
     * Using the defined sort order, this function reorganizes a list of FileItemViewModels.
     */
    private ObservableList<FileItem> sortItems(ObservableList<FileItem> items) {
        // I duplicate the array to prevent the function from making changes to the underlying view model's data. (Otherwise, this causes a stack overflow error).
        ObservableList<FileItem> newItemList = FXCollections.observableArrayList(items);

        switch (sortOrder) {
            // Sorts based on the alphabetical name of each item.
            case Name -> newItemList.sort((o1, o2) -> o1.getFileName().compareToIgnoreCase(o2.getFileName()));
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
        loader.setLocation(getClass().getResource("/com/candle/fileexplorer/view/FileItemView.fxml"));
        loader.setRoot(fileItemView);
        // For some reason, the FXML elements aren't initialized properly unless the controller is set here.
        loader.setController(fileItemView);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileItemView;
    }

    /**
     * A helper function that gets the clicked file item from the grid using the clicked mouse event.
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
