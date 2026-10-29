package com.example.booking.steps;

import com.example.booking.api.BookingApi;
import com.example.booking.context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.annotations.Steps;

public class Hooks {

    @Steps
    BookingApi api;

    @Before
    public void resetContext() {
        TestContext.reset();
    }

    @After(order = 1)
    public void cleanUpCreatedBookings() {
        TestContext context = TestContext.current();
        if (context.getCreatedIds().isEmpty()) {
            return;
        }
        String token = context.getToken() != null ? context.getToken() : api.getToken();
        context.getCreatedIds().forEach(id -> api.deleteBooking(id, token));
    }
}
