package org.health.testcases;

import org.health.base.BaseClass;
import org.health.pages.AllPages;
import org.health.utilities.TestUtility;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.lang.reflect.Method;

public class GettingProductsInformationTest extends BaseClass {

    SoftAssert softAssert = new SoftAssert();

    @Test(dataProvider = "getProductData")
    public void verifyAmazonProductDetails(String dept, String deptItem, String item, String brand, String sortOf, Method method) throws IOException, InterruptedException {
        Log.info("-------------TEST CASE STARTED---------------------------");
        extentTest = extent.createTest(this.getClass().getSimpleName() + "." + method.getName());
        AllPages allPages = new AllPages();
        allPages.mainPage.validateMainPageTitle();
        allPages.mainPage.isHamBrgrMenuButtonVisible();
        allPages.mainPage.clickOnHamBrgrMenuButton();
        allPages.hMenuContainerPage.isHamBurgrOpened();
        allPages.hMenuContainerPage.scrollToDepartmentTitle(dept);
        allPages.hMenuContainerPage.clickOnDeptMenuItem(deptItem);
        allPages.hMenuContainerPage.ifDeptMenuItemIsClicked();
        allPages.hMenuContainerPage.clickOnProductFromCategoryItems(item);
        allPages.productPage.isProductPageDisplayed(item);
        allPages.productPage.selectBrandOfProduct(brand);
        allPages.productPage.sortProducts(sortOf);
        allPages.productPage.selectProductAfterSorting(2);
        allPages.viewProductsPage.ifUserIsViewingProductInAnotherWindow(brand);
        allPages.viewProductsPage.getProductDetails();
        softAssert.assertAll();

    }

    /**
     * @return
     * @throws IOException Data Provider to fetch multiple set of data and assign
     *                     them to 2D Object Array
     */
    @DataProvider
    public Object[][] getProductData() throws IOException {
        String sheetName = "ViewItem";
        Object data[][] = TestUtility.getTestData(sheetName);
        return data;
    }


}
