package com.theinternet.automation.tests;

import com.theinternet.automation.base.BaseTest;
import com.theinternet.automation.pages.FramesPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Covers the Frames scenario.
 *
 * The test verifies that the user can open the iframe page,
 * switch into the iframe, interact with its content, validate
 * the content, and return to the main document.
 */
public class FramesTest extends BaseTest {

    /**
     * Verifies interaction with content inside an iframe.
     */
    @Test
    public void shouldInteractWithIframeAndReturnToMainDocument() {

        FramesPage framesPage = new FramesPage();

        // Open the iframe page.
        framesPage.open();

        // Switch into the iframe.
        framesPage.switchToEditorFrame();

        // Enter test content into the iframe editor.
        String testText = "Selenium iframe automation test.";

        framesPage.enterText(testText);

        // Verify the content entered into the iframe.
        Assert.assertEquals(
                framesPage.getEditorText(),
                testText,
                "The iframe content was not updated correctly."
        );

        // Return to the main document.
        framesPage.switchToMainDocument();

        // Verify that the main page is active again.
        Assert.assertEquals(
                framesPage.getPageTitle(),
                "The Internet",
                "The WebDriver context was not returned to the main document."
        );
    }
}