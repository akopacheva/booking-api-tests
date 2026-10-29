package com.example.booking.steps;

import com.example.booking.api.BookingApi;
import com.example.booking.context.TestContext;
import com.example.booking.models.Booking;
import com.example.booking.models.BookingResponse;
import com.example.booking.utils.TestDataFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import net.serenitybdd.annotations.Steps;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingSteps {

    @Steps
    BookingApi api;

    private TestContext context() {
        return TestContext.current();
    }

    // ---------- Given ----------

    @Given("I have valid booking data")
    public void iHaveValidBookingData() {
        context().setBooking(TestDataFactory.randomBooking());
    }

    @Given("I have booking data with:")
    public void iHaveBookingDataWith(DataTable table) {
        Map<String, String> row = table.asMaps().get(0);
        Booking b = TestDataFactory.randomBooking().toBuilder()
                .firstname(row.getOrDefault("firstname", "John"))
                .lastname(row.getOrDefault("lastname", "Doe"))
                .totalprice(Integer.parseInt(row.getOrDefault("totalprice", "100")))
                .depositpaid(Boolean.parseBoolean(row.getOrDefault("depositpaid", "true")))
                .build();
        context().setBooking(b);
    }

    @Given("an existing booking")
    public void anExistingBooking() {
        iHaveValidBookingData();
        iSendARequestToCreateTheBooking();
        assertThat(context().getLastResponse().statusCode()).isEqualTo(200);
        rememberCreatedId();
    }

    // ---------- When ----------

    @When("I send a request to create the booking")
    public void iSendARequestToCreateTheBooking() {
        context().setLastResponse(api.createBooking(context().getBooking()));
    }

    @When("I send a request to create a booking with body:")
    public void iSendARequestToCreateABookingWithBody(String rawJson) {
        context().setLastResponse(api.createBookingRaw(rawJson));
    }

    @When("I request the booking by id")
    public void iRequestTheBookingById() {
        context().setLastResponse(api.getBooking(context().getBookingId()));
    }

    @When("I request the booking with id {int}")
    public void iRequestTheBookingWithId(int id) {
        context().setLastResponse(api.getBooking(id));
    }

    @When("I request all booking ids")
    public void iRequestAllBookingIds() {
        context().setLastResponse(api.getBookingIds(Map.of()));
    }

    @When("I request booking ids filtered by firstname and lastname of the booking")
    public void iRequestBookingIdsFilteredByName() {
        Booking b = context().getBooking();
        context().setLastResponse(api.getBookingIds(Map.of(
                "firstname", b.getFirstname(),
                "lastname", b.getLastname())));
    }

    @When("I update the booking with new data")
    public void iUpdateTheBookingWithNewData() {
        Booking updated = TestDataFactory.randomBooking();
        context().setBooking(updated);
        context().setLastResponse(api.updateBooking(context().getBookingId(), updated, context().getToken()));
    }

    @When("I partially update the booking with:")
    public void iPartiallyUpdateTheBookingWith(DataTable table) {
        Map<String, Object> fields = new HashMap<>();
        table.asMaps().get(0).forEach((k, v) -> {
            if (k.equals("totalprice")) fields.put(k, Integer.parseInt(v));
            else if (k.equals("depositpaid")) fields.put(k, Boolean.parseBoolean(v));
            else fields.put(k, v);
        });
        context().setLastResponse(api.patchBooking(context().getBookingId(), fields, context().getToken()));

        // keep the expected state in sync for later assertions
        Booking.BookingBuilder b = context().getBooking().toBuilder();
        if (fields.containsKey("firstname")) b.firstname((String) fields.get("firstname"));
        if (fields.containsKey("lastname")) b.lastname((String) fields.get("lastname"));
        if (fields.containsKey("totalprice")) b.totalprice((Integer) fields.get("totalprice"));
        if (fields.containsKey("depositpaid")) b.depositpaid((Boolean) fields.get("depositpaid"));
        if (fields.containsKey("additionalneeds")) b.additionalneeds((String) fields.get("additionalneeds"));
        context().setBooking(b.build());
    }

    @When("I delete the booking")
    public void iDeleteTheBooking() {
        context().setLastResponse(api.deleteBooking(context().getBookingId(), context().getToken()));
        if (context().getLastResponse().statusCode() == 201) {
            context().getCreatedIds().remove(context().getBookingId());
        }
    }

    // ---------- Then ----------

    @Then("the response contains a booking id")
    public void theResponseContainsABookingId() {
        rememberCreatedId();
        assertThat(context().getBookingId()).as("bookingid").isNotNull().isPositive();
    }

    @And("the booking in the response matches the sent data")
    public void theBookingInTheResponseMatchesTheSentData() {
        Booking actual = context().getLastResponse().as(BookingResponse.class).getBooking();
        assertThat(actual).usingRecursiveComparison().isEqualTo(context().getBooking());
    }

    @Then("the returned booking matches the stored data")
    public void theReturnedBookingMatchesTheStoredData() {
        Booking actual = context().getLastResponse().as(Booking.class);
        assertThat(actual).usingRecursiveComparison().isEqualTo(context().getBooking());
    }

    @Then("the list of booking ids is not empty")
    public void theListOfBookingIdsIsNotEmpty() {
        List<Integer> ids = context().getLastResponse().jsonPath().getList("bookingid", Integer.class);
        assertThat(ids).isNotEmpty();
    }

    @Then("the list of booking ids contains the created booking")
    public void theListContainsTheCreatedBooking() {
        List<Integer> ids = context().getLastResponse().jsonPath().getList("bookingid", Integer.class);
        assertThat(ids).contains(context().getBookingId());
    }

    @Then("the response body is {string}")
    public void theResponseBodyIs(String expected) {
        assertThat(context().getLastResponse().asString().trim()).isEqualTo(expected);
    }

    // ---------- helpers ----------

    private void rememberCreatedId() {
        Response r = context().getLastResponse();
        Integer id = r.jsonPath().getInt("bookingid");
        context().setBookingId(id);
        context().getCreatedIds().add(id);
    }
}
