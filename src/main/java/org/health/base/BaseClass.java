package org.health.base;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.log4j.Logger;
import org.health.utilities.TestUtility;
import org.health.utilities.WebEventListener;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

//Base class for all the classes
public class BaseClass {

    public static WebDriver driver;
    public static Properties property; // Making public So that we can use in all Child Classes.
    public static EventFiringWebDriver e_driver;
    public static WebEventListener eventListener;
    public static Logger Log;
    public static ExtentReports extent;
    public static ExtentTest extentTest;
    public static ExtentHtmlReporter htmlReporter;

    public ITestResult result;

    // Using Base Class we're achieving Inheritance Concept from Java.
    public BaseClass() // Constructor to read data from property file.
    {
        Log = Logger.getLogger(this.getClass()); // Logger Implementation.
        try {
            property = new Properties();
            FileInputStream ip = new FileInputStream(
                    System.getProperty("user.dir")
                            + "/src/main/java/org/health/config/config.properties");
            property.load(ip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Browser launching taking place here

    @BeforeClass
    public static void initialization() throws IOException {
        String runtype = property.getProperty("RunType");
        String browserName = property.getProperty("Browser");
        Log.info("INFO: Tests are running on: " + browserName + " browser ");

        if (browserName.equals("chrome") && runtype.equals("docker")) {
            URL url = new URL("http://localhost:4444/wd/hub");
            ChromeOptions options = new ChromeOptions();
            options.setCapability("name", "chrome");
            options.addArguments("start-maximized");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--headless");

            driver = new RemoteWebDriver(url, options);
            // driver.get(baseUrl);
        } else if (browserName.equalsIgnoreCase("chrome") && runtype.equals("local")) {
            // Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.setPageLoadStrategy(PageLoadStrategy.NONE);
            options.addArguments("start-maximized");
            options.addArguments("enable-automation");
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-infobars");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-browser-side-navigation");
            options.addArguments("--disable-gpu");

            System.setProperty("webdriver.chrome.driver",
                    System.getProperty("user.dir")
                            + "/src/main/resources/chromedriver.exe");
            driver = new ChromeDriver();
        } else if (browserName.equalsIgnoreCase("firefox")) {
            //  Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
            System.setProperty("webdriver.gecko.driver",
                    System.getProperty("user.dir")
                            + "/src/main/resources/geckodriver.exe");
            driver = new FirefoxDriver();
        } else if (browserName.equals("firefox") && runtype.equals("docker")) {
            URL url = new URL("http://localhost:4444/wd/hub");
            FirefoxOptions options = new FirefoxOptions()
                    .addPreference("browser.startup.page", 1);
            driver = new RemoteWebDriver(url, options);
            // driver.get(baseUrl);
        } else {
            Log.info("Path of Driver Executable is not Set for any Browser");
        }

        e_driver = new EventFiringWebDriver(driver);
        // Now create object of EventListerHandler to register it with EventFiringWebDriver.
        eventListener = new WebEventListener();
        e_driver.register(eventListener);
        driver = e_driver;

        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(TestUtility.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(TestUtility.IMPLICIT_WAIT, TimeUnit.SECONDS);
        //launching url
        Log.info("-------------Launching URL---------------------------");
        driver.get(property.getProperty("URL"));
    }

    @BeforeTest
    public void setExtent() {
        TestUtility.setDateForLog4j();
        // Telling System Where Exactly Extent Report has to be Generated under Project.
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir")
                + "/ExtentResults/ClipBoardHealthTestReport" + ".html");

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setReportUsesManualConfiguration(true);

        htmlReporter.config().setDocumentTitle("Test Execution Summary Report");
        htmlReporter.config().setReportName("Test Execution Summary Report ");
        htmlReporter.config().setTheme(Theme.STANDARD);

        String screenShotDir = System.getProperty("user.dir")
                + "/Automation_ScreenShots/" + System.getProperty("Application") + TestUtility.getSystemDate() + ".png";
        extent.setSystemInfo("Host Name", property.getProperty("Host_Name"));
        extent.setSystemInfo("User Name", property.getProperty("User_Name"));
        extent.setSystemInfo("Environment", property.getProperty("Environment"));
        extent.setSystemInfo("Application-Name", property.getProperty("Application"));

    }

    @AfterTest
    public void endReport() {
        extent.flush();

    }

    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {

        Log.info("--------------TEST CASE FINISHED--------------------------");
        if (result.getStatus() == ITestResult.FAILURE) {
            extentTest.log(Status.FAIL, "Test Case Failed is " + result.getName()); // To Add Name in Extent Report.
            extentTest.log(Status.FAIL, "Test Case Failed is " + result.getThrowable()); // To Add Errors and
            // Exceptions in Extent
            // Report.
            Log.info("---------Capturing screenshot for fail test case------------");
            String screenshotPath = TestUtility.getScreenshot(driver, result.getName());

            extentTest.log(Status.FAIL, MarkupHelper.createLabel(result.getMethod().getMethodName()
                    + " test is failed", ExtentColor.RED));// To Add Screenshot in Extent
            // Report.
            extentTest.fail("screenshot Here :", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

        } else if (result.getStatus() == ITestResult.SKIP) {
            extentTest.log(Status.SKIP, "Test Case Skipped is " + result.getName());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            extentTest.log(Status.PASS, "Test Case Passed is " + result.getName());
        }
        // Ending Test and Ends the Current Test and Prepare to Create HTML Report.

        driver.quit();

        Log.info("-----------------------------------------------");
        Log.info("-------- Browser Terminated --------------------");
        Log.info("-----------------------------------------------");
    }

}
