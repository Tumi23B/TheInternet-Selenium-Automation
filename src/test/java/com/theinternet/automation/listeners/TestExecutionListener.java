package com.theinternet.automation.listeners;

import com.theinternet.automation.driver.DriverFactory;
import com.theinternet.automation.reporting.HtmlReportGenerator;
import com.theinternet.automation.reporting.TestExecutionSummary;
import com.theinternet.automation.reporting.TestResult;
import com.theinternet.automation.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Listens to TestNG test execution events.
 *
 * The listener centrally collects execution information, captures
 * screenshots, and generates the final HTML execution report.
 *
 * Individual test classes therefore remain focused on functional
 * verification rather than reporting responsibilities.
 */
public class TestExecutionListener implements ITestListener {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final List<TestResult> TEST_RESULTS =
            new ArrayList<>();

    private long suiteStartTime;

    private String suiteStartDateTime;

    private String executionDate;

    @Override
    public void onStart(ITestContext context) {

        TEST_RESULTS.clear();

        suiteStartTime =
                System.currentTimeMillis();

        suiteStartDateTime =
                LocalDateTime.now()
                        .format(DATE_TIME_FORMATTER);

        executionDate =
                LocalDate.now()
                        .toString();

        System.out.println(
                "=============================================="
        );

        System.out.println(
                "Test execution started: "
                        + suiteStartDateTime
        );

        System.out.println(
                "=============================================="
        );
    }

    @Override
    public void onTestStart(ITestResult result) {

        result.setAttribute(
                "startTime",
                System.currentTimeMillis()
        );
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        recordTestResult(
                result,
                "PASSED"
        );
    }

    @Override
    public void onTestFailure(ITestResult result) {

        recordTestResult(
                result,
                "FAILED"
        );
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        recordTestResult(
                result,
                "SKIPPED"
        );
    }

    @Override
    public void onFinish(ITestContext context) {

        long suiteEndTime =
                System.currentTimeMillis();

        long duration =
                suiteEndTime - suiteStartTime;

        String suiteEndDateTime =
                LocalDateTime.now()
                        .format(DATE_TIME_FORMATTER);

        System.out.println(
                "=============================================="
        );

        System.out.println(
                "Test execution finished: "
                        + suiteEndDateTime
        );

        System.out.println(
                "Execution duration: "
                        + duration
                        + " ms"
        );

        System.out.println(
                "Recorded test results: "
                        + TEST_RESULTS.size()
        );

        System.out.println(
                "Generating HTML report..."
        );

        TestExecutionSummary summary =
                new TestExecutionSummary(
                        executionDate,
                        suiteStartDateTime,
                        suiteEndDateTime,
                        duration,
                        TEST_RESULTS
                );

        HtmlReportGenerator reportGenerator =
                new HtmlReportGenerator();

        reportGenerator.generateReport(
                summary
        );

        System.out.println(
                "HTML report generation completed."
        );

        System.out.println(
                "=============================================="
        );
    }

    /**
     * Records an individual test execution.
     *
     * @param result TestNG execution result.
     * @param status execution status.
     */
    private void recordTestResult(
            ITestResult result,
            String status) {

        long endTime =
                System.currentTimeMillis();

        Long startTime =
                (Long) result.getAttribute("startTime");

        if (startTime == null) {
            startTime = endTime;
        }

        long duration =
                endTime - startTime;

        String startDateTime =
                formatDateTime(startTime);

        String endDateTime =
                formatDateTime(endTime);

        String screenshotBase64 =
                captureScreenshot(result);

        String failureMessage =
                extractFailureMessage(result);

        String testClassName =
                result.getTestClass()
                        .getRealClass()
                        .getSimpleName();

        String testMethodName =
                result.getMethod()
                        .getMethodName();

        TestResult testResult =
                new TestResult(
                        testClassName,
                        testMethodName,
                        status,
                        startDateTime,
                        endDateTime,
                        duration,
                        failureMessage,
                        screenshotBase64
                );

        TEST_RESULTS.add(testResult);

        System.out.println(
                status
                        + ": "
                        + testClassName
                        + " - "
                        + testMethodName
        );

        System.out.println(
                "Screenshot captured."
        );
    }

    /**
     * Captures the final browser state of the test.
     *
     * @param result TestNG execution result.
     * @return Base64 encoded screenshot.
     */
    private String captureScreenshot(
            ITestResult result) {

        WebDriver driver;

        try {
            driver =
                    DriverFactory.getDriver();

        } catch (IllegalStateException exception) {

            System.out.println(
                    "Screenshot could not be captured because "
                            + "WebDriver is no longer available."
            );

            return "";
        }

        String testClassName =
                result.getTestClass()
                        .getRealClass()
                        .getSimpleName();

        String testMethodName =
                result.getMethod()
                        .getMethodName();

        String fileName =
                testClassName
                        + "_"
                        + testMethodName;

        return ScreenshotUtils.captureScreenshot(
                driver,
                fileName
        );
    }

    /**
     * Extracts the failure message from TestNG.
     *
     * @param result TestNG execution result.
     * @return failure message or empty string.
     */
    private String extractFailureMessage(
            ITestResult result) {

        if (result.getThrowable() == null) {
            return "";
        }

        return result.getThrowable()
                .toString();
    }

    /**
     * Converts milliseconds into a formatted date/time value.
     */
    private String formatDateTime(
            long timestamp) {

        return LocalDateTime
                .ofInstant(
                        java.time.Instant.ofEpochMilli(timestamp),
                        java.time.ZoneId.systemDefault()
                )
                .format(DATE_TIME_FORMATTER);
    }

    /**
     * Provides access to the collected test results.
     *
     * @return copy of the current execution results.
     */
    public static List<TestResult> getTestResults() {

        return new ArrayList<>(
                TEST_RESULTS
        );
    }
}