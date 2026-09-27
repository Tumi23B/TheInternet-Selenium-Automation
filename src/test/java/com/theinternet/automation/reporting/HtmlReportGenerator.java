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
 * individual test results, execution-specific findings, failure messages,
 * and embedded screenshots.
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

                    <title>
                        The Internet - Selenium Automation Report
                    </title>

                    <style>

                        * {
                            box-sizing: border-box;
                        }

                        body {
                            margin: 0;
                            padding: 0;
                            font-family:
                                Arial,
                                Helvetica,
                                sans-serif;
                            background: #f1f5f9;
                            color: #172033;
                            line-height: 1.5;
                        }

                        .container {
                            width: 92%;
                            max-width: 1450px;
                            margin: 35px auto 60px;
                        }

                        /* ================================
                           REPORT HEADER
                           ================================ */

                        .header {
                            background: #ffffff;
                            border: 1px solid #e2e8f0;
                            border-radius: 14px;
                            padding: 32px;
                            margin-bottom: 24px;
                            box-shadow:
                                0 4px 16px
                                rgba(15, 23, 42, 0.06);
                        }

                        .header h1 {
                            margin: 0 0 18px;
                            font-size: 30px;
                            line-height: 1.2;
                            color: #0f172a;
                        }

                        .header p {
                            margin: 7px 0;
                            color: #475569;
                            font-size: 14px;
                        }

                        .header strong {
                            color: #1e293b;
                        }

                        /* ================================
                           SUMMARY CARDS
                           ================================ */

                        .summary-grid {
                            display: grid;
                            grid-template-columns:
                                repeat(
                                    auto-fit,
                                    minmax(180px, 1fr)
                                );
                            gap: 15px;
                            margin-bottom: 28px;
                        }

                        .summary-card {
                            background: #ffffff;
                            border: 1px solid #e2e8f0;
                            border-radius: 12px;
                            padding: 22px;
                            box-shadow:
                                0 3px 12px
                                rgba(15, 23, 42, 0.05);
                        }

                        .summary-card h3 {
                            margin: 0 0 8px;
                            font-size: 12px;
                            color: #64748b;
                            text-transform: uppercase;
                            letter-spacing: 0.7px;
                        }

                        .summary-card .value {
                            font-size: 29px;
                            font-weight: 700;
                            color: #0f172a;
                        }

                        /* ================================
                           TEST SECTION
                           ================================ */

                        .test-section {
                            background: #ffffff;
                            border: 1px solid #e2e8f0;
                            border-radius: 14px;
                            padding: 26px;
                            margin-bottom: 22px;
                            box-shadow:
                                0 4px 16px
                                rgba(15, 23, 42, 0.05);
                        }

                        .section-heading {
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            gap: 15px;
                            margin-bottom: 10px;
                        }

                        .section-heading h2 {
                            margin: 0;
                            color: #0f172a;
                            font-size: 21px;
                        }

                        .section-description {
                            margin: 0 0 18px;
                            color: #64748b;
                            font-size: 14px;
                        }

                        /* ================================
                           TEST CARD
                           ================================ */

                        .test-card {
                            border: 1px solid #dbe3ec;
                            border-radius: 11px;
                            margin-top: 18px;
                            overflow: hidden;
                            background: #ffffff;
                        }

                        .test-card-header {
                            padding: 17px 20px;
                            background: #f8fafc;
                            border-bottom: 1px solid #e2e8f0;
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            gap: 15px;
                        }

                        .test-name {
                            font-weight: 700;
                            color: #172033;
                            word-break: break-word;
                        }

                        /* ================================
                           STATUS BADGES
                           ================================ */

                        .status {
                            display: inline-block;
                            padding: 6px 12px;
                            border-radius: 999px;
                            font-size: 11px;
                            font-weight: 700;
                            letter-spacing: 0.5px;
                            white-space: nowrap;
                        }

                        .passed {
                            background: #dcfce7;
                            color: #166534;
                        }

                        .failed {
                            background: #fee2e2;
                            color: #991b1b;
                        }

                        .skipped {
                            background: #fef3c7;
                            color: #92400e;
                        }

                        /* ================================
                           TEST DETAILS
                           ================================ */

                        .test-details {
                            padding: 20px;
                        }

                        .detail-grid {
                            display: grid;
                            grid-template-columns:
                                repeat(
                                    auto-fit,
                                    minmax(210px, 1fr)
                                );
                            gap: 12px;
                            margin-bottom: 20px;
                        }

                        .detail-item {
                            background: #f8fafc;
                            border: 1px solid #e2e8f0;
                            padding: 13px;
                            border-radius: 8px;
                        }

                        .detail-label {
                            display: block;
                            font-size: 11px;
                            color: #64748b;
                            margin-bottom: 4px;
                            font-weight: 700;
                            text-transform: uppercase;
                            letter-spacing: 0.4px;
                        }

                        /* ================================
                           EXECUTION ANALYSIS
                           ================================ */

                        .analysis {
                            margin: 20px 0;
                            border: 1px solid #bfdbfe;
                            border-radius: 10px;
                            background: #eff6ff;
                            overflow: hidden;
                        }

                        .analysis-header {
                            padding: 14px 17px;
                            background: #dbeafe;
                            border-bottom: 1px solid #bfdbfe;
                            font-weight: 700;
                            color: #1e3a8a;
                            font-size: 14px;
                        }

                        .analysis-body {
                            padding: 17px;
                        }

                        .analysis-title {
                            margin: 0 0 8px;
                            font-size: 12px;
                            font-weight: 700;
                            color: #475569;
                            text-transform: uppercase;
                            letter-spacing: 0.5px;
                        }

                        .analysis-amount {
                            display: inline-block;
                            margin-bottom: 16px;
                            font-size: 25px;
                            font-weight: 700;
                            color: #0f172a;
                        }

                        .user-list {
                            display: flex;
                            flex-direction: column;
                            gap: 8px;
                        }

                        .user-item {
                            display: flex;
                            align-items: center;
                            gap: 10px;
                            padding: 10px 12px;
                            background: #ffffff;
                            border: 1px solid #dbeafe;
                            border-radius: 8px;
                            font-weight: 600;
                            color: #1e293b;
                        }

                        .user-marker {
                            display: inline-flex;
                            align-items: center;
                            justify-content: center;
                            width: 25px;
                            height: 25px;
                            border-radius: 50%;
                            background: #2563eb;
                            color: #ffffff;
                            font-size: 12px;
                            font-weight: 700;
                            flex-shrink: 0;
                        }

                        /* ================================
                           FAILURE INFORMATION
                           ================================ */

                        .failure {
                            background: #fff5f5;
                            border: 1px solid #fecaca;
                            border-radius: 9px;
                            padding: 16px;
                            margin-bottom: 20px;
                        }

                        .failure strong {
                            display: block;
                            margin-bottom: 8px;
                            color: #991b1b;
                        }

                        .failure-message {
                            white-space: pre-wrap;
                            word-break: break-word;
                            font-family: Consolas, monospace;
                            font-size: 13px;
                            color: #7f1d1d;
                        }

                        /* ================================
                           SCREENSHOT
                           ================================ */

                        .screenshot-container {
                            margin-top: 20px;
                        }

                        .screenshot-heading {
                            margin-bottom: 10px;
                            font-size: 13px;
                            font-weight: 700;
                            color: #475569;
                        }

                        .screenshot-container img {
                            display: block;
                            width: 100%;
                            max-width: 1100px;
                            height: auto;
                            border: 1px solid #cbd5e1;
                            border-radius: 9px;
                            box-shadow:
                                0 3px 10px
                                rgba(15, 23, 42, 0.06);
                        }

                        .no-screenshot {
                            padding: 15px;
                            background: #f8fafc;
                            border: 1px solid #e2e8f0;
                            border-radius: 8px;
                            color: #64748b;
                        }

                        /* ================================
                           EMPTY STATE
                           ================================ */

                        .empty-state {
                            padding: 25px;
                            text-align: center;
                            color: #64748b;
                            background: #f8fafc;
                            border-radius: 8px;
                        }

                        /* ================================
                           RESPONSIVE DESIGN
                           ================================ */

                        @media (max-width: 700px) {

                            .container {
                                width: 95%;
                                margin: 20px auto 40px;
                            }

                            .header {
                                padding: 22px;
                            }

                            .header h1 {
                                font-size: 24px;
                            }

                            .test-section {
                                padding: 18px;
                            }

                            .test-card-header {
                                flex-direction: column;
                                align-items: flex-start;
                            }

                            .section-heading {
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
                    <h1>
                        The Internet - Selenium Automation
                        Execution Report
                    </h1>
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

                    <div class="section-heading">
                        <h2>Test Execution Details</h2>
                    </div>

                    <p class="section-description">
                        Detailed results, execution findings, and
                        final browser-state screenshots for each test.
                    </p>
                """);

        for (TestResult testResult : testResults) {

            appendTestCard(
                    html,
                    testResult
            );
        }

        if (testResults.isEmpty()) {

            html.append("""
                    <div class="empty-state">
                        No test results were recorded.
                    </div>
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

        appendExecutionDetails(
                html,
                testResult
        );

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
     * Adds test-specific execution findings.
     *
     * This section is intentionally optional so that tests which do not
     * produce additional findings do not display an empty panel.
     */
    private void appendExecutionDetails(
            StringBuilder html,
            TestResult testResult) {

        String executionDetails =
                testResult.getExecutionDetails();

        if (executionDetails == null
                || executionDetails.isBlank()) {

            return;
        }

        String[] lines =
                executionDetails.split("\\R");

        if (lines.length == 0) {
            return;
        }

        String analysisTitle =
                lines[0];

        String amount =
                "";

        StringBuilder users =
                new StringBuilder();

        boolean readingUsers =
                false;

        for (int index = 1;
             index < lines.length;
             index++) {

            String line =
                    lines[index].trim();

            if (line.isBlank()) {
                continue;
            }

            if ("MATCHING USERS:".equalsIgnoreCase(line)) {

                readingUsers = true;
                continue;
            }

            if (!readingUsers) {

                if (amount.isBlank()) {
                    amount = line;
                }

                continue;
            }

            if (line.startsWith("•")) {

                String userName =
                        line.substring(1).trim();

                if (!userName.isBlank()) {

                    users.append("""
                            <div class="user-item">
                                <span class="user-marker">✓</span>
                            """);

                    users.append(
                            escapeHtml(userName)
                    );

                    users.append("""
                            </div>
                            """);
                }
            }
        }

        String heading =
                analysisTitle.startsWith("HIGHEST")
                        ? "Highest Due"
                        : analysisTitle.startsWith("LOWEST")
                        ? "Lowest Due"
                        : "Execution Finding";

        html.append("""
                <div class="analysis">

                    <div class="analysis-header">
                        Test Analysis
                    </div>

                    <div class="analysis-body">

                        <div class="analysis-title">
                """);

        html.append(
                escapeHtml(heading)
        );

        html.append("""
                        </div>
                """);

        if (!amount.isBlank()) {

            html.append("<div class=\"analysis-amount\">")
                    .append(escapeHtml(amount))
                    .append("</div>");
        }

        if (users.length() > 0) {

            html.append("""
                        <div class="analysis-title">
                            Matching Users
                        </div>

                        <div class="user-list">
                    """);

            html.append(users);

            html.append("""
                        </div>
                    """);
        }

        html.append("""
                    </div>
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

                    <div class="screenshot-heading">
                        Final Browser State
                    </div>
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
                    " alt="Final browser state screenshot">
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