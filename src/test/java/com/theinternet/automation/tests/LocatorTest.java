package com.theinternet.automation.tests;

import com.theinternet.automation.base.BaseTest;
import com.theinternet.automation.pages.LocatorPage;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Demonstrates different Selenium locator strategies
 * against elements on the Login page.
 *
 * The test covers ID, Name, Class, CSS Selector, XPath,
 * Relative XPath, XPath using text, and XPath using attributes.
 */
public class LocatorTest extends BaseTest {

    @Test
    public void shouldLocateElementsUsingDifferentStrategies() {

        LocatorPage locatorPage =
                new LocatorPage();

        locatorPage.open();

        WebElement usernameById =
                locatorPage.findUsernameById();

        Assert.assertTrue(
                usernameById.isDisplayed(),
                "Username field was not located using ID."
        );

        WebElement passwordByName =
                locatorPage.findPasswordByName();

        Assert.assertTrue(
                passwordByName.isDisplayed(),
                "Password field was not located using Name."
        );

        WebElement loginButtonByClass =
                locatorPage.findLoginButtonByClass();

        Assert.assertTrue(
                loginButtonByClass.isDisplayed(),
                "Login button was not located using Class."
        );

        WebElement usernameByCss =
                locatorPage.findUsernameByCss();

        Assert.assertTrue(
                usernameByCss.isDisplayed(),
                "Username field was not located using CSS Selector."
        );

        WebElement passwordByXPath =
                locatorPage.findPasswordByXPath();

        Assert.assertTrue(
                passwordByXPath.isDisplayed(),
                "Password field was not located using XPath."
        );

        WebElement passwordByRelativeXPath =
                locatorPage.findPasswordByRelativeXPath();

        Assert.assertTrue(
                passwordByRelativeXPath.isDisplayed(),
                "Password field was not located using Relative XPath."
        );

        WebElement loginHeadingByText =
                locatorPage.findLoginHeadingByText();

        Assert.assertTrue(
                loginHeadingByText.isDisplayed(),
                "Login heading was not located using text."
        );

        WebElement loginButtonByAttribute =
                locatorPage.findLoginButtonByAttribute();

        Assert.assertTrue(
                loginButtonByAttribute.isDisplayed(),
                "Login button was not located using an attribute."
        );
    }
}