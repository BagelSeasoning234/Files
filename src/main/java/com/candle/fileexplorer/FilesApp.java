package com.candle.fileexplorer;

import com.candle.fileexplorer.core.ModelFactory;
import com.candle.fileexplorer.core.ViewHandler;
import com.candle.fileexplorer.core.ViewModelFactory;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The 'main' class
 */
public class FilesApp extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // Create the management classes
        ModelFactory modelFactory = new ModelFactory();
        ViewModelFactory viewModelFactory = new ViewModelFactory(modelFactory);
        ViewHandler viewHandler = new ViewHandler(primaryStage, viewModelFactory);
        viewHandler.start();    // Things get started here.
    }
}
