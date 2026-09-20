package com.theinternet.automation.driver;

import com.theinternet.automation.config.ConfigurationManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Manages the lifecycle of the WebDriver used by the automation framework.
 *
 * Keeping driver creation and shutdown in one place prevents individual
 * tests from having to manage browser lifecycle themselves.
 */
public final class DriverFactory {

    private static WebDriver driver;

    /**
     * Utility class — driver management is handled through static methods.
     */
    private DriverFactory() {
        // Prevent instantiation.
    }

    /**
     * Creates the browser configured for the current test execution.
     *
     * The browser is selected from config.properties rather than being
     * hardcoded inside the test classes.
     */
    public static void initializeDriver() {

        String browser = ConfigurationManager.getConfig("browser");

        if (!"chrome".equalsIgnoreCase(browser)) {
            throw new IllegalArgumentException(
                    "Unsupported browser configured: " + browser
            );
        }

        // Selenium Manager handles the ChromeDriver setup .
        driver = new ChromeDriver();

        // Start each test with a predictable browser window size.
        driver.manage().window().maximize();
    }

    /**
     * Returns the active WebDriver instance.
     *
     * Failing immediately when the driver has not been initialized gives
     * us a clear framework error instead of a NullPointerException later.
     *
     * 
     */
    public static WebDriver getDriver() {

        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver has not been initialized."
            );
        }

        return driver;
    }

    /**
     * Closes the browser and releases the WebDriver instance.
     *
     * Keeping shutdown here gives BaseTest a single, consistent place
     * to clean up after every test.
     */
    public static void quitDriver() {

        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}