package com.candle.fileexplorer.viewmodel;

import com.candle.fileexplorer.model.FilesModel;
import com.candle.fileexplorer.model.data.FileItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.mockito.Mockito.*;

public class DirectoryButtonViewModelTests {
    @Test
    public void getDirectoryName_shouldReturnHome_InHomeFolder() {
        FilesModel mockModel = mock(FilesModel.class);
        FileItem mockItem = mock(FileItem.class);
        File file = new File(System.getProperty("user.home"));

        when(mockItem.getFileName()).thenReturn(file.getName());

        DirectoryButtonViewModel vm = new DirectoryButtonViewModel(mockModel,
                mockItem);
        Assertions.assertEquals(vm.getDirectoryName(), "Home");
    }

    @Test
    public void getDirectoryName_shouldReturnName_InFolder() {
        String folderName = "testFolder";

        FilesModel mockModel = mock(FilesModel.class);
        FileItem mockItem = mock(FileItem.class);
        when(mockItem.getFileName()).thenReturn(folderName);

        DirectoryButtonViewModel vm = new DirectoryButtonViewModel(mockModel,
                mockItem);
        Assertions.assertEquals(vm.getDirectoryName(), folderName);
    }

    @Test
    public void goToDirectory_shouldSetDataDirectory_whenCalled() {
        FilesModel mockModel = mock(FilesModel.class);
        FileItem mockItem = mock(FileItem.class);

        String newDirectory = System.getProperty("user.home") + "/Pictures";
        when(mockItem.getItemDirectory()).thenReturn(newDirectory);

        DirectoryButtonViewModel vm = new DirectoryButtonViewModel(mockModel,
                mockItem);
        vm.goToDirectory();
        verify(mockModel).setCurrentDirectory(newDirectory);
    }
}
