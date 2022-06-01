package model.helpers;

import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.data.FileItemManager;
import com.candle.fileexplorer.model.data.FileType;
import com.candle.fileexplorer.model.helpers.DirectoryStructure;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class FilesStructureTest
{
    @Test
    public void testGetDrives()
    {
        FileItem drive1 = new FileItemManager(FileType.Drive, "/");
        FileItem drive2 = new FileItemManager(FileType.Drive, "/run/media/cachandler/Local Disk");
        ArrayList<FileItem> expectedResult = new ArrayList<>();
        expectedResult.add(drive1);
        expectedResult.add(drive2);

        Assertions.assertArrayEquals(expectedResult.toArray(), DirectoryStructure.getDrives().toArray());
    }

    @Test
    public void testGetCurrentFilesAndFolders() throws IOException
    {
        String path = "~/Productivity/Development/Java/FileExplorer/src/test";
        ArrayList<FileItem> expectedResult = new ArrayList<>();
        expectedResult.add(new FileItemManager(FileType.Folder, "/home/cachandler/Productivity/Development/Java/FileExplorer/src/test/java"));

        Assertions.assertArrayEquals(expectedResult.toArray(), DirectoryStructure.getDirectoryContents(path, false).toArray());
    }
}
