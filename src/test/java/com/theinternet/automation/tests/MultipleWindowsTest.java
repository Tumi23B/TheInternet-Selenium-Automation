package com.theinternet.automation.tests;

import com.theinternet.automation.base.BaseTest;
import com.theinternet.automation.pages.MultipleWindowsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * Covers the Multiple Windows scenario.
 *
 * The test verifies that multiple browser windows can be opened,
 * identified, accessed, validated, closed, and that control can
 * return to the original window.
 */
public class MultipleWindowsTest extends BaseTest {

    /**
     * Verifies that five additional browser windows can be opened
     * and managed successfully.
     */
    @Test
    public void shouldManageMultipleBrowserWindows() {

        MultipleWindowsPage multipleWindowsPage =
                new MultipleWindowsPage();

        multipleWindowsPage.open();

        // Store the original browser window.
        String originalWindow =
                multipleWindowsPage.getCurrentWindowHandle();

        // Open five additional browser windows.
        multipleWindowsPage.openMultipleWindows(5);

        Set<String> windowHandles =
                multipleWindowsPage.getWindowHandles();

        // Verify that the original window plus five new windows exist.
        Assert.assertEquals(
                windowHandles.size(),
                6,
                "Expected the original window plus five additional windows."
        );

        // Verify each additional window and close it.
        for (String windowHandle : windowHandles) {

            if (!windowHandle.equals(originalWindow)) {

                multipleWindowsPage.switchToWindow(windowHandle);

                Assert.assertEquals(
                        multipleWindowsPage.getPageTitle(),
                        "New Window",
                        "Unexpected title in the additional browser window."
                );

                multipleWindowsPage.closeCurrentWindow();
            }
        }

        // Return to the original browser window.
        multipleWindowsPage.switchToWindow(originalWindow);

        // Verify that the original page is active again.
        Assert.assertEquals(
                multipleWindowsPage.getPageTitle(),
                "The Internet",
                "The original browser window was not restored."
        );
    }
}