package com.phptravels.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HotelPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Search form
    private final By locationField    = By.cssSelector("input[name='location']");
    private final By checkInField     = By.cssSelector("input[name='checkin']");
    private final By checkOutField    = By.cssSelector("input[name='checkout']");
    private final By guestsDropdown   = By.cssSelector("select[name='adults'], input[name='adults']");
    private final By searchButton     = By.cssSelector("button[type='submit']");

    // Results
    private final By hotelCard        = By.cssSelector(".hotel-item, .card-hotel");
    private final By noResultsMsg     = By.cssSelector(".no-results, .alert-warning");
    private final By hotelName        = By.cssSelector(".hotel-item .title, .card-hotel .name");

    public HotelPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "/hotels");
    }

    public void enterLocation(String location) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(locationField));
        field.clear();
        field.sendKeys(location);
        By suggestion = By.cssSelector(".autoComplete_result, .tt-suggestion");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(suggestion));
            driver.findElements(suggestion).get(0).click();
        } catch (Exception ignored) {}
    }

    public void setCheckIn(String date) {
        WebElement field = driver.findElement(checkInField);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].removeAttribute('readonly'); arguments[0].value = arguments[1];",
            field, date
        );
    }

    public void setCheckOut(String date) {
        WebElement field = driver.findElement(checkOutField);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].removeAttribute('readonly'); arguments[0].value = arguments[1];",
            field, date
        );
    }

    public void clickSearch() {
        driver.findElement(searchButton).click();
    }

    public boolean areResultsDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(hotelCard));
            return !driver.findElements(hotelCard).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public int getResultCount() {
        return driver.findElements(hotelCard).size();
    }

    public boolean isNoResultsMessageDisplayed() {
        try {
            return driver.findElement(noResultsMsg).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getFirstHotelName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(hotelName)).getText();
    }
}
