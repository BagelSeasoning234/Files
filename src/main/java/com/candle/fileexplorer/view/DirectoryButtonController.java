package com.candle.fileexplorer.view;

import com.candle.fileexplorer.viewmodel.DirectoryButtonViewModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The button class for "quick access" directories like Pictures, Desktop, etc.
 */
public class DirectoryButtonController extends Button {
    //region Private Members

    //region GUI Elements

    //endregion

    /**
     * A reference to the view model class that allows this object
     * to set the data model's current directory.
     */
    private final DirectoryButtonViewModel viewModel;

    //endregion

    //region Constructor

    /**
     * Creates a new "quick access" button, given a specified file item and image.
     *
     * @param viewModel The view model that allows this button to interface with the backend data.
     * @param image     The image thumbnail that will be used for this button.
     */
    public DirectoryButtonController(DirectoryButtonViewModel viewModel, Image image) {
        super(viewModel.getDirectoryName(), new ImageView(image));

        this.viewModel = viewModel;
        this.setOnAction(this::goToDirectory);
    }

    //endregion

    //region Private Methods

    /**
     * When clicked, the button will take the user to the directory for this file item.
     */
    private void goToDirectory(ActionEvent actionEvent) {
        viewModel.goToDirectory();
    }

    // TODO: Look into adding the context menu items.

    //endregion
}
