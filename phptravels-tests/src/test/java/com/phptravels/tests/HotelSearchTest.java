package com.phptravels.tests;

import com.phptravels.pages.HotelPage;
import com.phptravels.utils.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HotelSearchTest extends BaseTest {

    private static final String CHECK_IN_DATE  = "2025-12-15";
    private static final String CHECK_OUT_DATE = "2025-12-20";

    // -------------------------------------------------------
    // HAPPY PATH
    // -------------------------------------------------------

    @Test(description = "Happy path: searching a valid city returns hotel results")
    public void testHotelSearchValidCity() {
        HotelPage hotelPage = new HotelPage(driver);
        hotelPage.navigateTo(BASE_URL);
        hotelPage.enterLocation("Dubai");
        hotelPage.setCheckIn(CHECK_IN_DATE);
        hotelPage.setCheckOut(CHECK_OUT_DATE);
        hotelPage.clickSearch();

        Assert.assertTrue(hotelPage.areResultsDisplayed(),
            "Hotel results should be displayed for a valid city search");
    }

    @Test(description = "Happy path: results list contains at least one hotel")
    public void testHotelResultsCount() {
        HotelPage hotelPage = new HotelPage(driver);
        hotelPage.navigateTo(BASE_URL);
        hotelPage.enterLocation("London");
        hotelPage.setCheckIn(CHECK_IN_DATE);
        hotelPage.setCheckOut(CHECK_OUT_DATE);
        hotelPage.clickSearch();

        Assert.assertTrue(hotelPage.getResultCount() > 0,
            "At least one hotel should be returned for a popular city");
    }

    @Test(description = "Happy path: first hotel result has a non-empty name")
    public void testHotelResultHasName() {
        HotelPage hotelPage = new HotelPage(driver);
        hotelPage.navigateTo(BASE_URL);
        hotelPage.enterLocation("New York");
        hotelPage.setCheckIn(CHECK_IN_DATE);
        hotelPage.setCheckOut(CHECK_OUT_DATE);
        hotelPage.clickSearch();

        String firstName = hotelPage.getFirstHotelName();
        Assert.assertNotNull(firstName, "First hotel name should not be null");
        Assert.assertFalse(firstName.isEmpty(), "First hotel name should not be empty");
    }

    // -------------------------------------------------------
    // EDGE CASES
    // -------------------------------------------------------

    @Test(description = "Edge case: empty search does not crash the page")
    public void testHotelSearchEmptyFields() {
        HotelPage hotelPage = new HotelPage(driver);
        hotelPage.navigateTo(BASE_URL);
        hotelPage.clickSearch();

        Assert.assertTrue(
            driver.getCurrentUrl().contains("hotel") ||
            hotelPage.isNoResultsMessageDisplayed(),
            "Empty hotel search should be handled gracefully without a crash"
        );
    }

    @Test(description = "Edge case: check-out date before check-in is handled gracefully")
    public void testCheckOutBeforeCheckIn() {
        HotelPage hotelPage = new HotelPage(driver);
        hotelPage.navigateTo(BASE_URL);
        hotelPage.enterLocation("Dubai");
        hotelPage.setCheckIn("2025-12-20");
        hotelPage.setCheckOut("2025-12-15"); // earlier than check-in
        hotelPage.clickSearch();

        boolean handledGracefully = hotelPage.isNoResultsMessageDisplayed()
            || driver.getCurrentUrl().contains("hotel");
        Assert.assertTrue(handledGracefully,
            "Check-out before check-in should be rejected or show no results");
    }

    @Test(description = "Edge case: unknown location shows no results or an appropriate message")
    public void testHotelSearchUnknownLocation() {
        HotelPage hotelPage = new HotelPage(driver);
        hotelPage.navigateTo(BASE_URL);
        hotelPage.enterLocation("Xyzabc12345Nonexistent");
        hotelPage.setCheckIn(CHECK_IN_DATE);
        hotelPage.setCheckOut(CHECK_OUT_DATE);
        hotelPage.clickSearch();

        Assert.assertTrue(
            hotelPage.isNoResultsMessageDisplayed() || hotelPage.getResultCount() == 0,
            "Searching an unknown location should return no results"
        );
    }

    @Test(description = "Edge case: same-day check-in and check-out is handled gracefully")
    public void testSameDayCheckInCheckOut() {
        HotelPage hotelPage = new HotelPage(driver);
        hotelPage.navigateTo(BASE_URL);
        hotelPage.enterLocation("Dubai");
        hotelPage.setCheckIn(CHECK_IN_DATE);
        hotelPage.setCheckOut(CHECK_IN_DATE); // same day
        hotelPage.clickSearch();

        boolean handledGracefully = hotelPage.isNoResultsMessageDisplayed()
            || driver.getCurrentUrl().contains("hotel");
        Assert.assertTrue(handledGracefully,
            "Same-day check-in/check-out should be handled without crashing");
    }
}
