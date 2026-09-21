package com.theinternet.automation.tests;

import com.theinternet.automation.base.BaseTest;
import com.theinternet.automation.pages.DynamicControlsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Covers the dynamic control scenarios on the Dynamic Controls page.
 *
 * The tests verify that controls can be selected, removed, added,
 * enabled, and disabled while the page is synchronized using
 * explicit waits.
 */
public class DynamicControlsTest extends BaseTest {

    /**
     * Verifies that the checkbox can be selected, removed, and added again.
     */
    @Test
    public void shouldRemoveAndAddCheckbox() {

        DynamicControlsPage dynamicControlsPage =
                new DynamicControlsPage();

        dynamicControlsPage.open();

        // Verify the checkbox is available when the page is opened.
        Assert.assertTrue(
                dynamicControlsPage.isCheckboxVisible(),
                "Checkbox should be visible when the page is opened."
        );

        // Select the checkbox.
        dynamicControlsPage.selectCheckbox();

        // Remove the checkbox and verify that it disappears.
        dynamicControlsPage.removeCheckbox();

        Assert.assertFalse(
                dynamicControlsPage.isCheckboxVisible(),
                "Checkbox should no longer be visible after removal."
        );

        // Add the checkbox again and verify that it returns.
        dynamicControlsPage.addCheckbox();

        Assert.assertTrue(
                dynamicControlsPage.isCheckboxVisible(),
                "Checkbox should be visible after being added again."
        );
    }

    /**
     * Verifies that the text input can be enabled and disabled dynamically.
     */
    @Test
    public void shouldEnableAndDisableInput() {

        DynamicControlsPage dynamicControlsPage =
                new DynamicControlsPage();

        dynamicControlsPage.open();

        // Verify the input starts in a disabled state.
        Assert.assertFalse(
                dynamicControlsPage.isInputEnabled(),
                "Input should be disabled when the page is opened."
        );

        // Enable the input and verify its state.
        dynamicControlsPage.enableInput();

        Assert.assertTrue(
                dynamicControlsPage.isInputEnabled(),
                "Input should be enabled after clicking Enable."
        );

        // Disable the input and verify its state.
        dynamicControlsPage.disableInput();

        Assert.assertFalse(
                dynamicControlsPage.isInputEnabled(),
                "Input should be disabled after clicking Disable."
        );
    }
}