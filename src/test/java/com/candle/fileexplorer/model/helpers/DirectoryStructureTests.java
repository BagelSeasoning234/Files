package com.candle.fileexplorer.model.helpers;

import com.candle.fileexplorer.model.data.DefaultFileItem;
import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.data.FileType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class DirectoryStructureTests {
    @TempDir
    Path tempFolderPath;

    @Test
    public void getDrives_shouldReturnDrives_whenCalled() {
        // TODO: Make this test work on various computers. I have no clue how to do this right now other than by hardcoding drives.
        //FileItem drive1 = new DefaultFileItem(FileType.Drive, "/");
        //FileItem drive2 = new DefaultFileItem(FileType.Drive, "/run/media/cachandler/SSD 500GB");
        FileItem drive1 = new DefaultFileItem(FileType.Drive, "/");
        FileItem drive2 = new DefaultFileItem(FileType.Drive, "/run/media/cachandler/7cbdceb7-59b5-45b1-82e3-d54ba374c457/");
        FileItem drive3 = new DefaultFileItem(FileType.Drive, "/run/media/cachandler/1CB63A0CB639E744/");

        ArrayList<FileItem> expectedDrives = new ArrayList<>();
        expectedDrives.add(drive1);
        expectedDrives.add(drive2);
        expectedDrives.add(drive3);

        Assertions.assertArrayEquals(expectedDrives.toArray(), DirectoryStructure.getDrives().toArray());
    }

    @Test
    public void getContents_shouldReturnFolderContents_whenFolderHasContents() {
        String subFolderName = "testFolder";
        File subFolder = new File(tempFolderPath.toFile(), subFolderName);
        subFolder.mkdir();

        String subFileName = "testFile.txt";
        File subFile = new File(tempFolderPath.toFile(), subFileName);
        try {
            subFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<FileItem> expectedContents = new ArrayList<>();
        expectedContents.add(new DefaultFileItem(FileType.File, subFile.getAbsolutePath()));
        expectedContents.add(new DefaultFileItem(FileType.Folder, subFolder.getAbsolutePath()));

        ArrayList<FileItem> actualContents = DirectoryStructure.getDirectoryContents(tempFolderPath.toAbsolutePath().toString(), true);
        Assertions.assertArrayEquals(expectedContents.toArray(), actualContents.toArray());
    }

    @Test
    public void getContents_shouldReturnNothing_whenFolderHasNoContents() {
        ArrayList<FileItem> expectedContents = new ArrayList<>();

        ArrayList<FileItem> actualContents = DirectoryStructure.getDirectoryContents(tempFolderPath.toAbsolutePath().toString(), true);
        Assertions.assertArrayEquals(expectedContents.toArray(), actualContents.toArray());
    }

    @Test
    public void getContents_shouldReturnNothing_whenItemIsFile() {
        String subFileName = "testFile.txt";
        File subFile = new File(tempFolderPath.toFile(), subFileName);
        try {
            subFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<FileItem> expectedResult = new ArrayList<>();

        ArrayList<FileItem> actualContents = DirectoryStructure.getDirectoryContents(subFile.getAbsolutePath(), true);
        Assertions.assertArrayEquals(expectedResult.toArray(), actualContents.toArray());
    }

}
