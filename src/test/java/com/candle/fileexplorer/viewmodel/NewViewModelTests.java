package com.candle.fileexplorer.viewmodel;

import com.candle.fileexplorer.model.FilesModel;
import com.candle.fileexplorer.model.data.FileType;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class NewViewModelTests {
    @Test
    public void createItem_shouldDoNothing_whenNameIsEmpty() {
        FilesModel dataModel = mock(FilesModel.class);
        NewFileViewModel viewModel = new NewFileViewModel(dataModel);

        FileType type = FileType.File;

        viewModel.setType(type);
        viewModel.createItem();

        verify(dataModel, never()).createItem(type, null);
    }

    @Test
    public void createItem_shouldDoNothing_ifTypeIsDrive() {
        FilesModel dataModel = mock(FilesModel.class);
        NewFileViewModel viewModel = new NewFileViewModel(dataModel);

        FileType type = FileType.Drive;
        String fileName = "DriveItem";

        viewModel.setType(type);
        viewModel.itemNameProperty().setValue(fileName);
        viewModel.createItem();

        verify(dataModel, never()).createItem(type, fileName);
    }

    @Test
    public void createItem_shouldCreateFile_WhenSetToFile() {
        FilesModel dataModel = mock(FilesModel.class);
        NewFileViewModel viewModel = new NewFileViewModel(dataModel);

        FileType type = FileType.File;
        String fileName = "Item.txt";

        viewModel.setType(type);
        viewModel.itemNameProperty().setValue(fileName);
        viewModel.createItem();

        verify(dataModel).createItem(type, fileName);
    }

    @Test
    public void createItem_shouldCreateFolder_WhenSetToFolder() {
        FilesModel dataModel = mock(FilesModel.class);
        NewFileViewModel viewModel = new NewFileViewModel(dataModel);

        FileType type = FileType.Folder;
        String folderName = "Documents";

        viewModel.setType(type);
        viewModel.itemNameProperty().setValue(folderName);
        viewModel.createItem();

        verify(dataModel).createItem(type, folderName);
    }
}
