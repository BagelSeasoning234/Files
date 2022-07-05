package com.candle.fileexplorer.model.helpers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FileUtilitiesTests {
    @Test
    public void sanitizePath_shouldReturnValidPath_whenGivenDirtyPath() {
        String dirtyPath = "~/Files/User\\040File.png";
        String expectedResult = System.getProperty("user.home") + "/Files/User File.png";

        Assertions.assertEquals(FileUtilities.sanitizePath(dirtyPath), expectedResult);
    }
}
