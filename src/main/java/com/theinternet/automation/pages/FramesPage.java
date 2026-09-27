package com.theinternet.automation.pages;

import com.theinternet.automation.base.BasePage;
import com.theinternet.automation.config.ConfigurationManager;
import com.theinternet.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

/**
 * Represents the Frames page.
 *
 * This page demonstrates switching into an iframe,
 * interacting with content inside the frame, and
 * returning to the main document.
 */
public class FramesPage extends BasePage {

    private final By iframe =
            By.id("mce_0_ifr");

    private final By editorBody =
            By.id("tinymce");

    /**
     * Opens the iframe page.
     */
    public void open() {
        driver.get(
                ConfigurationManager.getConfig("base.url")
                        + "/iframe"
        );
    }

    /**
     * Switches into the TinyMCE iframe.
     */
    public void switchToEditorFrame() {
        WaitUtils.waitForFrameAndSwitch(
                driver,
                iframe
        );
    }

    /**
     * Returns the text currently displayed inside the editor.
     */
    public String getEditorText() {
        return WaitUtils.waitForVisibility(
                driver,
                editorBody
        ).getText();
    }

      /**
     * Replaces the content inside the TinyMCE editor.
     *
     * TinyMCE uses a contenteditable body inside the iframe.
     * JavaScript is used to update the editor content and trigger
     * the input event required by the editor.
     */
    public void enterText(String text) {

        WebElement editor = WaitUtils.waitForVisibility(
                driver,
                editorBody
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].innerHTML = '<p>' + arguments[1] + '</p>';" +
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                editor,
                text
        );
    }

    /**
     * Returns the current browser page title.
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Switches WebDriver back to the main document.
     */
    public void switchToMainDocument() {
        driver.switchTo().defaultContent();
    }
}