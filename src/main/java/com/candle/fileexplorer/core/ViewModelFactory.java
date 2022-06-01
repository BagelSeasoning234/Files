package com.candle.fileexplorer.core;

import com.candle.fileexplorer.viewmodel.FileGridViewModel;
import com.candle.fileexplorer.viewmodel.MainViewModel;

/**
 * The class that is responsible for creating the view models.
 */
public class ViewModelFactory
{
    //region Private Members

    /**
     * A reference to the main window's view model.
     */
    private MainViewModel mainViewModel;

    //endregion

    /**
     * Instantiates the view models.
     * @param modelFactory A reference to the model factory.
     */
    public ViewModelFactory(ModelFactory modelFactory)
    {
        FileGridViewModel fileStructure = new FileGridViewModel(modelFactory.getFilesModel());
        mainViewModel = new MainViewModel(fileStructure, modelFactory.getFilesModel());
    }

    //region Public Methods

    public MainViewModel getMainViewModel() { return mainViewModel; }

    //endregion
}
