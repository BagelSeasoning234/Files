package com.candle.fileexplorertest.view;

import com.candle.fileexplorer.view.DirectoryButtonController;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;
import com.candle.fileexplorer.viewmodel.DirectoryButtonViewModel;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.mockito.Mockito.verify;

import static org.mockito.Mockito.mock;

@ExtendWith(ApplicationExtension.class)
public class DirectoryButtonControllerTests {

    DirectoryButtonController testButton;
    DirectoryButtonViewModel viewModel;
    Image image;

    @Start
    public void start(Stage stage) {
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
    public void goToDirectory_shouldTellViewModel_whenButtonClicked(FxRobot robot) {
        robot.clickOn(testButton);
        verify(viewModel).goToDirectory();
    }
}
