package org.health.webpages;

import org.health.base.BaseClass;
import org.health.utilities.TestUtility;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewProductsPage extends BaseClass {
    String methodName = "";
    @FindBy(xpath = "//h1[normalize-space()='About this item']")
    WebElement aboutItem;

    @FindBy(xpath = "//ul[@class='a-unordered-list a-vertical a-spacing-mini']//span")
    List<WebElement> aboutItemDetails;

    public ViewProductsPage() {
        PageFactory.initElements(driver, this);
    }


    public void ifUserIsViewingProductInAnotherWindow(String brand) throws IOException, InterruptedException {
        methodName = " If Product Details Page Displays after clicking on item ==> ";
        Thread.sleep(5000);
        TestUtility.switchWindow(2);
        TestUtility.waitToBeVisble(driver, aboutItem, TestUtility.EXPLICIT_WAIT);
        if (driver.getCurrentUrl().contains(brand)
                || driver.getCurrentUrl().equalsIgnoreCase(brand)
                && aboutItem.isDisplayed()) {
            TestUtility.scrollToElement(driver, aboutItem, TestUtility.EXPLICIT_WAIT);
            TestUtility.reportStatusPass(methodName + "User is on Product details page",
                    true, true);
        } else {
            TestUtility.reportStatusFail(methodName + " User is not on Product Detail Page", true);
        }

    }

    public void getProductDetails() throws IOException {
        methodName = " Getting Product details ==>>>>>>> ";
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < aboutItemDetails.size(); i++) {
            TestUtility.scrollToElement(driver, aboutItemDetails.get(i), TestUtility.EXPLICIT_WAIT);
            list.add(aboutItemDetails.get(i).getText());
            list.add("\r\n");

        }
        Log.info(list);
        TestUtility.reportStatusPass(methodName + " " + list + " ",
                true, true);


    }
}
