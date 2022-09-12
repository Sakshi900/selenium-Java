package org.health.webpages;

import org.health.base.BaseClass;
import org.health.utilities.TestUtility;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.IOException;
import java.util.List;

public class HMenuContainerPage extends BaseClass {

    String methodName = "";
    @FindBy(xpath = "//div[@id='hmenu-customer-profile-right']//b[contains(normalize-space(),'Hello')]")
    WebElement helloProfileText;
    @FindBy(css = "[class='hmenu hmenu-visible']>li")
    List<WebElement> hamBrgMenuItems;

    @FindBy(xpath = "//div[@class='hmenu-item hmenu-title ']")
    List<WebElement> deptTitleList;

    @FindBy(className = "hmenu-item")
    List<WebElement> deptMenuItem;

    @FindBy(xpath = "//ul[@class='hmenu hmenu-visible hmenu-translateX']//div[normalize-space()='main menu']")
    WebElement mainMenuHeader;

    @FindBy(xpath = "//a[@aria-label='Back to main menu']/ancestor::ul[@class='hmenu hmenu-visible hmenu-translateX']/li/a")
    List<WebElement> items;


    public HMenuContainerPage() {

        PageFactory.initElements(driver, this);
    }

    public void isHamBurgrOpened() throws IOException {
        methodName = "Is Hamburger opened validation";
        if (TestUtility.isElementDisplayed(driver, helloProfileText, TestUtility.EXPLICIT_WAIT)
                && TestUtility.isElementDisplayed(driver, hamBrgMenuItems.get(0), TestUtility.EXPLICIT_WAIT)) {
            TestUtility.reportStatusPass(methodName + helloProfileText.getText().contains("Hello,") + "==> "
                    + " Hamburger menu is opened and" + "HamBurger Menu items are Displayed"
                    + hamBrgMenuItems.size(), true, true);
        } else {
            TestUtility.reportStatusFail(methodName + " Error if hamburger menu is opened", true);
        }
    }

    public void scrollToDepartmentTitle(String dept) throws IOException {
        methodName = " Department Title validation";
        System.out.println("----------" + dept + "-------------");
        for (int i = 0; i < deptTitleList.size(); i++) {
            if (deptTitleList.get(i).getText().contains(dept)
                    || deptTitleList.get(i).getText().equalsIgnoreCase(dept)) {
                TestUtility.scrollToElement(driver, deptTitleList.get(i), TestUtility.EXPLICIT_WAIT);
                TestUtility.reportStatusPass(methodName + "Department Title is found", true, true);
                break;
            }
        }

    }

    public void clickOnDeptMenuItem(String menuItem) throws IOException {
        methodName = "Click on department menu Item ==> ";

        for (int i = 0; i < deptMenuItem.size(); i++) {
            if (deptMenuItem.get(i).getText().contains(menuItem)
                    || deptMenuItem.get(i).getText().equalsIgnoreCase(menuItem)) {
                TestUtility.scrollToElement(driver, deptMenuItem.get(i), TestUtility.EXPLICIT_WAIT);
                TestUtility.clickOn(driver, deptMenuItem.get(i), TestUtility.EXPLICIT_WAIT);
                TestUtility.reportStatusPass(methodName + "Department menu item found and clicked is==> " + deptMenuItem.get(i).getText(),
                        true, true);
                break;
            }
        }

    }

    public void ifDeptMenuItemIsClicked() throws IOException {
        methodName = " Dept. Menu Item click Validation";
        if (TestUtility.isElementDisplayed(driver, mainMenuHeader, TestUtility.EXPLICIT_WAIT)) {
            TestUtility.reportStatusPass(methodName + "Menu Item is Clicked", true, true);
        } else {
            TestUtility.reportStatusFail(methodName + "Menu item is not clicked", true);
        }
    }

    public void clickOnProductFromCategoryItems(String item) throws IOException {
        methodName = "Click on department menu Item ==> ";

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getText().contains(item)
                    || items.get(i).getText().equalsIgnoreCase(item)) {
                TestUtility.scrollToElement(driver, items.get(i), TestUtility.EXPLICIT_WAIT);
                TestUtility.reportStatusPass(methodName + "Item found ==> " + items.get(i).getText(),
                        true, true);
                TestUtility.clickOn(driver, items.get(i), TestUtility.EXPLICIT_WAIT);
                TestUtility.reportStatusPass(methodName + "Item found and clicked is==> " + item,
                        true, true);
                break;
            }
        }
    }
}