package com.candle.fileexplorer.model;

import com.candle.fileexplorer.model.data.ClipboardMode;
import com.candle.fileexplorer.model.data.FileType;
import com.candle.fileexplorer.model.observer.DataListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
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
    public void createItem_shouldCreateFile_whenTypeIsFile() {
        FilesModel dataModel = new DefaultFilesModel();
        dataModel.addTab();
        String fileName = "file.txt";

        dataModel.setCurrentDirectory(tempFolder.toAbsolutePath().toString());
        dataModel.createItem(FileType.File, fileName);

        File fileObject =
                new File(dataModel.getCurrentDirectory() + "/" + fileName);
        Assertions.assertTrue(fileObject.exists() && fileObject.isFile());
    }

    @Test
    public void createItem_shouldCreateFolder_whenTypeIsFolder() {
        FilesModel dataModel = new DefaultFilesModel();
        dataModel.addTab();
        String folderName = "folder";

        dataModel.setCurrentDirectory(tempFolder.toAbsolutePath().toString());
        dataModel.createItem(FileType.Folder, folderName);

        File folderObject =
                new File(dataModel.getCurrentDirectory() + "/" + folderName);
        Assertions.assertTrue(folderObject.exists() && folderObject.isDirectory());
    }

    @Test
    public void createItem_shouldNotifyListeners_afterWritingToDisk() {
        DataListener listener = mock(DataListener.class);
        FilesModel dataModel = new DefaultFilesModel();
        dataModel.addTab();
        dataModel.addListener(listener);

        String fileName = "file.png";
        dataModel.setCurrentDirectory(tempFolder.toAbsolutePath().toString());
        dataModel.createItem(FileType.File, fileName);

        verify(listener, times(2)).currentDirectoryChanged();
    }

    @Test
    public void createItem_shouldNotNotifyListeners_IfTypeIsDrive() {
        DataListener listener = mock(DataListener.class);
        FilesModel dataModel = new DefaultFilesModel();
        dataModel.addTab();
        dataModel.addListener(listener);

        String fileName = "driveObject";
        dataModel.setCurrentDirectory(tempFolder.toAbsolutePath().toString());
        dataModel.createItem(FileType.Drive, fileName);

        verify(listener, times(1)).currentDirectoryChanged();
    }

    @Test
    public void addTab_shouldUpdateTabIndex_afterCreatingNewTab() {
        FilesModel dataModel = new DefaultFilesModel();
        dataModel.addTab();
        int expectedIndex = dataModel.getTabIndex() + 1;
        dataModel.addTab();
        Assertions.assertEquals(expectedIndex, dataModel.getTabIndex());
    }

    @Test
    public void removeTab_shouldSetTabIndexToZero_ifTabWasDeleted() {
        FilesModel dataModel = new DefaultFilesModel();
        dataModel.addTab();
        int firstIndex = dataModel.getTabIndex();
        dataModel.addTab();
        dataModel.removeTab(firstIndex + 1);
        Assertions.assertEquals(firstIndex, dataModel.getTabIndex());
    }

    @Test
    public void removeTab_shouldNotSetTabIndexToZero_ifTabWasDifferent() {
        FilesModel dataModel = new DefaultFilesModel();
        dataModel.addTab();
        int firstIndex = dataModel.getTabIndex();
        dataModel.addTab();
        dataModel.addTab();

        dataModel.removeTab(firstIndex + 1);
        Assertions.assertEquals(firstIndex + 1, dataModel.getTabIndex());
    }

    @Test
    public void currentDirectory_shouldReturnUserHomeDirectory_byDefault() {
        FilesModel model = new DefaultFilesModel();
        model.addTab();
        Assertions.assertEquals(System.getProperty("user.home"),
                model.getCurrentDirectory());
    }

    @Test
    public void currentDirectory_shouldReturnFolder_whenSetToFolder() {
        FilesModel model = new DefaultFilesModel();
        model.addTab();
        model.setCurrentDirectory(tempFolder.toString());
        Assertions.assertEquals(replaceWindowsBackslashes(tempFolder.toString()),
                model.getCurrentDirectory());
    }

    @Test
    public void currentDirectory_shouldNotBeSet_whenSetToFile() {
        FilesModel model = new DefaultFilesModel();
        model.addTab();
        model.setCurrentDirectory(tempFile.toString());
        Assertions.assertEquals(System.getProperty("user.home"),
                model.getCurrentDirectory());
    }

    @Test
    public void addListener_shouldNotifyObject_whenDirectoryChanged() {
        FilesModel model = new DefaultFilesModel();
        model.addTab();
        DataListener mockListener = mock(DataListener.class);
        model.addListener(mockListener);

        model.setCurrentDirectory(tempFolder.toString());
        verify(mockListener).currentDirectoryChanged();
    }

    @Test
    public void goBackward_shouldReturnToPreviousDir_afterDirChanged() {
        FilesModel model = new DefaultFilesModel();
        model.addTab();
        model.setCurrentDirectory(tempFolder.toString());
        model.goBackwardInDirectoryHistory();
        Assertions.assertEquals(System.getProperty("user.home"),
                model.getCurrentDirectory());
    }

    @Test
    public void goForward_shouldReturnToNextDir_afterGoingBack() {
        FilesModel model = new DefaultFilesModel();
        model.addTab();
        model.setCurrentDirectory(tempFolder.toString());
        model.goBackwardInDirectoryHistory();
        model.goForwardInDirectoryHistory();
        Assertions.assertEquals(replaceWindowsBackslashes(tempFolder.toString()),
                model.getCurrentDirectory());
    }

    @Test
    public void clipboardMode_shouldBeCopy_byDefault() {
        FilesModel model = new DefaultFilesModel();
        Assertions.assertEquals(ClipboardMode.Copy, model.getClipboardMode());
    }

    private String replaceWindowsBackslashes(String path) {
        return path.replace("\\", "/");
    }
}
