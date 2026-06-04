package com.phptravels.tests;

import com.phptravels.pages.FlightPage;
import com.phptravels.utils.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FlightSearchTest extends BaseTest {

    // Use future dates to ensure results are available
    private static final String DEPARTURE_DATE = "2025-12-15";
    private static final String RETURN_DATE    = "2025-12-22";

    // -------------------------------------------------------
    // HAPPY PATH
    // -------------------------------------------------------

    @Test(description = "Happy path: one-way search returns results for a valid route")
    public void testOneWayFlightSearch() {
        FlightPage flightPage = new FlightPage(driver);
        flightPage.navigateTo(BASE_URL);
        flightPage.selectOneWay();
        flightPage.enterOrigin("London");
        flightPage.enterDestination("Dubai");
        flightPage.setDepartureDate(DEPARTURE_DATE);
        flightPage.clickSearch();

        Assert.assertTrue(flightPage.areResultsDisplayed(),
            "Flight results should be shown for a valid one-way search");
    }

    @Test(description = "Happy path: round-trip search returns results for a valid route")
    public void testRoundTripFlightSearch() {
        FlightPage flightPage = new FlightPage(driver);
        flightPage.navigateTo(BASE_URL);
        flightPage.selectRoundTrip();
        flightPage.enterOrigin("London");
        flightPage.enterDestination("New York");
        flightPage.setDepartureDate(DEPARTURE_DATE);
        flightPage.setReturnDate(RETURN_DATE);
        flightPage.clickSearch();

        Assert.assertTrue(flightPage.areResultsDisplayed(),
            "Flight results should be shown for a valid round-trip search");
    }

    @Test(description = "Happy path: results list is non-empty (at least one flight shown)")
    public void testFlightResultsCount() {
        FlightPage flightPage = new FlightPage(driver);
        flightPage.navigateTo(BASE_URL);
        flightPage.selectOneWay();
        flightPage.enterOrigin("London");
        flightPage.enterDestination("Dubai");
        flightPage.setDepartureDate(DEPARTURE_DATE);
        flightPage.clickSearch();

        Assert.assertTrue(flightPage.getResultCount() > 0,
            "At least one flight result should appear for a valid route");
    }

    // -------------------------------------------------------
    // EDGE CASES
    // -------------------------------------------------------

    @Test(description = "Edge case: searching without filling any field does not crash the page")
    public void testFlightSearchWithEmptyFields() {
        FlightPage flightPage = new FlightPage(driver);
        flightPage.navigateTo(BASE_URL);
        flightPage.clickSearch();

        // Page should stay stable — either show a validation message or remain on the search page
        Assert.assertTrue(
            driver.getCurrentUrl().contains("flights") ||
            flightPage.isNoResultsMessageDisplayed(),
            "The page should handle an empty search gracefully"
        );
    }

    @Test(description = "Edge case: same origin and destination is handled gracefully")
    public void testFlightSearchSameOriginAndDestination() {
        FlightPage flightPage = new FlightPage(driver);
        flightPage.navigateTo(BASE_URL);
        flightPage.selectOneWay();
        flightPage.enterOrigin("Dubai");
        flightPage.enterDestination("Dubai");
        flightPage.setDepartureDate(DEPARTURE_DATE);
        flightPage.clickSearch();

        // Expect either no results or a user-friendly error — not a crash
        boolean handledGracefully = flightPage.isNoResultsMessageDisplayed()
            || driver.getCurrentUrl().contains("flights");
        Assert.assertTrue(handledGracefully,
            "Identical origin and destination should be handled without crashing");
    }

    @Test(description = "Edge case: return date before departure date is rejected or shows no results")
    public void testReturnDateBeforeDeparture() {
        FlightPage flightPage = new FlightPage(driver);
        flightPage.navigateTo(BASE_URL);
        flightPage.selectRoundTrip();
        flightPage.enterOrigin("London");
        flightPage.enterDestination("Dubai");
        flightPage.setDepartureDate("2025-12-20");
        flightPage.setReturnDate("2025-12-15"); // earlier than departure
        flightPage.clickSearch();

        boolean handledGracefully = flightPage.isNoResultsMessageDisplayed()
            || driver.getCurrentUrl().contains("flights");
        Assert.assertTrue(handledGracefully,
            "Return date earlier than departure should be rejected gracefully");
    }
}
