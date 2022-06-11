package com.candle.fileexplorer.core;

import com.candle.fileexplorer.viewmodel.FileGridViewModel;
import com.candle.fileexplorer.viewmodel.MainViewModel;
import com.candle.fileexplorer.viewmodel.QuickAccessViewModel;

/**
 * The class that is responsible for creating the view models.
 */
public class ViewModelFactory {
    //region Private Members

    /**
     * A reference to the main window's view model.
     */
    private MainViewModel mainViewModel;

    //endregion

    /**
     * Instantiates the view models.
     *
     * @param modelFactory A reference to the model factory.
     */
    public ViewModelFactory(ModelFactory modelFactory) {
        FileGridViewModel fileGrid = new FileGridViewModel(modelFactory.getFilesModel());
        QuickAccessViewModel quickAccess = new QuickAccessViewModel(modelFactory.getFilesModel());
        mainViewModel = new MainViewModel(fileGrid, quickAccess, modelFactory.getFilesModel());
    }

    //region Public Methods

    public MainViewModel getMainViewModel() {
        return mainViewModel;
    }

    //endregion
}
