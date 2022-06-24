package com.candle.fileexplorer.core;

import com.candle.fileexplorer.viewmodel.*;

/**
 * The class that is responsible for creating the view models.
 */
public class ViewModelFactory {
    //region Private Members

    /**
     * A reference to the main window's view model.
     */
    private MainViewModel mainViewModel;

    /**
     * A reference to the about window's view model.
     */
    private AboutViewModel aboutViewModel;

    private NewFileViewModel newFileViewModel;

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

        newFileViewModel = new NewFileViewModel(modelFactory.getFilesModel());
    }

    //region Public Methods

    public MainViewModel getMainViewModel() {
        return mainViewModel;
    }

    public AboutViewModel getAboutViewModel() {
        if (aboutViewModel == null)
            aboutViewModel = new AboutViewModel();
        return aboutViewModel;
    }

    public NewFileViewModel getNewFileViewModel() {
        return newFileViewModel;
    }

    //endregion
}
