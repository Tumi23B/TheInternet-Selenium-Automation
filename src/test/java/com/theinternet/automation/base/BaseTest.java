package com.theinternet.automation.base;

import com.theinternet.automation.driver.DriverFactory;
import com.theinternet.automation.listeners.TestExecutionListener;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

/**
 * Provides the common browser lifecycle used by all tests.
 *
 * BaseTest is responsible for starting and stopping WebDriver.
 * Individual page objects are responsible for navigating to their
 * required application pages.
 */
@Listeners(TestExecutionListener.class)
public abstract class BaseTest {

    protected WebDriver driver;

    /**
     * Starts a fresh browser before each test method.
     */
    @BeforeMethod
    public void setUp() {

        DriverFactory.initializeDriver();

        driver = DriverFactory.getDriver();
    }

    /**
     * Closes the browser after each test method.
     */
    @AfterMethod
    public void tearDown() {

        DriverFactory.quitDriver();

        driver = null;
    }
}