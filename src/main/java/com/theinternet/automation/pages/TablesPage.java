package com.theinternet.automation.pages;

import com.theinternet.automation.base.BasePage;
import com.theinternet.automation.config.ConfigurationManager;
import com.theinternet.automation.utils.TableRow;
import com.theinternet.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Tables page.
 *
 * This page provides access to table data without relying on
 * hard-coded row numbers. Table rows are read dynamically from
 * the page and converted into TableRow objects.
 */
public class TablesPage extends BasePage {

    private final By tableRows =
            By.cssSelector("#table1 tbody tr");

    /**
     * Opens the Tables page.
     */
    public void open() {
        driver.get(
                ConfigurationManager.getConfig("base.url")
                        + "/tables"
        );
    }

    /**
     * Reads all rows from the first table on the page.
     *
     * Rows are located dynamically so the test does not depend
     * on a specific row number.
     *
     * @return list containing all table rows
     */
   public List<TableRow> getTableRows() {

    List<WebElement> rows =
            driver.findElements(tableRows);

    List<TableRow> tableData =
            new ArrayList<>();

    for (WebElement row : rows) {

        List<WebElement> cells =
                row.findElements(By.tagName("td"));

        if (cells.size() < 5) {
            continue;
        }

        String lastName = cells.get(0).getText();
        String firstName = cells.get(1).getText();
        String email = cells.get(2).getText();

        double due = parseDueAmount(
                cells.get(3).getText()
        );

        String website = cells.get(4).getText();

        tableData.add(
                new TableRow(
                        lastName,
                        firstName,
                        email,
                        due,
                        website
                )
        );
    }

    return tableData;
}

    /**
     * Converts a table Due value such as "$100.00"
     * into a numeric value suitable for calculations.
     */
    private double parseDueAmount(String dueText) {

        String numericValue =
                dueText.replace("$", "").trim();

        return Double.parseDouble(numericValue);
    }
}