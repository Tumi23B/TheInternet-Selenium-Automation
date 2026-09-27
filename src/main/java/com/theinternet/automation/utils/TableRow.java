package com.theinternet.automation.utils;

/**
 * Represents a single row of data from an HTML table.
 *
 * Keeping table data in a dedicated object allows tests and utility
 * methods to work with meaningful values instead of raw WebElements.
 */
public class TableRow {

    private final String lastName;
    private final String firstName;
    private final String email;
    private final double due;
    private final String website;

    public TableRow(
            String lastName,
            String firstName,
            String email,
            double due,
            String website) {

        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.due = due;
        this.website = website;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public double getDue() {
        return due;
    }

    public String getWebsite() {
        return website;
    }
}