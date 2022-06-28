package com.candle.fileexplorer.viewmodel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AboutViewModelTests {
    @Test
    public void getFilesVersion_shouldReturnVersion_WhenCalled() {
        // TODO: Make this test future proof. I don't see how to test this except by hard coding the correct result.
        AboutViewModel viewModel = new AboutViewModel();
        Assertions.assertEquals(viewModel.getFilesVersion(), "0.1");
    }

    @Test
    public void getJavaFxVersion_shouldReturnVersion_whenCalled() {
        AboutViewModel viewModel = new AboutViewModel();
        Assertions.assertEquals(viewModel.getJavaFxVersion(), System.getProperty("javafx.runtime.version"));
    }

    @Test
    public void getJavaVersion_shouldReturnVersion_whenCalled() {
        AboutViewModel viewModel = new AboutViewModel();
        Assertions.assertEquals(viewModel.getJavaVersion(), System.getProperty("java.version"));
    }
}
