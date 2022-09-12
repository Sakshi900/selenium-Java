package org.health.webpages;

import org.health.base.BaseClass;
import org.health.utilities.TestUtility;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.IOException;

public class MainPage extends BaseClass {
    String methodName = "";
    @FindBy(xpath = "//a[@aria-label='Open Menu']")
    WebElement hamBrgrMenuBtn;

    // Initializing the Page Objects:
    public MainPage() {
        PageFactory.initElements(driver, this);
    }

    public String validateMainPageTitle() throws IOException {
        methodName = "Validate Main Page Title";
        if (driver.getTitle().equalsIgnoreCase(property.getProperty("MainPageTitle"))) {
            TestUtility.reportStatusPass(methodName + " Title is matched", true, true);
        } else {
            TestUtility.reportStatusFail(methodName + " Title validation is failed ", true);
        }
        return driver.getTitle();
    }

    public boolean isHamBrgrMenuButtonVisible() throws IOException {
        methodName = "Hamburger Menu Btn Visibility on main Page";
        if (TestUtility.isElementDisplayed(driver, hamBrgrMenuBtn, TestUtility.EXPLICIT_WAIT)) {
            TestUtility.reportStatusPass(methodName + " HamBurger Menu Btn is displayed ", true, true);
        } else {
            TestUtility.reportStatusFail(methodName + " HamBurger Menu Btn is displayed", true);
        }
        return TestUtility.isElementDisplayed(driver, hamBrgrMenuBtn, TestUtility.EXPLICIT_WAIT);
    }

    public void clickOnHamBrgrMenuButton() throws IOException {
        methodName = "  Click on hamburger Menu Item Btn ";
        TestUtility.clickOn(driver, hamBrgrMenuBtn, TestUtility.EXPLICIT_WAIT);
        TestUtility.reportStatusPass(methodName, true, true);
    }

}
