package org.health.webpages;

import org.health.base.BaseClass;
import org.health.utilities.TestUtility;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductPage extends BaseClass {
    String methodName = "";
    @FindBy(xpath = "//span[contains(@class,'browse-refinements-indent')]")
    List<WebElement> selectedBoldProduct;

    @FindBy(xpath = "//span[normalize-space()='Brands']")
    WebElement brandsSection;
    @FindBy(xpath = "//span[normalize-space()='Samsung']")
    WebElement brandOption;
    @FindBy(xpath = "//span[normalize-space()='Brands']/parent::div/following-sibling::ul//a/span")
    List<WebElement> brandOptions;
    @FindBy(xpath = "//div[@id='brandsRefinements']//li[@class='a-spacing-micro s-list-item']/following-sibling::li[1]//a/span")
    WebElement selectedBrandOption;
    @FindBy(xpath = "//select[@id='s-result-sort-select']")
    WebElement sortByField;
    @FindBy(xpath = "//span[normalize-space()='Sort by:']/ancestor::span[@aria-label='Sort by:']/preceding-sibling::select/option")
    List<WebElement> sortingOptions;
    @FindBy(xpath = "//span[normalize-space()='Sort by:']/following-sibling::span")
    WebElement appliedSort;

    @FindBy(xpath = "//span[@class='a-price']//span[@class='a-price-symbol']/following-sibling::span")
    List<WebElement> itemPriceList;


    public ProductPage() {
        PageFactory.initElements(driver, this);
    }

    public void isProductPageDisplayed(String item) throws IOException {
        methodName = " If Product Page Displays Validation ";
        for (int i = 0; i < selectedBoldProduct.size(); i++) {
            if (selectedBoldProduct.get(i).getText().contains(item)
                    || selectedBoldProduct.get(i).getText().equalsIgnoreCase(item)) {
                TestUtility.scrollToElement(driver, selectedBoldProduct.get(i), TestUtility.EXPLICIT_WAIT);
                TestUtility.reportStatusPass(methodName
                                + " Product page is displayed and Category item is highlighted ",
                        true, true);
                break;
            }
        }
    }

    public void selectBrandOfProduct(String brand) throws IOException {
        methodName = " Filter On Brands and Selecting a Product Brand ==> ";
        TestUtility.scrollToElement(driver, brandsSection, TestUtility.EXPLICIT_WAIT);
        for (int i = 0; i < brandOptions.size(); i++) {
            if (brandOptions.get(i).getText().equalsIgnoreCase(brand)) {
                TestUtility.clickOn(driver, brandOptions.get(i), TestUtility.EXPLICIT_WAIT);
                if (selectedBrandOption.getText().equalsIgnoreCase(brand)) {
                    TestUtility.reportStatusPass(methodName + selectedBrandOption.getText() + " is selected",
                            true, true);
                    break;
                } else {
                    TestUtility.reportStatusFail(methodName + selectedBrandOption.getText() + " is not selected",
                            true);

                }
                break;
            }

        }
    }

    public void sortProducts(String sortOption) throws IOException {
        methodName = "Sorting of products ==> ";
        TestUtility.selectTextFromDropDown(sortByField, sortOption, TestUtility.EXPLICIT_WAIT);
        if (TestUtility.isElementDisplayed(driver, appliedSort, TestUtility.EXPLICIT_WAIT)
                && appliedSort.getText().equalsIgnoreCase(sortOption)
                || appliedSort.getText().contains(sortOption)) {
            TestUtility.reportStatusPass(methodName + " Product is sorted by :" + appliedSort.getText(), true, true);
        }
    }

    public void selectProductAfterSorting(int itemPosition) throws IOException {
        methodName = "Selecting Product based upon price ==> ";

        List<WebElement> list = new ArrayList<WebElement>();
        System.out.println("======Item Price List=========is====" + itemPriceList.size());
        for (int i = 0; i < itemPriceList.size(); i++) {
            WebElement listText = itemPriceList.get(i);
            System.out.println("=================List Text Is======" + listText.getText());
            list.add(listText);
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println("======Element at the index=====" + i + " is ::" + list.get(i).getText());
            if (i == itemPosition - 1) {
                String selectedItemPrice = list.get(i).getText();
                list.get(i).click();
                TestUtility.reportStatusPass(methodName + "Item Clicked On is ==> " + selectedItemPrice, true, true);
                break;
            }
        }


    }

}
