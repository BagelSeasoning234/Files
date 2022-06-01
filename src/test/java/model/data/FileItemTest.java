package model.data;

import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.data.FileItemManager;
import com.candle.fileexplorer.model.data.FileType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FileItemTest
{
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
}
