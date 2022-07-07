package com.candle.fileexplorer.core;

import com.candle.fileexplorer.viewmodel.*;

/**
 * The class that is responsible for creating the view models.
 */
public class ViewModelFactory {
    //region Private Members

    private final MainViewModel mainViewModel;
    private final NewFileViewModel newFileViewModel;
    private final RenameViewModel renameViewModel;
    private AboutViewModel aboutViewModel;

    //endregion

    //region Constructors

    /**
     * Instantiates the view models.
     *
     * @param modelFactory A reference to the model factory.
     */
    public ViewModelFactory(ModelFactory modelFactory) {
        FileGridViewModel fileGrid =
                new FileGridViewModel(modelFactory.getFilesModel());
        QuickAccessViewModel quickAccess =
                new QuickAccessViewModel(modelFactory.getFilesModel());
        mainViewModel = new MainViewModel(fileGrid, quickAccess,
                modelFactory.getFilesModel());

        newFileViewModel = new NewFileViewModel(modelFactory.getFilesModel());
        renameViewModel = new RenameViewModel(modelFactory.getFilesModel());
    }

    //endregion

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

    public RenameViewModel getRenameViewModel() {
        return renameViewModel;
    }

    //endregion
}
