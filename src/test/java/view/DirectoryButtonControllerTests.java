package view;

import com.candle.fileexplorer.view.DirectoryButtonController;
import com.candle.fileexplorer.viewmodel.DirectoryButtonViewModel;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import static org.mockito.Mockito.verify;


import static org.mockito.Mockito.mock;

public class DirectoryButtonControllerTests extends ApplicationTest {

    DirectoryButtonController testButton;
    DirectoryButtonViewModel viewModel;
    Image image;

    @Override
    public void start(Stage stage) throws Exception {
        viewModel = mock(DirectoryButtonViewModel.class);
        image = new Image("/com/candle/fileexplorer/images/16/Folder.png");
        testButton = new DirectoryButtonController(viewModel, image);

        Parent sceneRoot = new StackPane(testButton);
        Scene scene = new Scene(sceneRoot);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void goToDirectory_shouldTellViewModel_whenButtonClicked() {
        clickOn(testButton);

        verify(viewModel).goToDirectory();
    }
}
