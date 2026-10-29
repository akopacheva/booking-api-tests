package com.example.booking.steps;

import com.example.booking.api.BookingApi;
import com.example.booking.context.TestContext;
import io.cucumber.java.en.Given;
import net.serenitybdd.annotations.Steps;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

public class CommonSteps {

    @Steps
    BookingApi api;

    private TestContext context() {
        return TestContext.current();
    }

    @Given("I am authenticated")
    public void iAmAuthenticated() {
        String token = api.getToken();
        assertThat(token).as("auth token").isNotBlank();
        context().setToken(token);
    }

    @Given("I am not authenticated")
    public void iAmNotAuthenticated() {
        context().setToken(null);
    }

    @Given("I use an invalid token")
    public void iUseAnInvalidToken() {
        context().setToken("invalid-token-123");
    }

    @When("I ping the service")
    public void iPingTheService() {
        context().setLastResponse(api.ping());
    }

    @Then("the response status code is {int}")
    public void theResponseStatusCodeIs(int expected) {
        assertThat(context().getLastResponse().statusCode())
                .as("HTTP status code")
                .isEqualTo(expected);
    }

    @Then("the response body matches the {string} schema")
    public void theResponseBodyMatchesSchema(String schemaFile) {
        context().getLastResponse().then().body(matchesJsonSchemaInClasspath("schemas/" + schemaFile));
    }
}
