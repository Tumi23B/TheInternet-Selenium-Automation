package com.theinternet.automation.pages;

import com.theinternet.automation.base.BasePage;
import com.theinternet.automation.config.ConfigurationManager;
import org.openqa.selenium.By;

/**
 * Represents the login page and the authentication actions available to a user.
 *
 * Keeping the page locators and interactions here allows the tests to focus
 * on the authentication behaviour rather than the page implementation details.
 */
public class LoginPage extends BasePage {

    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By authenticationMessage = By.id("flash");
    private final By logoutLink = By.cssSelector("a[href='/logout']");

    /**
     * Enters the supplied username.
     */
    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }

    /**
     * Enters the supplied password.
     */
    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    /**
     * Submits the login form.
     */
    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    /**
     * Performs a complete login using the supplied credentials.
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    /**
     * Returns the authentication message displayed by the application.
     */
    public String getAuthenticationMessage() {
        return driver.findElement(authenticationMessage).getText();
    }

    /**
     * Logs the user out of the application.
     */
    public void logout() {
        driver.findElement(logoutLink).click();
    }

  public void open() {
    driver.get(
            ConfigurationManager.getConfig("login.url")
    );
}
}