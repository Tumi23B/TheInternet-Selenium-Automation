package com.theinternet.automation.config;

import com.theinternet.automation.constants.FrameworkConstants;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Centralizes access to framework configuration, test data, and credentials.
 *
 * Keeping configuration logic here means the rest of the framework does not
 * need to know where values are stored or how they are loaded.
 *
 * Non-sensitive settings and test data are read from property files.
 * Sensitive credentials are read from environment variables so they are
 * never stored in source code or committed to Git!.
 */
public final class ConfigurationManager {

    private static final Properties CONFIG_PROPERTIES = new Properties();
    private static final Properties TEST_DATA_PROPERTIES = new Properties();

    /*
     * Load the framework configuration when this class is first used.
     * Failing early prevents the framework from running with incomplete
     * configuration and producing confusing errors later.
     */
    static {
        loadProperties(
                CONFIG_PROPERTIES,
                FrameworkConstants.CONFIG_FILE_PATH
        );

        loadProperties(
                TEST_DATA_PROPERTIES,
                FrameworkConstants.TEST_DATA_FILE_PATH
        );
    }

    /**
     * Utility class — all methods are static, so creating an instance
     * of this class is unnecessary.
     */
    private ConfigurationManager() {
    }

    /**
     * Loads a properties file from the project configuration directory.
     */
    private static void loadProperties(
            Properties properties,
            String filePath) {

        Path path = Path.of(filePath);

        try (InputStream inputStream = Files.newInputStream(path)) {
            properties.load(inputStream);
        } catch (IOException exception) {
            throw new RuntimeException(
                    "Unable to load properties file: " + filePath,
                    exception
            );
        }
    }

    /**
     * Returns a framework configuration value.
     */
    public static String getConfig(String key) {
        return getProperty(CONFIG_PROPERTIES, key);
    }

    /**
     * Returns a non-sensitive test-data value.
     *
     * Example:
     * ConfigurationManager.getTestData("valid.username");
     *
     */
    public static String getTestData(String key) {
        return getProperty(TEST_DATA_PROPERTIES, key);
    }

    /**
     * Retrieves a sensitive credential from an environment variable.
     *
     * Credentials are deliberately kept outside the project so they cannot
     * accidentally be committed to source control or exposed on GitHub.
     *
     * Example:
     * ConfigurationManager.getCredential("THE_INTERNET_PASSWORD");
     */
    public static String getCredential(String environmentVariable) {

        String value = System.getenv(environmentVariable);

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Required credential environment variable is missing: "
                            + environmentVariable
            );
        }

        return value.trim();
    }

    /**
     * Retrieves a property and validates that it exists and contains
     * a meaningful value.
     *
     * Keeping this validation centralized gives us a clear configuration
     * error instead of allowing a missing value to fail somewhere else.
     */
    private static String getProperty(
            Properties properties,
            String key) {

        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException(
                    "Property key cannot be null or empty."
            );
        }

        String value = properties.getProperty(key);

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Property not found or empty: " + key
            );
        }

        return value.trim();
    }
}