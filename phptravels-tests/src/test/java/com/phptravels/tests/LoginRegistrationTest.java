package com.phptravels.tests;

import com.github.javafaker.Faker;
import com.phptravels.pages.LoginPage;
import com.phptravels.pages.RegisterPage;
import com.phptravels.utils.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginRegistrationTest extends BaseTest {

    // Demo credentials from phptravels.net
    private static final String VALID_EMAIL    = "user@phptravels.com";
    private static final String VALID_PASSWORD = "demouser";

    // -------------------------------------------------------
    // LOGIN TESTS
    // -------------------------------------------------------

    @Test(description = "Happy path: valid credentials should log the user in")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo(BASE_URL);
        loginPage.login(VALID_EMAIL, VALID_PASSWORD);

        Assert.assertTrue(loginPage.isLoggedIn(),
            "User should be logged in after entering valid credentials");
    }

    @Test(description = "Edge case: wrong password shows an error")
    public void testLoginWithWrongPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo(BASE_URL);
        loginPage.login(VALID_EMAIL, "wrongpassword123");

        Assert.assertTrue(loginPage.isErrorDisplayed(),
            "An error message should appear for an incorrect password");
    }

    @Test(description = "Edge case: non-existent email shows an error")
    public void testLoginWithUnknownEmail() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo(BASE_URL);
        loginPage.login("nobody@nowhere.com", "somepassword");

        Assert.assertTrue(loginPage.isErrorDisplayed(),
            "An error message should appear for an unregistered email");
    }

    @Test(description = "Edge case: blank credentials show a validation error")
    public void testLoginWithEmptyCredentials() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo(BASE_URL);
        loginPage.clickLogin(); // submit without filling anything

        Assert.assertFalse(loginPage.isLoggedIn(),
            "Submitting empty credentials should not log the user in");
    }

    @Test(description = "Edge case: invalid email format is rejected")
    public void testLoginWithMalformedEmail() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo(BASE_URL);
        loginPage.login("not-an-email", VALID_PASSWORD);

        Assert.assertFalse(loginPage.isLoggedIn(),
            "A malformed email address should not result in a successful login");
    }

    @Test(description = "Happy path: logged-in user can successfully log out")
    public void testLogout() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo(BASE_URL);
        loginPage.login(VALID_EMAIL, VALID_PASSWORD);
        Assert.assertTrue(loginPage.isLoggedIn(), "Pre-condition: user should be logged in");

        loginPage.logout();
        Assert.assertFalse(loginPage.isLoggedIn(),
            "User should be logged out after clicking Logout");
    }

    // -------------------------------------------------------
    // REGISTRATION TESTS
    // -------------------------------------------------------

    @Test(description = "Happy path: new user can register with valid details")
    public void testSuccessfulRegistration() {
        Faker faker = new Faker();
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.navigateTo(BASE_URL);

        registerPage.fillRegistrationForm(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.phoneNumber().subscriberNumber(10),
            faker.internet().emailAddress(),
            "Test@1234!"
        );
        registerPage.clickSignUp();

        Assert.assertTrue(registerPage.isSuccessMessageDisplayed(),
            "A success message should appear after valid registration");
    }

    @Test(description = "Edge case: registering with an already-used email shows an error")
    public void testRegistrationWithDuplicateEmail() {
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.navigateTo(BASE_URL);

        registerPage.fillRegistrationForm(
            "Demo", "User",
            "0123456789",
            VALID_EMAIL,      // already registered
            "Test@1234!"
        );
        registerPage.clickSignUp();

        Assert.assertTrue(registerPage.isErrorDisplayed(),
            "An error should appear when registering with a duplicate email");
    }

    @Test(description = "Edge case: submitting a blank registration form shows validation errors")
    public void testRegistrationWithEmptyFields() {
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.navigateTo(BASE_URL);
        registerPage.clickSignUp();

        Assert.assertTrue(registerPage.isErrorDisplayed(),
            "Validation errors should appear for an empty registration form");
    }
}
