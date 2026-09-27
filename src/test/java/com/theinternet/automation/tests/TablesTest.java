
package com.theinternet.automation.tests;

import com.theinternet.automation.base.BaseTest;
import com.theinternet.automation.pages.TablesPage;
import com.theinternet.automation.utils.TableRow;
import com.theinternet.automation.utils.TableUtils;
import org.testng.Assert;
import org.testng.Reporter;
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
     * Verifies that all rows containing the highest Due amount
     * can be identified dynamically.
     *
     * If multiple people have the same highest amount, every
     * matching person is displayed and included in the execution
     * details used by the HTML report.
     */
    @Test
    public void shouldFindPersonWithHighestDueAmount() {

        TablesPage tablesPage = new TablesPage();

        tablesPage.open();

        List<TableRow> rows =
                tablesPage.getTableRows();

        List<TableRow> highestDueRows =
                TableUtils.findRowsWithHighestDue(rows);

        Assert.assertFalse(
                highestDueRows.isEmpty(),
                "At least one row should contain the highest Due amount."
        );

        double highestDue =
                highestDueRows.get(0).getDue();

        StringBuilder executionDetails =
                new StringBuilder();

        executionDetails.append(
                "HIGHEST DUE AMOUNT: $"
        );

        executionDetails.append(
                String.format(
                        "%.2f",
                        highestDue
                )
        );

        executionDetails.append(
                "\nMATCHING USERS:"
        );

        System.out.println();
        System.out.println("==============================================");
        System.out.println("[HIGHEST DUE]");
        System.out.printf(
                "Amount: $%.2f%n",
                highestDue
        );

        for (TableRow row : highestDueRows) {

            String fullName =
                    row.getFirstName()
                            + " "
                            + row.getLastName();

            executionDetails.append(
                    "\n• "
            ).append(
                    fullName
            );

            System.out.println(
                    "Person: "
                            + fullName
            );

            Assert.assertEquals(
                    row.getDue(),
                    highestDue,
                    "Every identified highest Due row should have the same amount."
            );
        }

        Reporter.getCurrentTestResult().setAttribute(
                "executionDetails",
                executionDetails.toString()
        );

        System.out.println("==============================================");
        System.out.println();

        Assert.assertEquals(
                highestDue,
                100.00,
                "Unexpected highest Due amount."
        );
    }

    /**
     * Verifies that all rows containing the lowest Due amount
     * can be identified dynamically.
     *
     * If multiple people have the same lowest amount, every
     * matching person is displayed and included in the execution
     * details used by the HTML report.
     */
    @Test
    public void shouldFindPersonWithLowestDueAmount() {

        TablesPage tablesPage = new TablesPage();

        tablesPage.open();

        List<TableRow> rows =
                tablesPage.getTableRows();

        List<TableRow> lowestDueRows =
                TableUtils.findRowsWithLowestDue(rows);

        Assert.assertFalse(
                lowestDueRows.isEmpty(),
                "At least one row should contain the lowest Due amount."
        );

        double lowestDue =
                lowestDueRows.get(0).getDue();

        StringBuilder executionDetails =
                new StringBuilder();

        executionDetails.append(
                "LOWEST DUE AMOUNT: $"
        );

        executionDetails.append(
                String.format(
                        "%.2f",
                        lowestDue
                )
        );

        executionDetails.append(
                "\nMATCHING USERS:"
        );

        System.out.println();
        System.out.println("==============================================");
        System.out.println("[LOWEST DUE]");
        System.out.printf(
                "Amount: $%.2f%n",
                lowestDue
        );

        for (TableRow row : lowestDueRows) {

            String fullName =
                    row.getFirstName()
                            + " "
                            + row.getLastName();

            executionDetails.append(
                    "\n• "
            ).append(
                    fullName
            );

            System.out.println(
                    "Person: "
                            + fullName
            );

            Assert.assertEquals(
                    row.getDue(),
                    lowestDue,
                    "Every identified lowest Due row should have the same amount."
            );
        }

        Reporter.getCurrentTestResult().setAttribute(
                "executionDetails",
                executionDetails.toString()
        );

        System.out.println("==============================================");
        System.out.println();

        Assert.assertEquals(
                lowestDue,
                50.00,
                "Unexpected lowest Due amount."
        );
    }
}
