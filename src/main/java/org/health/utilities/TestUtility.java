package org.health.utilities;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.health.base.BaseClass;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

public class TestUtility extends BaseClass {

    // 1. These 2 variable we used in TestBase Class for Page Load and Implicit
    // Wait.
    public static long PAGE_LOAD_TIMEOUT = 20;
    public static long IMPLICIT_WAIT = 10;
    public static long EXPLICIT_WAIT = 25;

    public static String TESTDATA_SHEET_PATH = System.getProperty("user.dir")
            + "/src/main/java/org/health/testdata/ViewItemTestData.xlsx";

    static Workbook book;
    static Sheet sheet;
    static JavascriptExecutor executor;
    static SoftAssert softassert;


    // Making a 2D array to store the excel data (using in data provider)
    public static Object[][] getTestData(String sheetName) throws IOException {

        FileInputStream file = null;
        try {
            file = new FileInputStream(TESTDATA_SHEET_PATH);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        book = new XSSFWorkbook(file);
        sheet = book.getSheet(sheetName);
        Object[][] data = new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];

        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            for (int k = 0; k < sheet.getRow(0).getLastCellNum(); k++) {
                data[i][k] = sheet.getRow(i + 1).getCell(k).toString();

            }
        }
        return data;
    }

    public static void highlightElement(WebDriver driver, WebElement element) {
        executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
    }

    public static void waitToBeClicked(WebDriver driver, WebElement element, Long timeout) {
        WebDriverWait wait = new WebDriverWait(BaseClass.driver, timeout);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        highlightElement(driver, element);
    }

    public static void waitToBeSelected(WebDriver driver, WebElement element, Long timeout) {
        WebDriverWait wait = new WebDriverWait(BaseClass.driver, timeout);
        wait.until(ExpectedConditions.elementToBeSelected(element));
        highlightElement(driver, element);
    }

    public static void waitToBeVisble(WebDriver driver, WebElement element, Long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.visibilityOf(element));
        highlightElement(driver, element);
    }

    public static void waitForText(WebElement element, String text, Long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.textToBePresentInElement(element, text));
        highlightElement(driver, element);
    }

    // Explicit Wait for Click on any Element.
    public static void clickOn(WebDriver driver, WebElement element, long timeout) {
        waitToBeClicked(driver, element, timeout);
        element.click();

    }

    // Explicit Wait for Click on any Element by Javascript.
    public static void clickOnElementByJs(WebDriver driver, WebElement element, long timeout) {
        waitToBeClicked(driver, element, timeout);
        executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    // Explicit Wait for Sending Data to any Element.
    public static void sendKeys(WebDriver driver, WebElement element, long timeout, String value) {
        waitToBeVisble(driver, element, timeout);
        element.clear();
        element.sendKeys(value);
    }

    // Explicit Wait to check visibility of any Element.
    public static boolean isElementDisplayed(WebDriver driver, WebElement element, Long timeout) {
        waitToBeVisble(driver, element, timeout);
        Assert.assertTrue(element.isDisplayed());
        return element.isDisplayed();

    }

    // Method to switch to given frame
    public static void switchToGivenFrame(WebDriver driver, WebElement element, Long timeout) {
        waitToBeVisble(driver, element, timeout);

        Log.info("Frame swithing...");
        driver.switchTo().frame(element);
        Log.info("Frame switched...");
    }

    public static void scrollToElement(WebDriver driver, WebElement element, Long timeout) {
        waitToBeClicked(driver, element, timeout);
        Log.info("Scrolling to..." + element.getText());
        executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    // Method to switch to default content
    public static void switchTodefaultContent(WebDriver driver) {

        driver.switchTo().defaultContent();
    }

    // Set Date For Log4J.
    public static void setDateForLog4j() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("_ddMMyyy_hhmmss");
        System.setProperty("current_date", dateFormat.format(new Date()));
        PropertyConfigurator.configure("./src/main/resources/log4j.properties");
    }

    // Method that returns date for Extent Report
    public static String getSystemDate() {
        DateFormat dateFormat = new SimpleDateFormat("_ddMMyyyy_HHmmss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    // Method to capturing screenshots and storing it to a dedicated location
    public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException {
        // We have generated Date now.
        String dateName = new SimpleDateFormat("_ddMMyyyy_HHmmss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        // After execution, you could see a folder "FailedTestsScreenshots"
        // Under Source folder
        String destination = System.getProperty("user.dir") + "/Automation_Screenshots/" + screenshotName + dateName
                + ".png";
        File finalDestination = new File(destination);
        FileUtils.copyFile(source, finalDestination);
        return destination;
    }

    /*
     * Gives a base64 image which is used to append the screenshots in the extent
     * report. Converting to base64 format avoids screenshots broken image if sent
     * the extent report through email.
     */

    public static String getBase64Image(String screenshotpath) {
        String base64 = null;
        try {
            InputStream is = new FileInputStream(screenshotpath);
            byte[] imageBytes = IOUtils.toByteArray(is);
            base64 = Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {

        }
        return base64;

    }

    public static void selectTextFromDropDown(WebElement element, String text, Long timeout) {
        waitToBeVisble(driver, element, timeout);
        Select sel = new Select(element);
        sel.selectByVisibleText(text);
        highlightElement(driver, element);
        //waitToBeSelected(driver, element, timeout);
    }

    public static int switchWindow(int winNum) {
        Set<String> winIDs = driver.getWindowHandles();
        java.util.Iterator<String> iter = winIDs.iterator();
        int winCount = 1;
        while (winCount <= winNum) {
            String windowToBeSwitched = iter.next();
            if (winCount == winNum) {
                driver.switchTo().window(windowToBeSwitched);
                driver.manage().window().maximize();
                break;
            }
            winCount++;
        }
        return winCount;
    }

    public static void reportStatusFail(String failLog, boolean Screenshot) throws IOException {

        Log.info(failLog);

        extentTest.log(Status.FAIL, MarkupHelper.createLabel(failLog, ExtentColor.RED));
        if (Screenshot) {

            String screenshotPath = getScreenshot(driver, "");
            extentTest.fail("", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        }

        softassert.assertTrue(false);

    }

    public static void reportStatusPass(String passLog, boolean screenshot, boolean isValidationStep) throws IOException {
        Log.info(passLog);
        if (isValidationStep) {
            extentTest.log(Status.PASS, MarkupHelper.createLabel(passLog, ExtentColor.GREEN));
        }

        if (screenshot) {
            String screenshotPath = getScreenshot(driver, "");
            extentTest.pass(passLog, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        }
        if ((!isValidationStep) && (!screenshot)) {
            extentTest.log(Status.PASS, passLog);
        }

    }

}
