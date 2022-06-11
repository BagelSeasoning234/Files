package viewmodel;

import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.data.FileType;
import com.candle.fileexplorer.viewmodel.FileItemViewModel;
import javafx.scene.image.Image;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class FileItemViewModelTests {
    @Test
    public void updateFile_shouldUpdateFileName_whenSetToItem() {
        FileItem mockItem = mock(FileItem.class);
        String fileItemName = "Pictures";
        when(mockItem.getFileName()).thenReturn(fileItemName);
        when(mockItem.getFileType()).thenReturn(FileType.Folder);

        FileItemViewModel vm = new FileItemViewModel(mockItem);
        Assertions.assertEquals(vm.getFileName(), fileItemName);
    }

    @Test
    public void updateFile_shouldReturnCorrectImage_whenSetToItem() {
        FileItem mockItem = mock(FileItem.class);
        Image folderImage = new Image("/com/candle/fileexplorer/images/64/Folder.png");
        when(mockItem.getFileName()).thenReturn("Pictures");
        when(mockItem.getFileType()).thenReturn(FileType.Folder);

        FileItemViewModel vm = new FileItemViewModel(mockItem);
        Assertions.assertEquals(vm.getImagePreview(), folderImage);
    }
}
