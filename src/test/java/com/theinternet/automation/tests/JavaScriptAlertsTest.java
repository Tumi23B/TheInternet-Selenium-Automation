package com.theinternet.automation.tests;

import com.theinternet.automation.base.BaseTest;
import com.theinternet.automation.pages.JavaScriptAlertsPage;
import org.openqa.selenium.Alert;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Covers the JavaScript alert, confirm, and prompt scenarios.
 *
 * The tests verify that browser dialogs can be handled correctly
 * through Selenium's Alert API.
 */
public class JavaScriptAlertsTest extends BaseTest {

    /**
     * Verifies that a JavaScript alert displays the expected message
     * and can be accepted.
     */
    @Test
    public void shouldAcceptJavaScriptAlert() {

        JavaScriptAlertsPage alertsPage =
                new JavaScriptAlertsPage();

        alertsPage.open();
        alertsPage.openAlert();

        Alert alert = alertsPage.getAlert();

        Assert.assertEquals(
                alert.getText(),
                "I am a JS Alert",
                "Unexpected JavaScript alert message."
        );

        alertsPage.acceptAlert();

        Assert.assertEquals(
                alertsPage.getResultMessage(),
                "You successfully clicked an alert",
                "Alert result message was not displayed."
        );
    }

    /**
     * Verifies that a JavaScript confirm dialog can be dismissed.
     */
    @Test
    public void shouldDismissJavaScriptConfirm() {

        JavaScriptAlertsPage alertsPage =
                new JavaScriptAlertsPage();

        alertsPage.open();
        alertsPage.openConfirm();

        Alert confirm = alertsPage.getAlert();

        Assert.assertEquals(
                confirm.getText(),
                "I am a JS Confirm",
                "Unexpected JavaScript confirm message."
        );

        alertsPage.dismissConfirm();

        Assert.assertEquals(
                alertsPage.getResultMessage(),
                "You clicked: Cancel",
                "Confirm dismissal result was not displayed."
        );
    }

    /**
     * Verifies that a value can be entered into a JavaScript prompt
     * and submitted successfully.
     */
    @Test
    public void shouldEnterValueIntoJavaScriptPrompt() {

        JavaScriptAlertsPage alertsPage =
                new JavaScriptAlertsPage();

        alertsPage.open();
        alertsPage.openPrompt();

        Alert prompt = alertsPage.getAlert();

        Assert.assertEquals(
                prompt.getText(),
                "I am a JS prompt",
                "Unexpected JavaScript prompt message."
        );

        alertsPage.enterPromptValue("Selenium Automation");
        alertsPage.acceptPrompt();

        Assert.assertEquals(
                alertsPage.getResultMessage(),
                "You entered: Selenium Automation",
                "Prompt result message was not displayed."
        );
    }
}