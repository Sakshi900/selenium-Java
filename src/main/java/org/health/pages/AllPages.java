package org.health.pages;

import org.health.webpages.HMenuContainerPage;
import org.health.webpages.MainPage;
import org.health.webpages.ProductPage;
import org.health.webpages.ViewProductsPage;

public class AllPages {

    public MainPage mainPage;
    public HMenuContainerPage hMenuContainerPage;
    public ProductPage productPage;

    public ViewProductsPage viewProductsPage;

    public AllPages() {

        mainPage = new MainPage();
        hMenuContainerPage = new HMenuContainerPage();
        productPage = new ProductPage();
        viewProductsPage = new ViewProductsPage();

    }

}
