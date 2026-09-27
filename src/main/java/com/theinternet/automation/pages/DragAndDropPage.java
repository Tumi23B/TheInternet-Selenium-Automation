package com.theinternet.automation.pages;

import com.theinternet.automation.base.BasePage;
import com.theinternet.automation.config.ConfigurationManager;
import com.theinternet.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * Represents the Drag and Drop page.
 *
 * This page demonstrates moving one draggable element onto
 * another element and verifying the resulting page state.
 */
public class DragAndDropPage extends BasePage {

    private final By columnA =
            By.id("column-a");

    private final By columnB =
            By.id("column-b");

    /**
     * Opens the Drag and Drop page.
     */
    public void open() {
        driver.get(
                ConfigurationManager.getConfig("base.url")
                        + "/drag_and_drop"
        );
    }

    /**
     * Returns the element currently displayed in Column A.
     */
    public String getColumnAText() {
        return WaitUtils.waitForVisibility(
                driver,
                columnA
        ).getText();
    }

    /**
     * Returns the element currently displayed in Column B.
     */
    public String getColumnBText() {
        return WaitUtils.waitForVisibility(
                driver,
                columnB
        ).getText();
    }

    /**
     * Drags Column A onto Column B using Selenium's
     * standard Actions API.
     */
    public void dragColumnAToColumnB() {

        WebElement source =
                WaitUtils.waitForVisibility(
                        driver,
                        columnA
                );

        WebElement target =
                WaitUtils.waitForVisibility(
                        driver,
                        columnB
                );

        new Actions(driver)
                .dragAndDrop(source, target)
                .perform();
    }
}