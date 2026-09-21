package com.theinternet.automation.base;

import com.theinternet.automation.driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Provides the common browser lifecycle used by all tests.
 *
 * BaseTest is responsible only for starting and stopping WebDriver.
 * Individual page objects are responsible for navigating to their
 * required application pages.
 */
public abstract class BaseTest {

    protected WebDriver driver;

    /**
     * Starts a fresh browser session before each test.
     *
     * Each test receives its own browser session so that cookies,
     * authentication state, and page state cannot leak between tests.
     */
    @BeforeMethod
    public void setUp() {

        DriverFactory.initializeDriver();

        driver = DriverFactory.getDriver();
    }

    /**
     * Closes the browser after each test.
     *
     * DriverFactory owns the actual shutdown logic so WebDriver
     * lifecycle remains centralized.
     */
    @AfterMethod
    public void tearDown() {

        DriverFactory.quitDriver();

        driver = null;
    }
}