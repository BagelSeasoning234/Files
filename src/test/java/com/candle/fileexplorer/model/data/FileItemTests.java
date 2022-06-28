package com.candle.fileexplorer.model.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class FileItemTests {
    Path tempFilePath;
    @TempDir
    Path tempFolderPath;

    String tempFile;
    String tempFolder;

    @BeforeEach
    public void setUp() {
        try {
            tempFilePath = tempFolderPath.resolve("testFile.txt");
        } catch (InvalidPathException e) {
            e.printStackTrace();
        }

        tempFile = tempFilePath.toAbsolutePath().toString();
        tempFolder = tempFolderPath.toAbsolutePath().toString();
    }

    @Test
    public void writeToDisk_shouldCreateFile_givenFileType() {
        String newFile = "file.txt";
        String filePath = tempFolderPath.resolve(newFile).toAbsolutePath().toString();
        FileItem fileItem = new DefaultFileItem(filePath);
        fileItem.writeToDisk();

        File testObject = new File(tempFolder + "/" + newFile);
        Assertions.assertTrue(testObject.exists() && testObject.isFile());
    }

    @Test
    public void writeToDisk_shouldCreateFolder_givenFolderType() {
        String newFolder = "folder";
        String folderLocation = tempFolder + "/" + newFolder;

        FileItem folderItem = new DefaultFileItem(FileType.Folder, folderLocation);
        folderItem.writeToDisk();

        File testObject = new File(folderLocation);
        Assertions.assertTrue(testObject.exists() && testObject.isDirectory());
    }

    @Test
    public void writeToDisk_shouldDoNothing_givenDriveType() {

    }

    @Test
    public void getFileName_shouldReturnFolderName_whenSetToFolder() {
        FileItem folder = new DefaultFileItem(tempFolder);
        Assertions.assertEquals(folder.getFileName(), tempFolderPath.getFileName().toString());
    }

    @Test
    public void getFileName_shouldReturnFileName_whenSetToFile() {
        FileItem file = new DefaultFileItem(tempFile);
        Assertions.assertEquals(file.getFileName(), tempFilePath.getFileName().toString());
    }

    @Test
    public void getItemDirectory_shouldReturnHome_whenSetToTilda() {
        FileItem folder = new DefaultFileItem("~");
        Assertions.assertEquals(folder.getItemDirectory(), System.getProperty("user.home"));
    }

    @Test
    public void getItemDirectory_shouldReturnFolderPath_whenSetToFolderPath() {
        FileItem folder = new DefaultFileItem(tempFolder);
        Assertions.assertEquals(folder.getItemDirectory(), tempFolder);
    }

    @Test
    public void getItemDirectory_shouldReturnFilePath_whenSetToFilePath() {
        FileItem file = new DefaultFileItem(tempFile);
        Assertions.assertEquals(file.getItemDirectory(), tempFile);
    }

    @Test
    public void getFileType_shouldReturnFolderType_whenSetToFolder() {
        FileItem folder = new DefaultFileItem(tempFolder);
        Assertions.assertEquals(folder.getFileType(), FileType.Folder);
    }

    @Test
    public void getFileType_shouldReturnFileType_whenSetToFile() {
        FileItem folder = new DefaultFileItem(tempFile);
        Assertions.assertEquals(folder.getFileType(), FileType.File);
    }

    @Test
    public void getFileType_shouldReturnDriveType_whenSetToDrive() {
        FileItem drive = new DefaultFileItem(FileType.Drive, tempFolder);
        Assertions.assertEquals(drive.getFileType(), FileType.Drive);
    }

    @Test
    public void isHiddenFile_shouldReturnTrue_onHiddenFolder() {
        String hidddenFilePath = tempFolderPath.resolve(".hiddenTestFolder").toAbsolutePath().toString();
        FileItem hiddenFolder = new DefaultFileItem(hidddenFilePath);
        Assertions.assertTrue(hiddenFolder.getIsHiddenFile());
    }

    @Test
    public void isHiddenFile_shouldReturnTrue_onHiddenFile() {
        String hidddenFilePath = tempFolderPath.resolve(".hiddenTestFile.txt").toAbsolutePath().toString();
        FileItem hiddenFile = new DefaultFileItem(hidddenFilePath);
        Assertions.assertTrue(hiddenFile.getIsHiddenFile());
    }

    @Test
    public void isHiddenFile_shouldReturnFalse_onVisibleFolder() {
        FileItem visibleFolder = new DefaultFileItem(tempFolder);
        Assertions.assertFalse(visibleFolder.getIsHiddenFile());
    }

    @Test
    public void isHiddenFile_shouldReturnFalse_onVisibleFile() {
        FileItem visibleFile = new DefaultFileItem(tempFile);
        Assertions.assertFalse(visibleFile.getIsHiddenFile());
    }

    @Test
    public void getLastModified_shouldReturnTime_whenCalledOnFolder() {
        FileItem folder = new DefaultFileItem(tempFolder);
        Assertions.assertEquals(folder.getLastModifiedTime(), new File(tempFolder).lastModified());
    }

    @Test
    public void getLastModified_shouldReturnTime_whenCalledOnFile() {
        FileItem file = new DefaultFileItem(tempFile);
        Assertions.assertEquals(file.getLastModifiedTime(), new File(tempFile).lastModified());
    }
}
