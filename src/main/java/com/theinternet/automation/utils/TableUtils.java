package com.theinternet.automation.utils;

import java.util.Comparator;
import java.util.List;

/**
 * Provides reusable operations for processing table data.
 *
 * Keeping calculations and row searches here allows page objects
 * to focus on browser interaction while tests focus on verification.
 */
public final class TableUtils {

    private TableUtils() {
        // Prevent instantiation.
    }

    /**
     * Finds the row containing the highest Due amount.
     *
     * @param rows table data to search
     * @return row with the highest Due amount
     */
    public static TableRow findHighestDue(List<TableRow> rows) {

        validateRows(rows);

        return rows.stream()
                .max(Comparator.comparingDouble(TableRow::getDue))
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Unable to find the row with the highest Due amount."
                        )
                );
    }

    /**
     * Finds the row containing the lowest Due amount.
     *
     * @param rows table data to search
     * @return row with the lowest Due amount
     */
    public static TableRow findLowestDue(List<TableRow> rows) {

        validateRows(rows);

        return rows.stream()
                .min(Comparator.comparingDouble(TableRow::getDue))
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Unable to find the row with the lowest Due amount."
                        )
                );
    }

    /**
     * Finds a table row using the person's first and last name.
     *
     * @param rows table data to search
     * @param firstName expected first name
     * @param lastName expected last name
     * @return matching row
     */
    public static TableRow findRowByPerson(
            List<TableRow> rows,
            String firstName,
            String lastName) {

        validateRows(rows);

        return rows.stream()
                .filter(row ->
                        row.getFirstName().equalsIgnoreCase(firstName)
                                && row.getLastName().equalsIgnoreCase(lastName)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Unable to find table row for: "
                                        + firstName + " " + lastName
                        )
                );
    }

    /**
     * Ensures that table data exists before processing it.
     */
    private static void validateRows(List<TableRow> rows) {

        if (rows == null || rows.isEmpty()) {
            throw new IllegalArgumentException(
                    "Table data cannot be null or empty."
            );
        }
    }
}