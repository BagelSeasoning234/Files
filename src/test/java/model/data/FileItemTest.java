package model.data;

import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.data.FileItemManager;
import com.candle.fileexplorer.model.data.FileType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileItemTest
{
    @Test
    public void testFileItemName()
    {
        String folder = "~/Pictures";
        String file = "~/Productivity/Development/Java/FileExplorer/src/test/java/model/data/FileItemTest.java";

        FileItem folderItem = new FileItemManager(folder);
        Assertions.assertEquals(folderItem.getFileName(), "Pictures");

        FileItem fileItem = new FileItemManager(file);
        Assertions.assertEquals(fileItem.getFileName(), "FileItemTest.java");
    }

    @Test
    public void testFileItemDirectory()
    {
        String folder = "~/Pictures";
        String absoluteFolder = "/home/cachandler/Pictures";
        String file = "~/Productivity/Development/Java/FileExplorer/src/test/java/model/data/FileItemTest.java";
        String absoluteFile = "/home/cachandler/Productivity/Development/Java/FileExplorer/src/test/java/model/data/FileItemTest.java";

        FileItem folderItem = new FileItemManager(folder);
        Assertions.assertEquals(folderItem.getItemDirectory(), absoluteFolder);

        FileItem absoluteFolderItem = new FileItemManager(absoluteFolder);
        Assertions.assertEquals(absoluteFolderItem.getItemDirectory(), absoluteFolder);

        FileItem fileItem = new FileItemManager(file);
        Assertions.assertEquals(fileItem.getItemDirectory(), absoluteFile);

        FileItem absoluteFileItem = new FileItemManager(absoluteFile);
        Assertions.assertEquals(absoluteFileItem.getItemDirectory(), absoluteFile);
    }

    @Test
    public void testFileItemType()
    {
        String filePath = "~/Productivity/Development/Java/FileExplorer/src/test/java/model/data/FileItemTest.java";
        String folderPath = "~/Productivity/Development/Java/FileExplorer/src/test";
        String drivePath = "/";

        FileItem autoTypeFile = new FileItemManager(filePath);
        Assertions.assertEquals(FileType.File, autoTypeFile.getFileType());

        FileItem autoTypeFolder = new FileItemManager(folderPath);
        Assertions.assertEquals(FileType.Folder, autoTypeFolder.getFileType());

        FileItem driveType = new FileItemManager(FileType.Drive, drivePath);
        Assertions.assertEquals(FileType.Drive, driveType.getFileType());
    }

    @Test
    public void testFileItemHidden()
    {
        String hiddenFolder = "~/.local";
        String hiddenFile = "~/.bashrc";
        String visibleFolder = "~/Pictures";
        String visibleFile = "~/Productivity/Development/Java/FileExplorer/src/test/java/model/data/FileItemTest.java";

        FileItem hiddenFolderItem = new FileItemManager(hiddenFolder);
        Assertions.assertTrue(hiddenFolderItem.getIsHiddenFile());

        FileItem hiddenFileItem = new FileItemManager(hiddenFile);
        Assertions.assertTrue(hiddenFileItem.getIsHiddenFile());

        FileItem visibleFolderItem = new FileItemManager(visibleFolder);
        FileItem visibleFileItem = new FileItemManager(visibleFile);
    }

    @Test
    public void testFileItemSize()
    {
        String folder = "~/Pictures/KDE";
        String file = "~/Pictures/KDE/Kde Plasma Glass.png";

        long expectedFileResult = 29540870;
        FileItem fileItem = new FileItemManager(file);
        Assertions.assertEquals(expectedFileResult, fileItem.getFileSize());

        long expectedFolderResult = 29540870;
        FileItem folderItem = new FileItemManager(folder);
        Assertions.assertEquals(expectedFolderResult, folderItem.getFileSize());
    }

    @Test
    public void testFileItemModifiedTime()
    {
        String folder = "home/cachandler/Pictures/KDE";
        String file = "home/cachandler/Pictures/KDE/Kde Plasma Glass.png";

        FileItem folderItem = new FileItemManager(folder);
        long folderTime = new File(folder).lastModified();
        Assertions.assertEquals(folderTime, folderItem.getLastModifiedTime());

        FileItem fileItem = new FileItemManager(file);
        long fileTime = new File(file).lastModified();
        Assertions.assertEquals(fileTime, fileItem.getLastModifiedTime());
    }
}
