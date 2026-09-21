package com.theinternet.automation.pages;

import com.theinternet.automation.base.BasePage;
import com.theinternet.automation.config.ConfigurationManager;
import com.theinternet.automation.utils.WaitUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;

/**
 * Represents the JavaScript Alerts page.
 *
 * This page demonstrates JavaScript alert, confirm, and prompt dialogs.
 * Browser dialogs are handled through Selenium's Alert API.
 */
public class JavaScriptAlertsPage extends BasePage {

    private final By jsAlertButton =
            By.xpath("//button[normalize-space()='Click for JS Alert']");

    private final By jsConfirmButton =
            By.xpath("//button[normalize-space()='Click for JS Confirm']");

    private final By jsPromptButton =
            By.xpath("//button[normalize-space()='Click for JS Prompt']");

    private final By resultMessage =
            By.id("result");

    /**
     * Opens the JavaScript Alerts page.
     */
    public void open() {
        driver.get(
                ConfigurationManager.getConfig("base.url")
                        + "/javascript_alerts"
        );
    }

    /**
     * Opens the JavaScript alert dialog.
     */
    public void openAlert() {
        WaitUtils.waitForClickable(driver, jsAlertButton).click();
    }

    /**
     * Returns the currently displayed JavaScript alert.
     */
    public Alert getAlert() {
        return driver.switchTo().alert();
    }

    /**
     * Accepts the currently displayed alert.
     */
    public void acceptAlert() {
        getAlert().accept();
    }

    /**
     * Opens the JavaScript confirm dialog.
     */
    public void openConfirm() {
        WaitUtils.waitForClickable(driver, jsConfirmButton).click();
    }

    /**
     * Dismisses the currently displayed confirm dialog.
     */
    public void dismissConfirm() {
        getAlert().dismiss();
    }

    /**
     * Opens the JavaScript prompt dialog.
     */
    public void openPrompt() {
        WaitUtils.waitForClickable(driver, jsPromptButton).click();
    }

    /**
     * Enters a value into the currently displayed prompt.
     */
    public void enterPromptValue(String value) {
        getAlert().sendKeys(value);
    }

    /**
     * Accepts the currently displayed prompt.
     */
    public void acceptPrompt() {
        getAlert().accept();
    }

    /**
     * Returns the result message displayed by the page.
     */
    public String getResultMessage() {
        return WaitUtils.waitForVisibility(driver, resultMessage)
                .getText();
    }
}