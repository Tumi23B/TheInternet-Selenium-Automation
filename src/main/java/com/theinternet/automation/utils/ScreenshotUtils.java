package com.theinternet.automation.utils;

import com.theinternet.automation.constants.FrameworkConstants;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Provides reusable screenshot functionality for the automation framework.
 *
 * Screenshots are captured for every executed test and stored in the
 * framework screenshots directory. The returned Base64 representation
 * can also be embedded directly into the HTML execution report.
 */
public final class ScreenshotUtils {

    private ScreenshotUtils() {
        // Prevent instantiation.
    }

    /**
     * Captures a screenshot and saves it to the screenshots directory.
     *
     * @param driver   active WebDriver instance
     * @param fileName name of the screenshot file without extension
     * @return Base64 encoded screenshot for HTML report embedding
     */
    public static String captureScreenshot(
            WebDriver driver,
            String fileName) {

        if (driver == null) {
            throw new IllegalArgumentException(
                    "WebDriver cannot be null."
            );
        }

        try {
            Path screenshotDirectory =
                    Path.of(
                            FrameworkConstants.SCREENSHOTS_DIRECTORY
                    );

            Files.createDirectories(screenshotDirectory);

            byte[] screenshotBytes =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.BYTES);

            Path screenshotPath =
                    screenshotDirectory.resolve(
                            fileName + ".png"
                    );

            Files.write(
                    screenshotPath,
                    screenshotBytes
            );

            return java.util.Base64.getEncoder()
                    .encodeToString(screenshotBytes);

        } catch (IOException exception) {

            throw new RuntimeException(
                    "Unable to save screenshot: " + fileName,
                    exception
            );
        }
    }
}