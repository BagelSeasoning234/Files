package com.candle.fileexplorertest.model;

import com.candle.fileexplorer.model.DefaultFilesModel;
import com.candle.fileexplorer.model.FilesModel;
import com.candle.fileexplorer.model.observer.DataListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import static org.mockito.Mockito.*;

public class FilesModelTests {
    Path tempFile;
    @TempDir
    Path tempFolder;

    @BeforeEach
    public void setUp() {
        try {
            tempFile = tempFolder.resolve("testFile.txt");
        } catch (InvalidPathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void currentDirectory_shouldReturnUserHomeDirectory_byDefault() {
        FilesModel model = new DefaultFilesModel();
        Assertions.assertEquals(System.getProperty("user.home"), model.getCurrentDirectory());
    }

    @Test
    public void currentDirectory_shouldReturnFolder_whenSetToFolder() {
        FilesModel model = new DefaultFilesModel();
        model.setCurrentDirectory(tempFolder.toString());
        Assertions.assertEquals(tempFolder.toString(), model.getCurrentDirectory());
    }

    @Test
    public void currentDirectory_shouldNotBeSet_whenSetToFile() {
        FilesModel model = new DefaultFilesModel();
        model.setCurrentDirectory(tempFile.toString());
        Assertions.assertEquals(System.getProperty("user.home"), model.getCurrentDirectory());
    }

    @Test
    public void addListener_shouldNotifyObject_whenDirectoryChanged() {
        FilesModel model = new DefaultFilesModel();
        DataListener mockListener = mock(DataListener.class);
        model.addListener(mockListener);

        model.setCurrentDirectory(tempFolder.toString());
        verify(mockListener).currentDirectoryChanged();
    }

    @Test
    public void goBackward_shouldReturnToPreviousDir_afterDirChanged()
    {
        FilesModel model = new DefaultFilesModel();
        model.setCurrentDirectory(tempFolder.toString());
        model.goBackwardInDirectoryHistory();
        Assertions.assertEquals(model.getCurrentDirectory(), System.getProperty("user.home"));
    }

    @Test
    public void goForward_shouldReturnToNextDir_afterGoingBack()
    {
        FilesModel model = new DefaultFilesModel();
        model.setCurrentDirectory(tempFolder.toString());
        model.goBackwardInDirectoryHistory();
        model.goForwardInDirectoryHistory();
        Assertions.assertEquals(model.getCurrentDirectory(), tempFolder.toString());
    }
}
