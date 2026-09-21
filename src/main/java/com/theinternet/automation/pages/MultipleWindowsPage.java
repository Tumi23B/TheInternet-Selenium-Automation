package com.theinternet.automation.pages;

import com.theinternet.automation.base.BasePage;
import com.theinternet.automation.config.ConfigurationManager;
import com.theinternet.automation.utils.WaitUtils;
import org.openqa.selenium.By;

import java.util.Set;

/**
 * Represents the Multiple Windows page.
 *
 * This page demonstrates opening and switching between multiple
 * browser windows while keeping the original window available.
 */
public class MultipleWindowsPage extends BasePage {

    private final By newWindowLink =
            By.cssSelector("a[href='/windows/new']");

    /**
     * Opens the Multiple Windows page.
     */
    public void open() {
        driver.get(
                ConfigurationManager.getConfig("base.url")
                        + "/windows"
        );
    }

    /**
     * Returns the handle of the current browser window.
     */
    public String getCurrentWindowHandle() {
        return driver.getWindowHandle();
    }

    /**
     * Returns all currently open browser window handles.
     */
    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    /**
     * Opens a new browser window.
     */
    public void openNewWindow() {
        WaitUtils.waitForClickable(driver, newWindowLink).click();
    }

    /**
     * Opens the requested number of additional browser windows.
     *
     * @param numberOfWindows number of additional windows to open
     */
    public void openMultipleWindows(int numberOfWindows) {

        if (numberOfWindows < 1) {
            throw new IllegalArgumentException(
                    "Number of windows must be greater than zero."
            );
        }

        for (int i = 0; i < numberOfWindows; i++) {
            openNewWindow();
        }
    }

    /**
     * Switches the WebDriver context to a newly opened window.
     *
     * The original window handle is excluded so the method
     * switches specifically to another available window.
     *
     * @param originalWindowHandle handle of the original window
     */
    public void switchToNewWindow(String originalWindowHandle) {

        for (String windowHandle : driver.getWindowHandles()) {

            if (!windowHandle.equals(originalWindowHandle)) {
                driver.switchTo().window(windowHandle);
                return;
            }
        }

        throw new IllegalStateException(
                "New browser window was not opened."
        );
    }

    /**
     * Returns the current browser window title.
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Closes the currently active browser window.
     */
    public void closeCurrentWindow() {
        driver.close();
    }

    /**
     * Switches WebDriver back to the specified browser window.
     *
     * @param windowHandle handle of the target window
     */
    public void switchToWindow(String windowHandle) {
        driver.switchTo().window(windowHandle);
    }
}