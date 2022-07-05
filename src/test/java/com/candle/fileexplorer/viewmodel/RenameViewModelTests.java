package com.candle.fileexplorer.viewmodel;

import com.candle.fileexplorer.model.FilesModel;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class RenameViewModelTests {
    @Test
    public void renameItem_shouldCallMethod_ifPropertiesAreSet() {
        FilesModel dataModel = mock(FilesModel.class);
        RenameViewModel viewModel = new RenameViewModel(dataModel);

        String itemPath = System.getProperty("user.home") + "/Pictures";
        String newName = "SuperPictures";

        viewModel.setItemPath(itemPath);
        viewModel.itemNameProperty().setValue(newName);

        viewModel.renameItem();
        verify(dataModel).renameItem(itemPath, newName);
    }

    @Test
    public void renameItem_shouldDoNothing_ifPathIsEmpty() {
        FilesModel dataModel = mock(FilesModel.class);
        RenameViewModel viewModel = new RenameViewModel(dataModel);

        String itemPath = "";
        String newName = "NewFile";

        viewModel.setItemPath(itemPath);
        viewModel.itemNameProperty().setValue(newName);

        viewModel.renameItem();
        verify(dataModel, never()).renameItem(itemPath, newName);
    }

    @Test
    public void renameItem_shouldDoNothing_ifNameIsEmpty() {
        FilesModel dataModel = mock(FilesModel.class);
        RenameViewModel viewModel = new RenameViewModel(dataModel);

        String itemPath = System.getProperty("user.home") + "/Pictures";
        String newName = "";

        viewModel.setItemPath(itemPath);
        viewModel.itemNameProperty().setValue(newName);

        viewModel.renameItem();
        verify(dataModel, never()).renameItem(itemPath, newName);
    }

    @Test
    public void renameItem_shouldDoNothing_ifNameHasSlash() {
        FilesModel dataModel = mock(FilesModel.class);
        RenameViewModel viewModel = new RenameViewModel(dataModel);

        String itemPath = System.getProperty("user.home") + "/Pictures";
        String newName = "Super/Pictures";

        viewModel.setItemPath(itemPath);
        viewModel.itemNameProperty().setValue(newName);

        viewModel.renameItem();
        verify(dataModel, never()).renameItem(itemPath, newName);
    }
}
