package com.theinternet.automation.tests;

import com.theinternet.automation.base.BaseTest;
import com.theinternet.automation.pages.TablesPage;
import com.theinternet.automation.utils.TableRow;
import com.theinternet.automation.utils.TableUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Covers the dynamic table scenarios required by the assignment.
 *
 * The tests read table data dynamically instead of relying on
 * hard-coded row numbers. TableUtils is used to search and
 * process the extracted data.
 */
public class TablesTest extends BaseTest {

    /**
     * Verifies that table data can be read and a specific
     * person's row can be identified dynamically.
     */
    @Test
    public void shouldIdentifySpecificTableRow() {

        TablesPage tablesPage = new TablesPage();

        tablesPage.open();

        List<TableRow> rows =
                tablesPage.getTableRows();

        Assert.assertFalse(
                rows.isEmpty(),
                "The table should contain data."
        );

        TableRow person =
                TableUtils.findRowByPerson(
                        rows,
                        "Jason",
                        "Doe"
                );

        Assert.assertEquals(
                person.getFirstName(),
                "Jason",
                "Unexpected first name."
        );

        Assert.assertEquals(
                person.getLastName(),
                "Doe",
                "Unexpected last name."
        );

        Assert.assertEquals(
                person.getEmail(),
                "jdoe@hotmail.com",
                "Unexpected email address."
        );
    }

    /**
     * Verifies that the row with the highest Due amount
     * can be identified dynamically.
     */
    @Test
    public void shouldFindPersonWithHighestDueAmount() {

        TablesPage tablesPage = new TablesPage();

        tablesPage.open();

        List<TableRow> rows =
                tablesPage.getTableRows();

        TableRow highestDue =
                TableUtils.findHighestDue(rows);

        Assert.assertEquals(
                highestDue.getFirstName(),
                "Jason",
                "Unexpected person associated with the highest Due amount."
        );

        Assert.assertEquals(
                highestDue.getLastName(),
                "Doe",
                "Unexpected last name associated with the highest Due amount."
        );

        Assert.assertEquals(
                highestDue.getDue(),
                100.00,
                "Unexpected highest Due amount."
        );
    }

    /**
     * Verifies that the row with the lowest Due amount
     * can be identified dynamically.
     */
    @Test
    public void shouldFindPersonWithLowestDueAmount() {

        TablesPage tablesPage = new TablesPage();

        tablesPage.open();

        List<TableRow> rows =
                tablesPage.getTableRows();

        TableRow lowestDue =
                TableUtils.findLowestDue(rows);

        Assert.assertEquals(
                lowestDue.getDue(),
                50.00,
                "Unexpected lowest Due amount."
        );
    }
}