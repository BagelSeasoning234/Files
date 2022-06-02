package model;

import com.candle.fileexplorer.model.FilesModel;
import com.candle.fileexplorer.model.FilesModelManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FilesModelTest
{
    @Test
    public void testCurrentDirectory()
    {
        String directory = "~/Productivity/Development/Java/FileExplorer";

        FilesModel model = new FilesModelManager();
        Assertions.assertEquals("/home/cachandler", model.getCurrentDirectory());

        model.setCurrentDirectory("/");
        Assertions.assertEquals("/", model.getCurrentDirectory());

        String fileThatCannotBeADirectory = "~/Productivity/Development/Java/FileExplorer/src/test/java/model/FilesModelTest.java";
        model.setCurrentDirectory(fileThatCannotBeADirectory);
        Assertions.assertEquals("/", model.getCurrentDirectory());
    }

    @Test
    public void testNotifier()
    {
        FilesModel model = new FilesModelManager();
        DummyListener listener = new DummyListener();

        Assertions.assertFalse(listener.getHasBeenNotified());

        model.addListener(listener);
        model.setCurrentDirectory("~/Pictures");

        Assertions.assertTrue(listener.getHasBeenNotified());
    }
}
