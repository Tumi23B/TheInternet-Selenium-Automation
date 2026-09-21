package com.theinternet.automation.pages;

import com.theinternet.automation.base.BasePage;
import com.theinternet.automation.config.ConfigurationManager;
import com.theinternet.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Represents the Dynamic Controls page.
 *
 * This page demonstrates controls that can be dynamically added, removed,
 * enabled, and disabled. Explicit waits are used to synchronize with
 * these changes instead of relying on fixed delays.
 */
public class DynamicControlsPage extends BasePage {

    private final By checkbox = By.id("checkbox");
    private final By removeButton =
            By.xpath("//button[normalize-space()='Remove']");
    private final By addButton =
            By.xpath("//button[normalize-space()='Add']");

    private final By inputField =
            By.cssSelector("#input-example input");

private final By enableButton =
        By.xpath("//button[normalize-space()='Enable']");

private final By disableButton =
        By.xpath("//button[normalize-space()='Disable']");

    private final By message = By.id("message");

    /**
     * Opens the Dynamic Controls page.
     */
    public void open() {
        driver.get(
                ConfigurationManager.getConfig("base.url")
                        + "/dynamic_controls"
        );
    }

    /**
     * Selects the checkbox.
     */
    public void selectCheckbox() {
        WaitUtils.waitForClickable(driver, checkbox).click();
    }

    /**
     * Removes the checkbox and waits for it to disappear.
     */
    public void removeCheckbox() {
        WaitUtils.waitForClickable(driver, removeButton).click();
        WaitUtils.waitForInvisibility(driver, checkbox);
    }

    /**
     * Adds the checkbox and waits for it to become visible again.
     */
    public void addCheckbox() {
        WaitUtils.waitForClickable(driver, addButton).click();
        WaitUtils.waitForVisibility(driver, checkbox);
    }

    /**
     * Determines whether the checkbox is currently visible.
     */
    public boolean isCheckboxVisible() {
        try {
            return WaitUtils.waitForVisibility(driver, checkbox).isDisplayed();
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * Enables the text input and waits for its enabled state.
     */
    public void enableInput() {
        WaitUtils.waitForClickable(driver, enableButton).click();
        WaitUtils.waitForEnabled(driver, inputField);
    }

    /**
     * Disables the text input and waits for its disabled state.
     */
    public void disableInput() {
        WaitUtils.waitForClickable(driver, disableButton).click();
        WaitUtils.waitForDisabled(driver, inputField);
    }

    /**
     * Determines whether the text input is currently enabled.
     */
    public boolean isInputEnabled() {
        WebElement input = WaitUtils.waitForVisibility(driver, inputField);
        return input.isEnabled();
    }

    /**
     * Returns the dynamic status message displayed by the page.
     */
    public String getMessage() {
        return WaitUtils.waitForVisibility(driver, message).getText();
    }
}