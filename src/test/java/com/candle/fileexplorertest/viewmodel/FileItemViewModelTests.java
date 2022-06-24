package com.candle.fileexplorertest.viewmodel;

import com.candle.fileexplorer.model.data.FileItem;
import com.candle.fileexplorer.model.data.FileType;
import com.candle.fileexplorer.viewmodel.FileItemViewModel;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class FileItemViewModelTests {

    @Start
    public void start(Stage stage) {
        Parent sceneRoot = new StackPane();
        Scene scene = new Scene(sceneRoot);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

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
        Assertions.assertTrue(isImageEqual(vm.getImagePreview(), folderImage));
    }

    // Helper Method
    private boolean isImageEqual(Image firstImage, Image secondImage){
        // Prevent `NullPointerException`
        if(firstImage != null && secondImage == null) return false;
        if(firstImage == null) return secondImage == null;

        // Compare images size
        if(firstImage.getWidth() != secondImage.getWidth()) return false;
        if(firstImage.getHeight() != secondImage.getHeight()) return false;

        // Compare images color
        for(int x = 0; x < firstImage.getWidth(); x++){
            for(int y = 0; y < firstImage.getHeight(); y++){
                int firstArgb = firstImage.getPixelReader().getArgb(x, y);
                int secondArgb = secondImage.getPixelReader().getArgb(x, y);

                if(firstArgb != secondArgb) return false;
            }
        }

        return true;
    }
}
