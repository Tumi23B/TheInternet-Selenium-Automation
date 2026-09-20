package com.theinternet.automation.base;

import com.theinternet.automation.config.ConfigurationManager;
import com.theinternet.automation.driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Provides the common browser setup and cleanup used by all tests.
 *
 * Individual test classes extend this class so they do not need to
 * duplicate WebDriver initialization, navigation, or shutdown logic.
 */
public abstract class BaseTest {

    protected WebDriver driver;

    /**
     * Starts a fresh browser session before each test.
     *
     * Using a new browser session for every test keeps tests isolated
     * and prevents one test's state from affecting another.
     */
    @BeforeMethod
    public void setUp() {

        DriverFactory.initializeDriver();

        driver = DriverFactory.getDriver();

        driver.get(
                ConfigurationManager.getConfig("base.url")
        );
    }

    /**
     * Closes the browser after each test.
     *
     * DriverFactory owns the actual shutdown logic so browser lifecycle
     * remains centralized in one place.
     */
    @AfterMethod
    public void tearDown() {

        DriverFactory.quitDriver();

        driver = null;
    }
}