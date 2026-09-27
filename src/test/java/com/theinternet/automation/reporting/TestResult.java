package com.theinternet.automation.reporting;

/**
 * Represents the execution result of a single automated test.
 *
 * The reporting layer uses this object to keep all information
 * required to display a test in the HTML execution report.
 */
public class TestResult {

    private final String testClassName;
    private final String testMethodName;
    private final String status;
    private final String startTime;
    private final String endTime;
    private final long durationMillis;
    private final String failureMessage;
    private final String screenshotBase64;

    public TestResult(
            String testClassName,
            String testMethodName,
            String status,
            String startTime,
            String endTime,
            long durationMillis,
            String failureMessage,
            String screenshotBase64) {

        this.testClassName = testClassName;
        this.testMethodName = testMethodName;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMillis = durationMillis;
        this.failureMessage = failureMessage;
        this.screenshotBase64 = screenshotBase64;
    }

    public String getTestClassName() {
        return testClassName;
    }

    public String getTestMethodName() {
        return testMethodName;
    }

    public String getStatus() {
        return status;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public String getScreenshotBase64() {
        return screenshotBase64;
    }
}