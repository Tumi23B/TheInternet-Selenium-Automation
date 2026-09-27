package com.theinternet.automation.reporting;

import com.theinternet.automation.constants.FrameworkConstants;

import java.awt.Desktop;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

/**
 * Generates a self-contained HTML report from a completed test execution.
 *
 * The report includes suite-level execution information, test statistics,
 * individual test results, failure messages, and embedded screenshots.
 */
public class HtmlReportGenerator {

    private static final String REPORT_FILE_NAME =
            "automation-report.html";

    /**
     * Generates the HTML execution report.
     *
     * @param summary complete test execution summary
     */
    public void generateReport(TestExecutionSummary summary) {

        if (summary == null) {
            throw new IllegalArgumentException(
                    "Test execution summary cannot be null."
            );
        }

        try {
            Path reportsDirectory =
                    Path.of(FrameworkConstants.REPORTS_DIRECTORY);

            Files.createDirectories(reportsDirectory);

            Path reportPath =
                    reportsDirectory.resolve(REPORT_FILE_NAME);

            String html =
                    buildHtmlReport(summary);

            Files.writeString(
                    reportPath,
                    html,
                    StandardCharsets.UTF_8
            );

            System.out.println(
                    "HTML report generated: "
                            + reportPath.toAbsolutePath()
            );

            openReportInBrowser(reportPath);

        } catch (IOException exception) {

            throw new RuntimeException(
                    "Unable to generate HTML execution report.",
                    exception
            );
        }
    }

    /**
     * Opens the generated HTML report using the system's default browser.
     *
     * Browser opening is treated as an optional convenience feature.
     * Failure to open the report must never cause the test execution
     * itself to fail.
     *
     * @param reportPath path to the generated HTML report
     */
    private void openReportInBrowser(
            Path reportPath) {

        try {

            if (!Desktop.isDesktopSupported()) {

                System.out.println(
                        "Desktop browsing is not supported. "
                                + "The HTML report can be opened manually at: "
                                + reportPath.toAbsolutePath()
                );

                return;
            }

            Desktop desktop =
                    Desktop.getDesktop();

            if (!desktop.isSupported(
                    Desktop.Action.BROWSE
            )) {

                System.out.println(
                        "Browser launching is not supported. "
                                + "The HTML report can be opened manually at: "
                                + reportPath.toAbsolutePath()
                );

                return;
            }

            desktop.browse(
                    reportPath.toUri()
            );

            System.out.println(
                    "HTML report opened in the default browser."
            );

        } catch (Exception exception) {

            System.out.println(
                    "Unable to automatically open the HTML report. "
                            + "Open it manually at: "
                            + reportPath.toAbsolutePath()
            );
        }
    }

    /**
     * Builds the complete HTML document.
     */
    private String buildHtmlReport(
            TestExecutionSummary summary) {

        StringBuilder html =
                new StringBuilder();

        appendDocumentStart(html);
        appendHeader(html, summary);
        appendSummaryCards(html, summary);
        appendTestResults(html, summary.getTestResults());
        appendDocumentEnd(html);

        return html.toString();
    }

    /**
     * Adds the HTML document opening and styling.
     */
    private void appendDocumentStart(
            StringBuilder html) {

        html.append("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport"
                          content="width=device-width, initial-scale=1.0">
                    <title>The Internet - Selenium Automation Report</title>

                    <style>
                        * {
                            box-sizing: border-box;
                        }

                        body {
                            margin: 0;
                            padding: 0;
                            font-family: Arial, Helvetica, sans-serif;
                            background: #f4f6f8;
                            color: #1f2933;
                        }

                        .container {
                            width: 92%;
                            max-width: 1400px;
                            margin: 40px auto;
                        }

                        .header {
                            background: #ffffff;
                            border-radius: 10px;
                            padding: 30px;
                            margin-bottom: 25px;
                            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
                        }

                        .header h1 {
                            margin: 0 0 10px 0;
                            font-size: 30px;
                        }

                        .header p {
                            margin: 6px 0;
                            color: #52606d;
                        }

                        .summary-grid {
                            display: grid;
                            grid-template-columns:
                                repeat(auto-fit, minmax(180px, 1fr));
                            gap: 15px;
                            margin-bottom: 25px;
                        }

                        .summary-card {
                            background: #ffffff;
                            border-radius: 10px;
                            padding: 22px;
                            box-shadow:
                                0 2px 8px rgba(0, 0, 0, 0.08);
                        }

                        .summary-card h3 {
                            margin: 0 0 8px 0;
                            font-size: 14px;
                            color: #52606d;
                            text-transform: uppercase;
                            letter-spacing: 0.5px;
                        }

                        .summary-card .value {
                            font-size: 30px;
                            font-weight: bold;
                        }

                        .test-section {
                            background: #ffffff;
                            border-radius: 10px;
                            padding: 25px;
                            margin-bottom: 20px;
                            box-shadow:
                                0 2px 8px rgba(0, 0, 0, 0.08);
                        }

                        .test-section h2 {
                            margin-top: 0;
                        }

                        .test-card {
                            border: 1px solid #d9e2ec;
                            border-radius: 8px;
                            margin-top: 15px;
                            overflow: hidden;
                        }

                        .test-card-header {
                            padding: 15px 18px;
                            background: #f8fafc;
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            gap: 15px;
                        }

                        .test-name {
                            font-weight: bold;
                            word-break: break-word;
                        }

                        .status {
                            display: inline-block;
                            padding: 5px 10px;
                            border-radius: 20px;
                            font-size: 12px;
                            font-weight: bold;
                        }

                        .passed {
                            background: #d1fae5;
                            color: #065f46;
                        }

                        .failed {
                            background: #fee2e2;
                            color: #991b1b;
                        }

                        .skipped {
                            background: #fef3c7;
                            color: #92400e;
                        }

                        .test-details {
                            padding: 18px;
                        }

                        .detail-grid {
                            display: grid;
                            grid-template-columns:
                                repeat(auto-fit, minmax(220px, 1fr));
                            gap: 12px;
                            margin-bottom: 20px;
                        }

                        .detail-item {
                            background: #f8fafc;
                            padding: 12px;
                            border-radius: 6px;
                        }

                        .detail-label {
                            display: block;
                            font-size: 12px;
                            color: #52606d;
                            margin-bottom: 5px;
                            font-weight: bold;
                        }

                        .failure {
                            background: #fff5f5;
                            border: 1px solid #fecaca;
                            border-radius: 6px;
                            padding: 15px;
                            margin-bottom: 20px;
                        }

                        .failure strong {
                            display: block;
                            margin-bottom: 8px;
                        }

                        .failure-message {
                            white-space: pre-wrap;
                            word-break: break-word;
                            font-family: Consolas, monospace;
                            font-size: 13px;
                        }

                        .screenshot-container {
                            margin-top: 15px;
                        }

                        .screenshot-container img {
                            display: block;
                            width: 100%;
                            max-width: 1000px;
                            height: auto;
                            border: 1px solid #d9e2ec;
                            border-radius: 6px;
                        }

                        .no-screenshot {
                            padding: 15px;
                            background: #f8fafc;
                            border-radius: 6px;
                            color: #52606d;
                        }

                        @media (max-width: 700px) {
                            .container {
                                width: 96%;
                                margin: 20px auto;
                            }

                            .header {
                                padding: 20px;
                            }

                            .test-card-header {
                                flex-direction: column;
                                align-items: flex-start;
                            }
                        }
                    </style>
                </head>

                <body>
                    <div class="container">
                """);
    }

    /**
     * Adds the report header and suite-level execution information.
     */
    private void appendHeader(
            StringBuilder html,
            TestExecutionSummary summary) {

        html.append("""
                <section class="header">
                    <h1>The Internet - Selenium Automation Execution Report</h1>
                """);

        html.append("<p><strong>Execution Date:</strong> ")
                .append(escapeHtml(summary.getExecutionDate()))
                .append("</p>");

        html.append("<p><strong>Start Time:</strong> ")
                .append(escapeHtml(summary.getStartTime()))
                .append("</p>");

        html.append("<p><strong>End Time:</strong> ")
                .append(escapeHtml(summary.getEndTime()))
                .append("</p>");

        html.append("<p><strong>Total Duration:</strong> ")
                .append(formatDuration(summary.getDurationMillis()))
                .append("</p>");

        html.append("""
                </section>
                """);
    }

    /**
     * Adds the execution statistics cards.
     */
    private void appendSummaryCards(
            StringBuilder html,
            TestExecutionSummary summary) {

        html.append("""
                <section class="summary-grid">
                """);

        appendSummaryCard(
                html,
                "Total Tests",
                String.valueOf(summary.getTotalTests())
        );

        appendSummaryCard(
                html,
                "Passed",
                String.valueOf(summary.getPassedTests())
        );

        appendSummaryCard(
                html,
                "Failed",
                String.valueOf(summary.getFailedTests())
        );

        appendSummaryCard(
                html,
                "Skipped",
                String.valueOf(summary.getSkippedTests())
        );

        appendSummaryCard(
                html,
                "Pass Rate",
                String.format(
                        "%.2f%%",
                        summary.getPassRate()
                )
        );

        html.append("""
                </section>
                """);
    }

    /**
     * Adds one execution summary card.
     */
    private void appendSummaryCard(
            StringBuilder html,
            String label,
            String value) {

        html.append("""
                <div class="summary-card">
                """);

        html.append("<h3>")
                .append(escapeHtml(label))
                .append("</h3>");

        html.append("<div class=\"value\">")
                .append(escapeHtml(value))
                .append("</div>");

        html.append("""
                </div>
                """);
    }

    /**
     * Adds individual test execution details.
     */
    private void appendTestResults(
            StringBuilder html,
            List<TestResult> testResults) {

        html.append("""
                <section class="test-section">
                    <h2>Test Execution Details</h2>
                """);

        for (TestResult testResult : testResults) {

            appendTestCard(
                    html,
                    testResult
            );
        }

        if (testResults.isEmpty()) {

            html.append("""
                    <p>No test results were recorded.</p>
                    """);
        }

        html.append("""
                </section>
                """);
    }

    /**
     * Adds one individual test result.
     */
    private void appendTestCard(
            StringBuilder html,
            TestResult testResult) {

        String statusClass =
                testResult.getStatus()
                        .toLowerCase();

        html.append("""
                <div class="test-card">
                    <div class="test-card-header">
                """);

        html.append("<div class=\"test-name\">")
                .append(escapeHtml(
                        testResult.getTestClassName()
                                + " - "
                                + testResult.getTestMethodName()
                ))
                .append("</div>");

        html.append("<span class=\"status ")
                .append(escapeHtml(statusClass))
                .append("\">")
                .append(escapeHtml(testResult.getStatus()))
                .append("</span>");

        html.append("""
                    </div>
                    <div class="test-details">
                        <div class="detail-grid">
                """);

        appendDetail(
                html,
                "Test Class",
                testResult.getTestClassName()
        );

        appendDetail(
                html,
                "Test Method",
                testResult.getTestMethodName()
        );

        appendDetail(
                html,
                "Start Time",
                testResult.getStartTime()
        );

        appendDetail(
                html,
                "End Time",
                testResult.getEndTime()
        );

        appendDetail(
                html,
                "Duration",
                formatDuration(
                        testResult.getDurationMillis()
                )
        );

        html.append("""
                        </div>
                """);

        appendFailureMessage(
                html,
                testResult
        );

        appendScreenshot(
                html,
                testResult
        );

        html.append("""
                    </div>
                </div>
                """);
    }

    /**
     * Adds an individual test detail field.
     */
    private void appendDetail(
            StringBuilder html,
            String label,
            String value) {

        html.append("""
                <div class="detail-item">
                """);

        html.append("<span class=\"detail-label\">")
                .append(escapeHtml(label))
                .append("</span>");

        html.append(escapeHtml(value));

        html.append("""
                </div>
                """);
    }

    /**
     * Adds failure information when a test fails.
     */
    private void appendFailureMessage(
            StringBuilder html,
            TestResult testResult) {

        if (testResult.getFailureMessage() == null
                || testResult.getFailureMessage().isBlank()) {

            return;
        }

        html.append("""
                <div class="failure">
                    <strong>Failure / Error</strong>
                    <div class="failure-message">
                """);

        html.append(
                escapeHtml(
                        testResult.getFailureMessage()
                )
        );

        html.append("""
                    </div>
                </div>
                """);
    }

    /**
     * Embeds the test screenshot directly into the HTML document.
     */
    private void appendScreenshot(
            StringBuilder html,
            TestResult testResult) {

        String screenshotBase64 =
                testResult.getScreenshotBase64();

        html.append("""
                <div class="screenshot-container">
                """);

        if (screenshotBase64 == null
                || screenshotBase64.isBlank()) {

            html.append("""
                    <div class="no-screenshot">
                        Screenshot was not available for this test.
                    </div>
                    """);

        } else {

            html.append("""
                    <img src="data:image/png;base64,
                    """);

            html.append(screenshotBase64);

            html.append("""
                    " alt="Test execution screenshot">
                    """);
        }

        html.append("""
                </div>
                """);
    }

    /**
     * Adds the closing HTML tags.
     */
    private void appendDocumentEnd(
            StringBuilder html) {

        html.append("""
                    </div>
                </body>
                </html>
                """);
    }

    /**
     * Converts milliseconds into a readable duration.
     */
    private String formatDuration(
            long durationMillis) {

        Duration duration =
                Duration.ofMillis(durationMillis);

        long minutes =
                duration.toMinutes();

        long seconds =
                duration.minusMinutes(minutes)
                        .getSeconds();

        long milliseconds =
                duration.toMillisPart();

        if (minutes > 0) {

            return String.format(
                    "%dm %02ds %03dms",
                    minutes,
                    seconds,
                    milliseconds
            );
        }

        return String.format(
                "%ds %03dms",
                seconds,
                milliseconds
        );
    }

    /**
     * Escapes text before inserting it into HTML.
     *
     * This prevents test names and failure messages containing HTML
     * characters from corrupting the generated report.
     */
    private String escapeHtml(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}