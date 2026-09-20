package com.theinternet.automation.base;

import com.theinternet.automation.driver.DriverFactory;
import org.openqa.selenium.WebDriver;

/**
 * Provides the common WebDriver access shared by all page objects.
 *
 * Page objects extend this class so they can interact with the active
 * browser without creating or managing their own WebDriver instance.
 */
public abstract class BasePage {

    protected final WebDriver driver;

    /**
     * Uses the WebDriver managed by DriverFactory.
     *
     * Centralizing driver access keeps browser lifecycle management
     * separate from page-specific behaviour.
     */
    protected BasePage() {
        this.driver = DriverFactory.getDriver();
    }
}