package com.theinternet.automation.pages;

import com.theinternet.automation.base.BasePage;
import com.theinternet.automation.config.ConfigurationManager;
import com.theinternet.automation.utils.WaitUtils;
import org.openqa.selenium.By;

/**
 * Represents the Dynamic Loading page.
 *
 * This page demonstrates content that appears only after
 * a user starts a loading operation. Explicit waits are used
 * to synchronize with the dynamically loaded content.
 */
public class DynamicLoadingPage extends BasePage {

    private final By startButton =
            By.cssSelector("#start button");

    private final By loadedContent =
            By.cssSelector("#finish h4");

    /**
     * Opens the Dynamic Loading example page.
     */
    public void open() {
        driver.get(
                ConfigurationManager.getConfig("base.url")
                        + "/dynamic_loading/2"
        );
    }

    /**
     * Starts the dynamic loading process.
     */
    public void clickStart() {
        WaitUtils.waitForClickable(driver, startButton).click();
    }

    /**
     * Waits for the dynamically loaded content and returns its text.
     */
    public String getLoadedContent() {
        return WaitUtils.waitForVisibility(driver, loadedContent)
                .getText();
    }
}