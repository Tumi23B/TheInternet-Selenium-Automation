
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

                        :root {
                            --black: #050505;
                            --black-soft: #0b0b0b;
                            --surface: #111111;
                            --surface-light: #171717;
                            --surface-hover: #1c1c1c;

                            --green: #00e676;
                            --green-dark: #00b85c;
                            --green-soft: rgba(0, 230, 118, 0.10);
                            --green-border: rgba(0, 230, 118, 0.28);

                            --white: #ffffff;
                            --text: #f4f4f4;
                            --text-muted: #a3a3a3;
                            --text-dark-muted: #737373;

                            --border: #292929;
                            --border-light: #222222;

                            --red: #ff5252;
                            --red-soft: rgba(255, 82, 82, 0.10);
                            --red-border: rgba(255, 82, 82, 0.30);

                            --yellow: #ffc107;
                            --yellow-soft: rgba(255, 193, 7, 0.10);
                            --yellow-border: rgba(255, 193, 7, 0.30);

                            --shadow:
                                0 12px 35px rgba(0, 0, 0, 0.35);
                        }

                        * {
                            box-sizing: border-box;
                        }

                        html {
                            scroll-behavior: smooth;
                        }

                        body {
                            margin: 0;
                            padding: 0;
                            font-family:
                                Inter,
                                Segoe UI,
                                Arial,
                                Helvetica,
                                sans-serif;
                            background:
                                radial-gradient(
                                    circle at top right,
                                    rgba(0, 230, 118, 0.055),
                                    transparent 30%
                                ),
                                var(--black);
                            color: var(--text);
                            line-height: 1.5;
                        }

                        body::before {
                            content: "";
                            position: fixed;
                            top: 0;
                            left: 0;
                            right: 0;
                            height: 3px;
                            background:
                                linear-gradient(
                                    90deg,
                                    var(--green),
                                    var(--green-dark),
                                    var(--green)
                                );
                            z-index: 9999;
                        }

                        .container {
                            width: 92%;
                            max-width: 1500px;
                            margin: 45px auto 70px;
                        }

                        /* ================================
                           REPORT HEADER
                           ================================ */

                        .header {
                            position: relative;
                            overflow: hidden;

                            background:
                                linear-gradient(
                                    135deg,
                                    #090909 0%,
                                    #101010 55%,
                                    #0b150f 100%
                                );

                            border: 1px solid var(--border);
                            border-radius: 18px;
                            padding: 36px;
                            margin-bottom: 24px;

                            box-shadow: var(--shadow);
                        }

                        .header::after {
                            content: "";
                            position: absolute;
                            width: 240px;
                            height: 240px;
                            right: -90px;
                            top: -110px;

                            background:
                                radial-gradient(
                                    circle,
                                    rgba(0, 230, 118, 0.18),
                                    transparent 68%
                                );

                            pointer-events: none;
                        }

                        .header-top {
                            display: flex;
                            justify-content: space-between;
                            align-items: flex-start;
                            gap: 25px;
                            margin-bottom: 28px;
                        }

                        .brand {
                            display: flex;
                            align-items: center;
                            gap: 15px;
                        }

                        .brand-icon {
                            display: flex;
                            align-items: center;
                            justify-content: center;

                            width: 48px;
                            height: 48px;

                            background: var(--green);
                            color: var(--black);

                            border-radius: 12px;

                            font-size: 23px;
                            font-weight: 900;

                            box-shadow:
                                0 0 25px
                                rgba(0, 230, 118, 0.20);
                        }

                        .brand-text {
                            font-size: 12px;
                            font-weight: 800;
                            text-transform: uppercase;
                            letter-spacing: 1.8px;
                            color: var(--green);
                        }

                        .brand-subtext {
                            margin-top: 2px;
                            color: var(--text-dark-muted);
                            font-size: 12px;
                        }

                        .execution-badge {
                            display: inline-flex;
                            align-items: center;
                            gap: 8px;

                            padding: 8px 13px;

                            border: 1px solid var(--green-border);
                            border-radius: 999px;

                            background: var(--green-soft);
                            color: var(--green);

                            font-size: 11px;
                            font-weight: 800;
                            text-transform: uppercase;
                            letter-spacing: 0.8px;
                            white-space: nowrap;
                        }

                        .execution-dot {
                            width: 7px;
                            height: 7px;
                            border-radius: 50%;
                            background: var(--green);
                            box-shadow:
                                0 0 9px
                                rgba(0, 230, 118, 0.85);
                        }

                        .header h1 {
                            margin: 0 0 10px;

                            font-size: clamp(26px, 4vw, 38px);
                            line-height: 1.15;

                            color: var(--white);
                            letter-spacing: -0.7px;
                        }

                        .header-description {
                            max-width: 780px;
                            margin: 0 0 27px;

                            color: var(--text-muted);
                            font-size: 14px;
                        }

                        .execution-info {
                            display: grid;
                            grid-template-columns:
                                repeat(
                                    auto-fit,
                                    minmax(190px, 1fr)
                                );
                            gap: 12px;
                        }

                        .execution-info-item {
                            padding: 14px 16px;

                            background: rgba(255, 255, 255, 0.025);
                            border: 1px solid var(--border);
                            border-radius: 10px;
                        }

                        .execution-info-label {
                            display: block;

                            margin-bottom: 5px;

                            color: var(--text-dark-muted);

                            font-size: 10px;
                            font-weight: 800;
                            text-transform: uppercase;
                            letter-spacing: 0.8px;
                        }

                        .execution-info-value {
                            color: var(--text);
                            font-size: 13px;
                            font-weight: 600;
                            word-break: break-word;
                        }

                        /* ================================
                           SUMMARY CARDS
                           ================================ */

                        .summary-grid {
                            display: grid;
                            grid-template-columns:
                                repeat(
                                    auto-fit,
                                    minmax(190px, 1fr)
                                );
                            gap: 15px;
                            margin-bottom: 28px;
                        }

                        .summary-card {
                            position: relative;
                            overflow: hidden;

                            background: var(--surface);
                            border: 1px solid var(--border);
                            border-radius: 13px;
                            padding: 22px;

                            box-shadow:
                                0 8px 25px
                                rgba(0, 0, 0, 0.25);

                            transition:
                                transform 0.2s ease,
                                border-color 0.2s ease;
                        }

                        .summary-card:hover {
                            transform: translateY(-2px);
                            border-color: var(--green-border);
                        }

                        .summary-card::before {
                            content: "";
                            position: absolute;
                            top: 0;
                            left: 0;
                            right: 0;
                            height: 2px;
                            background: var(--green);
                            opacity: 0.75;
                        }

                        .summary-card h3 {
                            margin: 0 0 9px;

                            font-size: 10px;
                            font-weight: 800;

                            color: var(--text-dark-muted);

                            text-transform: uppercase;
                            letter-spacing: 1px;
                        }

                        .summary-card .value {
                            font-size: 31px;
                            font-weight: 800;
                            line-height: 1.1;
                            color: var(--white);
                        }

                        .summary-card:nth-child(2) .value {
                            color: var(--green);
                        }

                        .summary-card:nth-child(3) .value {
                            color: var(--red);
                        }

                        .summary-card:nth-child(4) .value {
                            color: var(--yellow);
                        }

                        .summary-card:nth-child(5) .value {
                            color: var(--green);
                        }

                        /* ================================
                           TEST SECTION
                           ================================ */

                        .test-section {
                            background: var(--surface);
                            border: 1px solid var(--border);
                            border-radius: 16px;
                            padding: 28px;
                            margin-bottom: 22px;

                            box-shadow: var(--shadow);
                        }

                        .section-heading {
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            gap: 15px;
                            margin-bottom: 7px;
                        }

                        .section-heading h2 {
                            margin: 0;

                            color: var(--white);
                            font-size: 21px;
                            font-weight: 750;
                        }

                        .section-heading h2::before {
                            content: "";
                            display: inline-block;

                            width: 4px;
                            height: 20px;

                            margin-right: 10px;
                            vertical-align: -3px;

                            background: var(--green);
                            border-radius: 3px;
                        }

                        .section-description {
                            margin: 0 0 20px;

                            color: var(--text-muted);
                            font-size: 13px;
                        }

                        /* ================================
                           TEST CARD
                           ================================ */

                        .test-card {
                            border: 1px solid var(--border);
                            border-radius: 12px;

                            margin-top: 16px;
                            overflow: hidden;

                            background: var(--black-soft);

                            transition:
                                border-color 0.2s ease,
                                box-shadow 0.2s ease;
                        }

                        .test-card:hover {
                            border-color: #383838;

                            box-shadow:
                                0 8px 28px
                                rgba(0, 0, 0, 0.30);
                        }

                        .test-card-header {
                            padding: 16px 19px;

                            background:
                                linear-gradient(
                                    90deg,
                                    #141414,
                                    #101010
                                );

                            border-bottom: 1px solid var(--border);

                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            gap: 15px;
                        }

                        .test-name {
                            position: relative;

                            padding-left: 13px;

                            font-weight: 700;
                            font-size: 14px;

                            color: var(--white);

                            word-break: break-word;
                        }

                        .test-name::before {
                            content: "";

                            position: absolute;
                            left: 0;
                            top: 4px;
                            bottom: 4px;

                            width: 3px;

                            background: var(--green);
                            border-radius: 3px;
                        }

                        /* ================================
                           STATUS BADGES
                           ================================ */

                        .status {
                            display: inline-flex;
                            align-items: center;
                            gap: 6px;

                            padding: 6px 12px;

                            border-radius: 999px;

                            font-size: 10px;
                            font-weight: 800;

                            letter-spacing: 0.8px;
                            text-transform: uppercase;

                            white-space: nowrap;
                        }

                        .status::before {
                            content: "";
                            width: 6px;
                            height: 6px;
                            border-radius: 50%;
                            background: currentColor;
                        }

                        .passed {
                            background: var(--green-soft);
                            color: var(--green);
                            border: 1px solid var(--green-border);
                        }

                        .failed {
                            background: var(--red-soft);
                            color: var(--red);
                            border: 1px solid var(--red-border);
                        }

                        .skipped {
                            background: var(--yellow-soft);
                            color: var(--yellow);
                            border: 1px solid var(--yellow-border);
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

                            gap: 10px;
                            margin-bottom: 20px;
                        }

                        .detail-item {
                            background: #0d0d0d;

                            border: 1px solid var(--border);
                            padding: 13px;

                            border-radius: 9px;

                            color: #dddddd;

                            font-size: 13px;
                            font-weight: 600;

                            word-break: break-word;
                        }

                        .detail-label {
                            display: block;

                            font-size: 9px;
                            color: var(--text-dark-muted);

                            margin-bottom: 5px;

                            font-weight: 800;

                            text-transform: uppercase;
                            letter-spacing: 0.8px;
                        }

                        /* ================================
                           EXECUTION ANALYSIS
                           ================================ */

                        .analysis {
                            margin: 20px 0;

                            border: 1px solid var(--green-border);
                            border-radius: 10px;

                            background:
                                linear-gradient(
                                    135deg,
                                    rgba(0, 230, 118, 0.07),
                                    rgba(0, 230, 118, 0.025)
                                );

                            overflow: hidden;
                        }

                        .analysis-header {
                            padding: 13px 17px;

                            background:
                                rgba(0, 230, 118, 0.08);

                            border-bottom: 1px solid var(--green-border);

                            font-weight: 800;
                            color: var(--green);

                            font-size: 12px;

                            text-transform: uppercase;
                            letter-spacing: 0.7px;
                        }

                        .analysis-body {
                            padding: 17px;
                        }

                        .analysis-title {
                            margin: 0 0 8px;

                            font-size: 10px;
                            font-weight: 800;

                            color: var(--text-muted);

                            text-transform: uppercase;
                            letter-spacing: 0.7px;
                        }

                        .analysis-amount {
                            display: inline-block;

                            margin-bottom: 16px;

                            font-size: 27px;
                            font-weight: 800;

                            color: var(--green);
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

                            background: #0c0c0c;

                            border: 1px solid var(--border);
                            border-radius: 8px;

                            font-weight: 600;
                            font-size: 13px;

                            color: #dddddd;
                        }

                        .user-marker {
                            display: inline-flex;
                            align-items: center;
                            justify-content: center;

                            width: 25px;
                            height: 25px;

                            border-radius: 50%;

                            background: var(--green);
                            color: var(--black);

                            font-size: 12px;
                            font-weight: 900;

                            flex-shrink: 0;
                        }

                        /* ================================
                           FAILURE INFORMATION
                           ================================ */

                        .failure {
                            background: var(--red-soft);

                            border: 1px solid var(--red-border);
                            border-left: 4px solid var(--red);

                            border-radius: 9px;

                            padding: 16px;

                            margin-bottom: 20px;
                        }

                        .failure strong {
                            display: block;

                            margin-bottom: 9px;

                            color: var(--red);

                            font-size: 11px;
                            font-weight: 800;

                            text-transform: uppercase;
                            letter-spacing: 0.8px;
                        }

                        .failure-message {
                            white-space: pre-wrap;
                            word-break: break-word;

                            font-family:
                                Consolas,
                                "Courier New",
                                monospace;

                            font-size: 12px;
                            line-height: 1.6;

                            color: #ffb3b3;
                        }

                        /* ================================
                           SCREENSHOT
                           ================================ */

                        .screenshot-container {
                            margin-top: 20px;
                        }

                        .screenshot-heading {
                            display: flex;
                            align-items: center;
                            gap: 8px;

                            margin-bottom: 10px;

                            font-size: 11px;
                            font-weight: 800;

                            color: var(--text-muted);

                            text-transform: uppercase;
                            letter-spacing: 0.7px;
                        }

                        .screenshot-heading::before {
                            content: "▣";

                            color: var(--green);
                            font-size: 13px;
                        }

                        .screenshot-container img {
                            display: block;

                            width: 100%;
                            max-width: 1150px;
                            height: auto;

                            border: 1px solid var(--border);
                            border-radius: 10px;

                            background: #000000;

                            box-shadow:
                                0 8px 30px
                                rgba(0, 0, 0, 0.45);

                            transition:
                                border-color 0.2s ease,
                                transform 0.2s ease;
                        }

                        .screenshot-container img:hover {
                            border-color: var(--green-border);
                        }

                        .no-screenshot {
                            padding: 15px;

                            background: #0d0d0d;

                            border: 1px dashed #333333;
                            border-radius: 8px;

                            color: var(--text-dark-muted);

                            font-size: 12px;
                        }

                        /* ================================
                           EMPTY STATE
                           ================================ */

                        .empty-state {
                            padding: 35px 25px;

                            text-align: center;

                            color: var(--text-muted);

                            background: #0d0d0d;

                            border: 1px dashed #333333;
                            border-radius: 10px;

                            font-size: 13px;
                        }

                        /* ================================
                           SCROLLBAR
                           ================================ */

                        ::-webkit-scrollbar {
                            width: 9px;
                            height: 9px;
                        }

                        ::-webkit-scrollbar-track {
                            background: #080808;
                        }

                        ::-webkit-scrollbar-thumb {
                            background: #303030;
                            border-radius: 10px;
                        }

                        ::-webkit-scrollbar-thumb:hover {
                            background: var(--green-dark);
                        }

                        /* ================================
                           RESPONSIVE DESIGN
                           ================================ */

                        @media (max-width: 700px) {

                            .container {
                                width: 94%;
                                margin: 30px auto 45px;
                            }

                            .header {
                                padding: 24px;
                                border-radius: 14px;
                            }

                            .header-top {
                                flex-direction: column;
                                align-items: flex-start;
                            }

                            .header h1 {
                                font-size: 26px;
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

                            .summary-grid {
                                grid-template-columns:
                                    repeat(2, 1fr);
                            }

                            .execution-info {
                                grid-template-columns: 1fr;
                            }
                        }

                        @media (max-width: 450px) {

                            .summary-grid {
                                grid-template-columns: 1fr;
                            }

                            .brand {
                                align-items: flex-start;
                            }

                            .brand-icon {
                                width: 42px;
                                height: 42px;
                                font-size: 20px;
                            }

                            .test-details {
                                padding: 15px;
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

                    <div class="header-top">

                        <div class="brand">

                            <div class="brand-icon">
                                ✓
                            </div>

                            <div>
                                <div class="brand-text">
                                    Selenium Automation
                                </div>

                                <div class="brand-subtext">
                                    Automated Quality Assurance Report
                                </div>
                            </div>

                        </div>

                        <div class="execution-badge">
                            <span class="execution-dot"></span>
                            Execution Complete
                        </div>

                    </div>

                    <h1>
                        The Internet - Selenium Automation
                    </h1>

                    <p class="header-description">
                        Comprehensive automated test execution results,
                        execution findings, failure diagnostics, and
                        final browser-state evidence.
                    </p>

                    <div class="execution-info">

                        <div class="execution-info-item">
                            <span class="execution-info-label">
                                Execution Date
                            </span>
                """);

        html.append("<div class=\"execution-info-value\">")
                .append(escapeHtml(summary.getExecutionDate()))
                .append("</div></div>");

        html.append("""
                        <div class="execution-info-item">
                            <span class="execution-info-label">
                                Start Time
                            </span>
                """);

        html.append("<div class=\"execution-info-value\">")
                .append(escapeHtml(summary.getStartTime()))
                .append("</div></div>");

        html.append("""
                        <div class="execution-info-item">
                            <span class="execution-info-label">
                                End Time
                            </span>
                """);

        html.append("<div class=\"execution-info-value\">")
                .append(escapeHtml(summary.getEndTime()))
                .append("</div></div>");

        html.append("""
                        <div class="execution-info-item">
                            <span class="execution-info-label">
                                Total Duration
                            </span>
                """);

        html.append("<div class=\"execution-info-value\">")
                .append(formatDuration(summary.getDurationMillis()))
                .append("</div></div>");

        html.append("""
                    </div>

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