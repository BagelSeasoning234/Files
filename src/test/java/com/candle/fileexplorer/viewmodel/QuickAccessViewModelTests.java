package com.candle.fileexplorer.viewmodel;

import com.candle.fileexplorer.model.DefaultFilesModel;
import com.candle.fileexplorer.model.FilesModel;
import com.candle.fileexplorer.model.data.DefaultFileItem;
import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.data.FileType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuickAccessViewModelTests {
    @Test
    public void getDrives_shouldReturnDrives_whenCalled() {
        ArrayList<FileItem> drives = new ArrayList<>();
        FileItem drive1 = new DefaultFileItem(FileType.Drive, "drive1");
        FileItem drive2 = new DefaultFileItem(FileType.Drive, "drive2");
        FileItem drive3 = new DefaultFileItem(FileType.Drive, "drive3");

        drives.add(drive1);
        drives.add(drive2);
        drives.add(drive3);

        FilesModel dataModel = mock(FilesModel.class);
        when(dataModel.getDrives()).thenReturn(drives);

        ArrayList<DirectoryButtonViewModel> expectedResult = new ArrayList<>();
        expectedResult.add(new DirectoryButtonViewModel(dataModel, drive1));
        expectedResult.add(new DirectoryButtonViewModel(dataModel, drive2));
        expectedResult.add(new DirectoryButtonViewModel(dataModel, drive3));

        QuickAccessViewModel viewModel = new QuickAccessViewModel(dataModel);

        Assertions.assertEquals(expectedResult, viewModel.getDriveVms());
    }

    @Test
    public void getQuickAccess_shouldReturnHome_inHomePath() {
        FilesModel dataModel = new DefaultFilesModel();
        String homeDirectory = System.getProperty("user.home");
        FileItem homeItem = new DefaultFileItem(homeDirectory);
        DirectoryButtonViewModel expectedResult =
                new DirectoryButtonViewModel(dataModel, homeItem);

        QuickAccessViewModel viewModel = new QuickAccessViewModel(dataModel);

        Assertions.assertEquals(expectedResult,
                viewModel.getQuickAccessButtonViewModel("Home"));
    }
}
