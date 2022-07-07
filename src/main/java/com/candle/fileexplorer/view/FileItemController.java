package com.candle.fileexplorer.view;

import com.candle.fileexplorer.model.data.FileItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;

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

    private FileItem fileItem;

    //endregion

    //region Public Methods

    /**
     * Initializes the view for this item using the given file item.
     */
    public void init(FileItem fileItem, Image image) {
        this.fileItem = fileItem;

        // Bind data
        fileName.textProperty().setValue(fileItem.getFileName());
        filePreview.imageProperty().setValue(image);

        this.getStyleClass().add("vbox");
    }

    public String getItemDirectory() {
        return fileItem.getItemDirectory();
    }

    //endregion
}
