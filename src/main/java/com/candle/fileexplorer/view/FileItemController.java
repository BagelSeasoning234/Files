package com.candle.fileexplorer.view;

import com.candle.fileexplorer.viewmodel.FileItemViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * The view class for the custom File Item component.
 */
public class FileItemController extends VBox {
    //region Private Members

    /**
     * The name of the file.
     */
    @FXML
    private Label fileName;

    /**
     * The image preview of the file
     */
    @FXML
    private ImageView filePreview;

    private FileItemViewModel viewModel;

    //endregion

    //region Public Methods

    /**
     * Initializes the view for this item by binding properties to information on the view model.
     */
    public void init(FileItemViewModel viewModel) {
        this.viewModel = viewModel;

        // Bind data
        fileName.textProperty().bind(viewModel.fileNameProperty());
        filePreview.imageProperty().bind(viewModel.imagePreviewProperty());

        this.getStyleClass().add("vbox");
    }

    public String getItemDirectory() {
        return viewModel.getItemDirectory();
    }

    //endregion
}
