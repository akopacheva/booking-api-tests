package com.example.booking.api;

import com.example.booking.models.AuthRequest;
import com.example.booking.models.Booking;
import com.example.booking.utils.Config;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import java.util.Map;

/**
 * Single place for all HTTP calls to the Booking API.
 * Step definitions only call these methods and never build requests themselves.
 */
public class BookingApi {

    private static final String AUTH = "/auth";
    private static final String BOOKING = "/booking";
    private static final String BOOKING_BY_ID = "/booking/{id}";
    private static final String PING = "/ping";

    private RequestSpecification spec() {
        return SerenityRest.given()
                .baseUri(Config.baseUrl())
                .contentType(ContentType.JSON)
                // Restful Booker answers 418 to REST Assured's default multi-value Accept header,
                // so we set a single explicit value.
                .header("Accept", "application/json");
    }

    @Step("Ping the service")
    public Response ping() {
        return spec().get(PING);
    }

    @Step("Request auth token for user {0}")
    public Response authenticate(String username, String password) {
        return spec().body(new AuthRequest(username, password)).post(AUTH);
    }

    public String getToken() {
        return authenticate(Config.username(), Config.password()).jsonPath().getString("token");
    }

    @Step("Create booking")
    public Response createBooking(Booking booking) {
        return spec().body(booking).post(BOOKING);
    }

    @Step("Create booking from raw body")
    public Response createBookingRaw(String rawJson) {
        return spec().body(rawJson).post(BOOKING);
    }

    @Step("Get booking {0}")
    public Response getBooking(int id) {
        return spec().pathParam("id", id).get(BOOKING_BY_ID);
    }

    @Step("Get booking ids")
    public Response getBookingIds(Map<String, String> filters) {
        return spec().queryParams(filters).get(BOOKING);
    }

    @Step("Update booking {0} (PUT)")
    public Response updateBooking(int id, Booking booking, String token) {
        return withToken(token).pathParam("id", id).body(booking).put(BOOKING_BY_ID);
    }

    @Step("Partially update booking {0} (PATCH)")
    public Response patchBooking(int id, Map<String, Object> fields, String token) {
        return withToken(token).pathParam("id", id).body(fields).patch(BOOKING_BY_ID);
    }

    @Step("Delete booking {0}")
    public Response deleteBooking(int id, String token) {
        return withToken(token).pathParam("id", id).delete(BOOKING_BY_ID);
    }

    private RequestSpecification withToken(String token) {
        RequestSpecification s = spec();
        return token == null ? s : s.cookie("token", token);
    }
}
