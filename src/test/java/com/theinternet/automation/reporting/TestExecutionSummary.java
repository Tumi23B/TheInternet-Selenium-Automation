package com.theinternet.automation.reporting;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the complete summary of a TestNG execution.
 *
 * This class keeps suite-level information separate from the individual
 * TestResult objects, making it easier for the HTML report generator
 * to build the final execution report.
 */
public class TestExecutionSummary {

    private final String executionDate;
    private final String startTime;
    private final String endTime;
    private final long durationMillis;
    private final List<TestResult> testResults;

    public TestExecutionSummary(
            String executionDate,
            String startTime,
            String endTime,
            long durationMillis,
            List<TestResult> testResults) {

        this.executionDate = executionDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMillis = durationMillis;
        this.testResults = new ArrayList<>(testResults);
    }

    public String getExecutionDate() {
        return executionDate;
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

    public List<TestResult> getTestResults() {
        return new ArrayList<>(testResults);
    }

    public int getTotalTests() {
        return testResults.size();
    }

    public int getPassedTests() {
        return (int) testResults.stream()
                .filter(result -> "PASSED".equals(result.getStatus()))
                .count();
    }

    public int getFailedTests() {
        return (int) testResults.stream()
                .filter(result -> "FAILED".equals(result.getStatus()))
                .count();
    }

    public int getSkippedTests() {
        return (int) testResults.stream()
                .filter(result -> "SKIPPED".equals(result.getStatus()))
                .count();
    }

    public double getPassRate() {

        if (getTotalTests() == 0) {
            return 0.0;
        }

        return (getPassedTests() * 100.0) / getTotalTests();
    }
}