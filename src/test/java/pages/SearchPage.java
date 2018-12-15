package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SearchPage {

    WebDriver driver;
    public static final String BASE_PATH = "/de/search";
    public static final String BASE_URL = "https://www.autohero.com";
    public enum SortBy {
        YEAR_DESC(0),
        PRICE_ASC(1),
        PRICE_DESC(2),
        MILES_ASC(3),
        MILES_DESC(4),
        MANUFACTURER_ASC(5),
        MANUFACTURER_DESC(6);

        public final int sortIndex;

        SortBy(int sortIndex) {
            this.sortIndex = sortIndex;
        }
    }

    @FindBy(css = "[data-qa-selector=filter-year]>.label___3agdr")
    public WebElement lblYearFilter;

    @FindBy(name = "yearRange.min")
    public WebElement yearFilter;

    @FindBy(name = "sort")
    public WebElement selectSort;

    @FindBy(css = "[data-qa-selector=spec-list] li:first-child")
    public List<WebElement> registrationDates;

    @FindBy (css = "[data-qa-selector=price]")
    public List<WebElement> pricesList;

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void setRegistrationYearFilter(int year){
        lblYearFilter.click();
        Select selectYearFilter = new Select(yearFilter);
        selectYearFilter.selectByVisibleText(Integer.toString(year));
        waitForResultsToLoad();
    }

    public void waitForResultsToLoad(){
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("loading___1v1Pd")));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("specItem___2gMHn")));

    }

    public boolean isYearFilterApplied(int regYear) throws ParseException {

        int i = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm/yyyy");
        Calendar calendar = Calendar.getInstance();

        for (WebElement el: registrationDates
            ) {
            Date date = simpleDateFormat.parse(el.getText().substring(2));
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            if (year >= regYear) {
                    i++;
            }
        }
        //TODO: add pagination
        return i == registrationDates.size();
    }

    public void setSortingMethod(SortBy sortBy){
        Select sortSelector = new Select(selectSort);
        sortSelector.selectByIndex(sortBy.sortIndex);
        waitForResultsToLoad();
    }


    public boolean isListSortedByPrice(){
        ArrayList<Integer> priceArray = new ArrayList<Integer>();
        for (WebElement el: pricesList
             ) {
            String strPrice = el.getText();
            Integer price = Integer.parseInt(strPrice.substring(0, strPrice.indexOf(" ")).replace(".", ""));
            priceArray.add(price);

        }

        List tmp = new ArrayList(priceArray);
        Collections.sort(tmp, Collections.reverseOrder());
        return tmp.equals(priceArray);

    }




}
