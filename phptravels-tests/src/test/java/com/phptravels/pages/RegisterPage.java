package com.phptravels.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By firstNameField  = By.name("firstname");
    private final By lastNameField   = By.name("lastname");
    private final By phoneField      = By.name("phone");
    private final By emailField      = By.name("email");
    private final By passwordField   = By.name("password");
    private final By signupButton    = By.cssSelector("button[type='submit']");
    private final By successMessage  = By.cssSelector(".alert-success");
    private final By errorMessage    = By.cssSelector(".alert-danger, .parsley-required");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "/account/signup");
    }

    public void fillRegistrationForm(String firstName, String lastName,
                                     String phone, String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(phoneField).sendKeys(phone);
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickSignUp() {
        driver.findElement(signupButton).click();
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
