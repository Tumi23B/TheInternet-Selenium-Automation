package com.theinternet.automation.tests;

import com.theinternet.automation.base.BaseTest;
import com.theinternet.automation.config.ConfigurationManager;
import com.theinternet.automation.pages.FileUploadPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Path;

/**
 * Covers the File Upload scenario.
 *
 * The test verifies that a file can be selected through Selenium,
 * submitted successfully, and identified by the application.
 */
public class FileUploadTest extends BaseTest {

    /**
     * Verifies that a test file can be uploaded successfully.
     */
    @Test
    public void shouldUploadFileSuccessfully() {

        FileUploadPage fileUploadPage =
                new FileUploadPage();

        fileUploadPage.open();

        String fileName =
                ConfigurationManager.getTestData("upload.file");

        String filePath =
                Path.of(
                        "test-data",
                        fileName
                ).toAbsolutePath().toString();

        fileUploadPage.uploadFile(filePath);

        fileUploadPage.clickUpload();

        Assert.assertEquals(
                fileUploadPage.getUploadedFileName(),
                fileName,
                "The uploaded file name was not displayed correctly."
        );
    }
}