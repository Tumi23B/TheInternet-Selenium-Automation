package com.theinternet.automation.utils;

import com.theinternet.automation.config.ConfigurationManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Provides reusable explicit-wait operations for the automation framework.
 *
 * Centralizing waits keeps synchronization logic consistent across page
 * objects and prevents tests from relying on fixed delays such as Thread.sleep().
 */
public final class WaitUtils {

    private WaitUtils() {
        // Prevent instantiation.
    }

    /**
     * Creates an explicit WebDriverWait using the timeout configured
     * in configuration.properties.
     */
    private static WebDriverWait createWait(WebDriver driver) {

        long timeout = Long.parseLong(
                ConfigurationManager.getConfig("explicit.wait")
        );

        return new WebDriverWait(
                driver,
                Duration.ofSeconds(timeout)
        );
    }

    /**
     * Waits until an element is visible.
     */
    public static WebElement waitForVisibility(
            WebDriver driver,
            By locator) {

        return createWait(driver).until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    /**
     * Waits until an element is clickable.
     */
    public static WebElement waitForClickable(
            WebDriver driver,
            By locator) {

        return createWait(driver).until(
                ExpectedConditions.elementToBeClickable(locator)
        );
    }

    /**
     * Waits until an element is no longer present in the DOM.
     */
    public static boolean waitForInvisibility(
            WebDriver driver,
            By locator) {

        return createWait(driver).until(
                ExpectedConditions.invisibilityOfElementLocated(locator)
        );
    }

    /**
     * Waits until an element is present in the DOM.
     */
    public static WebElement waitForPresence(
            WebDriver driver,
            By locator) {

        return createWait(driver).until(
                ExpectedConditions.presenceOfElementLocated(locator)
        );
    }
    /**
 * Waits until an element becomes enabled.
 */
public static WebElement waitForEnabled(
        WebDriver driver,
        By locator) {

    return createWait(driver).until(webDriver -> {
        WebElement element = webDriver.findElement(locator);
        return element.isEnabled() ? element : null;
    });
}

/**
 * Waits until an element becomes disabled.
 */
public static WebElement waitForDisabled(
        WebDriver driver,
        By locator) {

    return createWait(driver).until(webDriver -> {
        WebElement element = webDriver.findElement(locator);
        return !element.isEnabled() ? element : null;
    });
}
}