package com.theinternet.automation.tests;

import com.theinternet.automation.base.BaseTest;
import com.theinternet.automation.pages.DynamicLoadingPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Covers the dynamic loading scenario.
 *
 * The test verifies that content is displayed after the user
 * starts the loading process and that synchronization is handled
 * using explicit waits rather than fixed delays.
 */
public class DynamicLoadingTest extends BaseTest {

    /**
     * Verifies that dynamically loaded content is displayed.
     */
    @Test
    public void shouldDisplayDynamicallyLoadedContent() {

        DynamicLoadingPage dynamicLoadingPage =
                new DynamicLoadingPage();

        dynamicLoadingPage.open();

        // Start the dynamic loading process.
        dynamicLoadingPage.clickStart();

        // Wait for and verify the dynamically loaded content.
        Assert.assertEquals(
                dynamicLoadingPage.getLoadedContent(),
                "Hello World!",
                "Expected dynamically loaded content was not displayed."
        );
    }
}