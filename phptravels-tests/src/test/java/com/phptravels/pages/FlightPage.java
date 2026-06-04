package com.phptravels.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FlightPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Search form locators
    private final By oneWayRadio       = By.cssSelector("input[value='oneway']");
    private final By roundTripRadio    = By.cssSelector("input[value='roundtrip']");
    private final By fromField         = By.cssSelector("input[name='from']");
    private final By toField           = By.cssSelector("input[name='to']");
    private final By departureDateField= By.cssSelector("input[name='departure_date']");
    private final By returnDateField   = By.cssSelector("input[name='return_date']");
    private final By searchButton      = By.cssSelector("button[type='submit']");

    // Results locators
    private final By resultsContainer  = By.cssSelector(".flight-item, .list-flights");
    private final By noResultsMsg      = By.cssSelector(".no-results, .alert-warning");

    public FlightPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "/flights");
    }

    public void selectOneWay() {
        WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(oneWayRadio));
        if (!radio.isSelected()) radio.click();
    }

    public void selectRoundTrip() {
        WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(roundTripRadio));
        if (!radio.isSelected()) radio.click();
    }

    public void enterOrigin(String origin) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(fromField));
        field.clear();
        field.sendKeys(origin);
        // Wait for and click the autocomplete suggestion
        By suggestion = By.cssSelector(".autoComplete_result, .tt-suggestion");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(suggestion));
            driver.findElements(suggestion).get(0).click();
        } catch (Exception ignored) {}
    }

    public void enterDestination(String destination) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(toField));
        field.clear();
        field.sendKeys(destination);
        By suggestion = By.cssSelector(".autoComplete_result, .tt-suggestion");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(suggestion));
            driver.findElements(suggestion).get(0).click();
        } catch (Exception ignored) {}
    }

    public void setDepartureDate(String date) {
        WebElement field = driver.findElement(departureDateField);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].removeAttribute('readonly'); arguments[0].value = arguments[1];",
            field, date
        );
    }

    public void setReturnDate(String date) {
        WebElement field = driver.findElement(returnDateField);
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
            wait.until(ExpectedConditions.visibilityOfElementLocated(resultsContainer));
            return !driver.findElements(resultsContainer).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNoResultsMessageDisplayed() {
        try {
            return wait.until(
                ExpectedConditions.visibilityOfElementLocated(noResultsMsg)
            ).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getResultCount() {
        return driver.findElements(resultsContainer).size();
    }
}
