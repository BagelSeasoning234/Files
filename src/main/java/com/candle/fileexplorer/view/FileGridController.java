package com.candle.fileexplorer.view;

import com.candle.fileexplorer.view.enums.GridSortOrder;
import com.candle.fileexplorer.viewmodel.FileItemViewModel;
import com.candle.fileexplorer.viewmodel.FileGridViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import java.io.IOException;
import java.util.Comparator;

/**
 * The view class for the grid of files/folders in the GUI.
 */
public class FileGridController extends GridPane
{
    //region Private Members

    /**
     * The number of columns to be used in the grid pane.
     */
    private final int MAX_COLUMNS = 6;

    /**
     * The number of clicks necessary to select an item.
     */
    private final int SELECTED_ITEM_CLICK_COUNT = 1;

    /**
     * The number of clicks necessary to open an item.
     */
    private final int OPEN_ITEM_CLICK_COUNT = 2;

    /**
     * The HGap and VGap spacing between items in the grid.
     */
    private final int ITEM_SPACING = 10;

    /**
     * The order used to sort grid items.
     */
    private GridSortOrder sortOrder = GridSortOrder.Name;

    /**
     * A reference to the view model responsible for getting the file items.
     */
    private FileGridViewModel viewModel;

    //endregion

    //region Constructor

    public FileGridController()
    {
    }

    //endregion

    //region Public Methods

    /**
     * Initializes the view by binding the view model data to the grid.
     * @param viewModel A reference to the file structure view model.
     */
    public void init(FileGridViewModel viewModel)
    {
        this.viewModel = viewModel;

        // Bind and setup contents
        viewModel.getItems().addListener(this::listListener);
        updateGridContents();

        setVisualAppearance();
        setEvents();
    }

    /**
     * Handles mouse clicks on nodes in the grid.
     */
    public void handleMouseClick(MouseEvent event)
    {
        Node clickedNode = getGridFileItem(event);
        if (clickedNode == null)
            return;

        // Select the item.
        if (event.getClickCount() == SELECTED_ITEM_CLICK_COUNT)
        {
            clickedNode.requestFocus();
        }
        // Open the item.
        else if (event.getClickCount() == OPEN_ITEM_CLICK_COUNT)
        {
            String newCurrentDirectory = ((FileItemController)clickedNode).getItemDirectory();
            viewModel.setCurrentDirectory(newCurrentDirectory);
            //updateGridContents();
        }
    }

    public GridSortOrder getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder(GridSortOrder sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    //endregion

    //region Private Methods

    /**
     * The function that runs whenever items in the view model get updated.
     * @param c The list change
     */
    private void listListener(ListChangeListener.Change<? extends FileItemViewModel> c)
    {
        updateGridContents();
    }

    /**
     * Iterates through elements in the view model as it adds them to the grid view.
     * Currently, the function re-adds everything when updated.
     * TODO: Optimize the grid construction in the future.
     */
    private void updateGridContents()
    {
        this.getChildren().clear();

        int columnNumber = 0;
        int rowNumber = 0;
        for (FileItemViewModel item : sortItems(viewModel.getItems()))
        {
            // For each item, create a new view and add it to the grid at the specified column and row indexes.
            FileItemController fileItemView = createFileView();
            fileItemView.init(item);
            this.add(fileItemView, columnNumber, rowNumber);

            // Increment the column indexes.
            columnNumber++;
            if (columnNumber == MAX_COLUMNS)
            {
                columnNumber = 0;
                rowNumber++;
            }
        }

    }

    /**
     * Using the defined sort order, this function reorganizes a list of FileItemViewModels.
     *
     */
    private ObservableList<FileItemViewModel> sortItems(ObservableList<FileItemViewModel> items)
    {
        // TODO: Make the Size sort algorithm account for folder sizes.
        //  This approach might work: https://stackoverflow.com/questions/2149785/get-size-of-folder-or-file/19877372#19877372

        // I duplicate the array to prevent the function from making changes to the underlying view model's data. (Otherwise, this causes a stack overflow error).
        ObservableList<FileItemViewModel> newItemList = FXCollections.observableArrayList(items);

        switch (sortOrder)
        {
            // Sorts based on the alphabetical name of each item.
            case Name -> newItemList.sort((o1, o2) -> o1.getFileName().compareToIgnoreCase(o2.getFileName()));
            // Sorts based on the size of each item.
            case Size -> newItemList.sort(Comparator.comparingLong(FileItemViewModel::getFileSize));
            // Sorts based on the last modified date of each item.
            case Modified -> newItemList.sort(Comparator.comparing(FileItemViewModel::getLastModifiedTime));
        }
        return newItemList;
    }

    /**
     * Creates a new instance of the file view class.
     */
    private FileItemController createFileView()
    {
        // Create the view
        FileItemController fileItemView = new FileItemController();
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/candle/fileexplorer/view/FileItemView.fxml"));
        loader.setRoot(fileItemView);
        // For some reason, the FXML elements aren't initialized properly unless the controller is set here.
        loader.setController(fileItemView);

        try
        {
            loader.load();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        // Return the view
        return fileItemView;
    }

    /**
     * A helper function that gets the clicked file item from the grid using the clicked mouse event.
     */
    private Node getGridFileItem(MouseEvent event)
    {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        // Get a child element of the grid.
        if (clickedNode != this)
        {
            // Walk up the hierarchy to get a direct sub-item of the grid
            Node parent = clickedNode.getParent();
            while (parent != this)
            {
                clickedNode = parent;
                parent = clickedNode.getParent();
            }
            return clickedNode;
        }
        return null;
    }

    /**
     * For some reason the CSS wasn't working, so I'm setting the values through Java.
     */
    private void setVisualAppearance()
    {
        this.setHgap(ITEM_SPACING);
        this.setVgap(ITEM_SPACING);
        this.setPadding(new Insets(10));
    }

    /**
     * For some reason the FXML references weren't working, so I'm setting the values through Java.
     */
    private void setEvents()
    {
        this.setOnMouseClicked(this::handleMouseClick);
    }

    //endregion
}
