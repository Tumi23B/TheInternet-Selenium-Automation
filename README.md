# The Internet - Selenium Automation Framework

A Java-based Selenium WebDriver automation framework built to demonstrate robust UI test automation, Page Object Model design, explicit synchronization, dynamic element handling, browser interactions, file operations, and data-driven testing.

The framework uses [The Internet](https://the-internet.herokuapp.com/) application as the system under test.

## 🚀 Project Overview

This project is designed as a practical demonstration of advanced Selenium automation concepts rather than a collection of simple UI tests.

It focuses on building a maintainable automation framework with:

* Page Object Model (POM)
* Centralized WebDriver management
* Reusable explicit wait utilities
* Configuration-driven test execution
* Externalized test data
* Screenshot capture support
* Dynamic table handling
* Window and frame switching
* JavaScript alert handling
* File upload automation
* Challenging locator strategies
* Maven-based dependency management
* TestNG test execution

## 🛠️ Technology Stack

| Technology                | Purpose                         |
| ------------------------- | ------------------------------- |
| Java 21                   | Programming language            |
| Selenium WebDriver 4.35.0 | Browser automation              |
| TestNG 7.11.0             | Test framework                  |
| Maven 3.9.6               | Build and dependency management |
| Chrome                    | Primary test browser            |
| Git & GitHub              | Version control                 |

## 📁 Project Structure

```text
TheInternet-Selenium-Automation/
│
├── .mvn/
│   ├── jvm.config
│   └── maven.config
│
├── configuration/
│   ├── config.properties
│   └── testdata.properties
│
├── test-data/
│   └── upload-test.txt
│
├── screenshots/
│   └── .gitkeep
│
├── reports/
│   └── .gitkeep
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── theinternet/
│   │               └── automation/
│   │                   ├── base/
│   │                   ├── config/
│   │                   ├── constants/
│   │                   ├── driver/
│   │                   ├── pages/
│   │                   └── utils/
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── theinternet/
│                   └── automation/
│                       ├── base/
│                       └── tests/
│
├── .gitignore
├── pom.xml
└── README.md
```

## 🧪 Automation Coverage

The framework is designed to cover the following scenarios:

### Authentication

* Valid login
* Invalid login
* Authentication success/error messages
* Logout

### Dynamic Controls

* Select checkbox
* Remove checkbox
* Verify dynamic element removal
* Add checkbox
* Verify dynamic element appears
* Explicit synchronization without `Thread.sleep()`

### Dynamic Loading

* Start dynamic loading
* Wait for content to become available
* Verify dynamically loaded content

### JavaScript Alerts

* JavaScript alert
* Confirmation alert
* Prompt alert
* Accept and dismiss actions
* Enter and validate prompt text

### Multiple Windows

* Open a new browser window
* Switch between windows
* Validate window title
* Close the secondary window
* Return to the original window

### Frames

* Switch into an iframe
* Interact with frame content
* Validate entered text
* Switch back to the parent document

### File Upload

* Upload a file using Selenium `sendKeys()`
* Verify the uploaded filename
* Avoid OS-level file picker automation

### Tables

* Read table data dynamically
* Identify specific rows
* Extract table values
* Determine highest and lowest values
* Validate dynamically calculated results

### Drag and Drop

* Identify source and target elements
* Perform drag-and-drop interaction
* Validate the resulting state

### Locator Strategies

The framework demonstrates multiple Selenium locator strategies:

* ID
* Name
* Class Name
* CSS Selector
* XPath
* Relative XPath
* XPath using text
* XPath using attributes

## 🏗️ Framework Design

The framework follows the **Page Object Model** to separate test logic from page interaction logic.

### Page Objects

Page-specific locators and interactions are maintained within dedicated page classes.

```text
pages/
├── LoginPage.java
├── DynamicControlsPage.java
├── DynamicLoadingPage.java
├── JavaScriptAlertsPage.java
├── MultipleWindowsPage.java
├── FramesPage.java
├── FileUploadPage.java
├── TablesPage.java
└── DragAndDropPage.java
```

### Utilities

Reusable framework functionality is centralized within utility classes.

```text
utils/
├── WaitUtils.java
├── ScreenshotUtils.java
├── FileUtils.java
└── TableUtils.java
```

### Configuration

Framework settings and test data are externalized from the test classes.

```text
configuration/
├── config.properties
└── testdata.properties
```

This makes it easier to maintain environments, browser settings, timeouts, and test data without modifying test implementation code.

## ⏱️ Synchronization Strategy

The framework uses **explicit waits** for dynamic web elements.

Fixed delays such as:

```java
Thread.sleep(5000);
```

are intentionally avoided.

Instead, the framework waits for meaningful browser conditions such as:

* Element visibility
* Element presence
* Element to become clickable
* Dynamic content to appear
* Page state changes

This improves test reliability and reduces unnecessary execution time.

## 📸 Failure Evidence

The framework includes screenshot support for capturing browser state when tests fail.

Screenshots are stored under:

```text
screenshots/
```

Generated screenshots are excluded from version control through `.gitignore`.

## 📊 Reports

Test execution reports are stored under:

```text
reports/
```

Generated reports are excluded from version control while the directory itself is preserved using `.gitkeep`.

## ▶️ Running the Tests

Clone the repository:

```bash
git clone https://github.com/Tumi23B/TheInternet-Selenium-Automation.git
```

Navigate into the project:

```bash
cd TheInternet-Selenium-Automation
```

Run the complete test suite:

```bash
mvn clean test
```

## ⚙️ Configuration

Framework configuration is maintained in:

```text
configuration/config.properties
```

Example:

```properties
base.url=https://the-internet.herokuapp.com
browser=chrome
explicit.wait=10
```

Test data is maintained separately in:

```text
configuration/testdata.properties
```

This separation keeps framework configuration independent from test-specific values.

## 🔐 Test Data

The project uses test data specifically for the public practice application used by this framework.

No production credentials or sensitive business information should be committed to the repository.

## 📌 Project Goals

The main objectives of this project are to demonstrate:

1. Strong Selenium WebDriver fundamentals
2. Maintainable automation architecture
3. Effective Page Object Model implementation
4. Reliable synchronization strategies
5. Dynamic web element handling
6. Reusable framework utilities
7. Clean separation between framework code and test code
8. Professional Git and Maven project practices

## 👨‍💻 Author

**Boitumelo Khauoe**

GitHub: [Tumi23B](https://github.com/Tumi23B)

LinkedIn: [Boitumelo Khauoe](https://www.linkedin.com/in/boitumelo-khauoe-464571288/)
