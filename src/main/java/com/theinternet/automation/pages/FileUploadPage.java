package com.theinternet.automation.pages;

import com.theinternet.automation.base.BasePage;
import com.theinternet.automation.config.ConfigurationManager;
import com.theinternet.automation.utils.WaitUtils;
import org.openqa.selenium.By;

/**
 * Represents the File Upload page.
 *
 * This page demonstrates uploading a file through Selenium
 * using the browser's file input element.
 */
public class FileUploadPage extends BasePage {

    private final By fileInput =
            By.id("file-upload");

    private final By uploadButton =
            By.id("file-submit");

    private final By uploadedFile =
            By.id("uploaded-files");

    /**
     * Opens the File Upload page.
     */
    public void open() {
        driver.get(
                ConfigurationManager.getConfig("base.url")
                        + "/upload"
        );
    }

    /**
     * Uploads a file through the file input element.
     *
     * Selenium sends the absolute file path directly to the
     * browser's file input, avoiding OS-level file picker automation.
     *
     * @param filePath absolute path of the file to upload
     */
    public void uploadFile(String filePath) {
        WaitUtils.waitForVisibility(
                driver,
                fileInput
        ).sendKeys(filePath);
    }

    /**
     * Submits the selected file.
     */
    public void clickUpload() {
        WaitUtils.waitForClickable(
                driver,
                uploadButton
        ).click();
    }

    /**
     * Returns the name of the uploaded file displayed by the application.
     */
    public String getUploadedFileName() {
        return WaitUtils.waitForVisibility(
                driver,
                uploadedFile
        ).getText();
    }
}