package com.theinternet.automation.pages;

import com.theinternet.automation.base.BasePage;
import com.theinternet.automation.config.ConfigurationManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Represents the Login page for demonstrating different
 * Selenium locator strategies.
 *
 * Each method uses a different locator technique so the
 * corresponding test can verify that the elements are
 * correctly identified.
 */
public class LocatorPage extends BasePage {

    public void open() {
        driver.get(
                ConfigurationManager.getConfig("login.url")
        );
    }

    /**
     * Locates the username field using its ID.
     */
    public WebElement findUsernameById() {
        return driver.findElement(
                By.id("username")
        );
    }

    /**
     * Locates the password field using its name attribute.
     */
    public WebElement findPasswordByName() {
        return driver.findElement(
                By.name("password")
        );
    }

    /**
     * Locates the login button using its CSS class.
     */
    public WebElement findLoginButtonByClass() {
        return driver.findElement(
                By.className("radius")
        );
    }

    /**
     * Locates the username field using a CSS selector.
     */
    public WebElement findUsernameByCss() {
        return driver.findElement(
                By.cssSelector("input[name='username']")
        );
    }

    /**
     * Locates the password field using XPath.
     */
    public WebElement findPasswordByXPath() {
        return driver.findElement(
                By.xpath("//input[@id='password']")
        );
    }

    /**
     * Locates the password field using a relative XPath
     * from the login form.
     */
    public WebElement findPasswordByRelativeXPath() {
        return driver.findElement(
                By.xpath(
                        "//form[@id='login']//input[@name='password']"
                )
        );
    }

    /**
     * Locates the page heading using its visible text.
     */
    public WebElement findLoginHeadingByText() {
        return driver.findElement(
                By.xpath(
                        "//h2[normalize-space()='Login Page']"
                )
        );
    }

    /**
     * Locates the login button using its type attribute.
     */
    public WebElement findLoginButtonByAttribute() {
        return driver.findElement(
                By.xpath(
                        "//button[@type='submit']"
                )
        );
    }
}