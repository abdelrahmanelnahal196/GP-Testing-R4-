package com.phptravels.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By emailField      = By.name("username");
    private final By passwordField   = By.name("password");
    private final By loginButton     = By.cssSelector("button[type='submit']");
    private final By errorMessage    = By.cssSelector(".alert-danger, .error-msg");
    private final By accountMenu     = By.cssSelector("li.dropdown a.dropdown-toggle");
    private final By logoutLink      = By.linkText("Logout");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "/account/login");
    }

    public void enterEmail(String email) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        field.clear();
        field.sendKeys(email);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    /** Full login helper */
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }

    public boolean isLoggedIn() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(accountMenu));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isErrorDisplayed() {
        return !getErrorMessage().isEmpty();
    }

    public void logout() {
        driver.findElement(accountMenu).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }
}
