package com.theinternet.automation.tests;

import com.theinternet.automation.base.BaseTest;
import com.theinternet.automation.config.ConfigurationManager;
import com.theinternet.automation.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Covers the authentication scenarios required by the automation framework.
 *
 * The tests use the LoginPage to keep browser interactions out of the
 * test class, allowing each test to describe the expected user behaviour.
 */
public class AuthenticationTest extends BaseTest {

    private LoginPage loginPage;

    /**
     * Verifies that a user can log in with valid credentials.
     */
    @Test
    public void shouldLoginWithValidCredentials() {

        loginPage = new LoginPage();
        loginPage.open();

        loginPage.login(
                ConfigurationManager.getTestData("valid.username"),
                ConfigurationManager.getCredential("THE_INTERNET_PASSWORD")
        );

        Assert.assertTrue(
                loginPage.getAuthenticationMessage().contains("You logged into a secure area!"),
                "Successful login message was not displayed."
        );
    }

    /**
     * Verifies that invalid credentials are rejected with the
     * appropriate authentication message.
     */
    @Test
    public void shouldRejectInvalidCredentials() {

        loginPage = new LoginPage();
        loginPage.open();

        loginPage.login(
                ConfigurationManager.getTestData("invalid.username"),
                ConfigurationManager.getTestData("invalid.password")
        );

        Assert.assertTrue(
                loginPage.getAuthenticationMessage().contains("Your username is invalid!"),
                "Invalid login message was not displayed."
        );
    }

    /**
     * Verifies that an authenticated user can log out successfully.
     */
    @Test
    public void shouldLogoutSuccessfully() {

        loginPage = new LoginPage();
        loginPage.open();

        loginPage.login(
                ConfigurationManager.getTestData("valid.username"),
                ConfigurationManager.getCredential("THE_INTERNET_PASSWORD")
        );

        loginPage.logout();

        Assert.assertTrue(
                loginPage.getAuthenticationMessage().contains("You logged out of the secure area!"),
                "Logout confirmation message was not displayed."
        );
    }
}