package com.example.booking.context;

import com.example.booking.models.Booking;
import io.restassured.response.Response;
import lombok.Data;
import net.serenitybdd.core.Serenity;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared state between the steps of ONE scenario.
 * Stored in the Serenity test session, which is reset for every scenario (see Hooks).
 */
@Data
public class TestContext {

    private static final String KEY = "testContext";

    private Booking booking;
    private Integer bookingId;
    private String token;
    private Response lastResponse;
    /** Ids created during the scenario; deleted in the @After hook. */
    private final List<Integer> createdIds = new ArrayList<>();

    public static TestContext current() {
        if (!Serenity.hasASessionVariableCalled(KEY)) {
            Serenity.setSessionVariable(KEY).to(new TestContext());
        }
        return Serenity.sessionVariableCalled(KEY);
    }

    public static void reset() {
        Serenity.setSessionVariable(KEY).to(new TestContext());
    }
}
