package com.candle.fileexplorer.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ModelFactoryTests {
    @Test
    public void getFilesModel_shouldReturnAValidModel_whenCalled() {
        ModelFactory modelFactory = new ModelFactory();
        Assertions.assertNotNull(modelFactory.getFilesModel());
    }
}
