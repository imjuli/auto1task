package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.SearchPage;

import java.text.ParseException;

public class SearchTest {

    WebDriver driver;
    SearchPage searchPage;
    String testURL = SearchPage.BASE_URL + SearchPage.BASE_PATH;

    @BeforeClass
    public void TestSetup (){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(testURL);
        searchPage = new SearchPage(driver);
    }

    @DataProvider
    public Object[] GetData() {
        return new Object[]{2015};
        //TODO figure out how add enum values to data provider
    }


    @Test(dataProvider="GetData")
    public void SearchByYearFilterByPrice(int regYear) throws ParseException {
        searchPage.setRegistrationYearFilter(regYear);
        searchPage.setSortingMethod(SearchPage.SortBy.PRICE_DESC);
        Assert.assertTrue(searchPage.isYearFilterApplied(regYear), "Filtering by registration year is incorrect:");
        Assert.assertTrue(searchPage.isListSortedByPrice(), "Sorting by price is incorrect:");
    }

    @AfterSuite
    public void Teardown(){
        driver.quit();
    }


}
