package com.candle.fileexplorer.view;

import com.candle.fileexplorer.viewmodel.DirectoryButtonViewModel;
import com.candle.fileexplorer.viewmodel.QuickAccessViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * The view class for the custom Quick Access component.
 */
public class QuickAccessController extends ScrollPane {
    //region Private Members

    /**
     * A vertical list of Directory Button items.
     */
    @FXML
    private VBox quickAccessList;

    /**
     * A reference to the view model for this object.
     */
    private QuickAccessViewModel viewModel;

    //endregion

    //region Constructor

    /**
     * Creates a new instance of the quick access view by calling the fxml loader.
     */
    public QuickAccessController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/candle/fileexplorer/view/QuickAccessView.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //endregion

    //region Public Methods

    /**
     * Initializes the view for this item by adding the drives from the pc.
     */
    public void init(QuickAccessViewModel viewModel) {
        this.viewModel = viewModel;

        constructAccessDirectories();
        setupAppearance();
    }

    //endregion

    //region Private Methods

    /**
     * Adds a list of quick access buttons to the view.
     */
    private void constructAccessDirectories() {
        // Quick Access
        Label quickAccessLabel = new Label("Quick Access");
        quickAccessList.getChildren().add(quickAccessLabel);
        addQuickAccessButtons();

        // Spacer
        Region spacer = new Region();
        spacer.setMinHeight(10);
        VBox.setVgrow(spacer, Priority.ALWAYS);
        quickAccessList.getChildren().add(spacer);

        // Drives
        Label driveLabel = new Label("Drives");
        quickAccessList.getChildren().add(driveLabel);
        addDriveButtons();
    }

    /**
     * Adds the quick access buttons like "Pictures," "Desktop," and "Documents" to the list view.
     */
    private void addQuickAccessButtons() {
        String folderPath = "/com/candle/fileexplorer/images/16/Folder.png";
        String trashPath = "/com/candle/fileexplorer/images/16/Trash.png";
        Image folderImage = new Image(folderPath);

        String[] quickAccessItems = {"Home", "Desktop", "Downloads", "Documents", "Pictures", "Music", "Trash"};

        for (String item : quickAccessItems) {
            DirectoryButtonViewModel directoryButtonVM = viewModel.getQuickAccess(item);
            DirectoryButtonController button = new DirectoryButtonController(directoryButtonVM, folderImage);
            quickAccessList.getChildren().add(button);
        }
    }

    /**
     * Adds the drive buttons to the list view.
     */
    private void addDriveButtons() {
        String drivePath = "/com/candle/fileexplorer/images/16/Drive.png";
        Image driveImage = new Image(drivePath);

        viewModel.getDriveVms().forEach(vm -> quickAccessList.getChildren().add(new DirectoryButtonController(vm, driveImage)));
    }

    /**
     * Sets up the style sheet for this component.
     */
    private void setupAppearance() {
        this.getStyleClass().add("scroll-pane");
    }

    //endregion
}
