package com.theinternet.automation.tests;

import com.theinternet.automation.base.BaseTest;
import com.theinternet.automation.pages.DragAndDropPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Covers the Drag and Drop scenario.
 *
 * The test verifies that Column A can be moved onto Column B
 * and that the resulting column state is updated correctly.
 */
public class DragAndDropTest extends BaseTest {

    /**
     * Verifies that Column A can be dragged onto Column B.
     */
    @Test
    public void shouldDragColumnAToColumnB() {

        DragAndDropPage dragAndDropPage =
                new DragAndDropPage();

        dragAndDropPage.open();

        Assert.assertEquals(
                dragAndDropPage.getColumnAText(),
                "A",
                "Column A should initially contain element A."
        );

        Assert.assertEquals(
                dragAndDropPage.getColumnBText(),
                "B",
                "Column B should initially contain element B."
        );

        dragAndDropPage.dragColumnAToColumnB();

        Assert.assertEquals(
                dragAndDropPage.getColumnAText(),
                "B",
                "Column A should contain element B after the drag-and-drop operation."
        );

        Assert.assertEquals(
                dragAndDropPage.getColumnBText(),
                "A",
                "Column B should contain element A after the drag-and-drop operation."
        );
    }
}