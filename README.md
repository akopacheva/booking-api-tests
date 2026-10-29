# Booking API Tests

[![API tests](https://github.com/akopachva/booking-api-tests/actions/workflows/tests.yml/badge.svg)](https://github.com/akopachva/booking-api-tests/actions)

BDD-style automated API tests for a hotel booking service, built with **Serenity BDD**, **Cucumber** and **REST Assured**.

System under test: [Restful Booker](https://restful-booker.herokuapp.com/apidoc/index.html) — a public playground API for hotel bookings.

## Tech stack

| Area | Tools |
|---|---|
| Language | Java 21 |
| BDD | Cucumber 7 (Gherkin), Serenity BDD 4 |
| HTTP client | REST Assured (via `serenity-rest-assured`) |
| Assertions | AssertJ, JSON Schema Validator |
| Test data | Datafaker |
| Build | Gradle |
| CI | GitHub Actions |

## Project structure

```
src/test/java/com/example/booking/
├── api/        BookingApi — the only place where HTTP requests are built
├── context/    TestContext — scenario-scoped shared state (DI via picocontainer)
├── models/     DTOs: Booking, BookingDates, BookingResponse, AuthRequest
├── runners/    CucumberTestSuite — JUnit 5 Platform suite
├── steps/      Cucumber step definitions and hooks
└── utils/      Config (environment handling), TestDataFactory
src/test/resources/
├── features/   Gherkin scenarios
├── schemas/    JSON schemas for response validation
└── serenity.conf
```

## Requirements

- JDK 21 (the Gradle wrapper is included, no local Gradle needed)

## Running the tests

```bash
# all scenarios
./gradlew clean test

# only smoke scenarios
./gradlew clean test -Dcucumber.filter.tags="@smoke"

# everything except negative scenarios
./gradlew clean test -Dcucumber.filter.tags="not @negative"

# run against a local instance (http://localhost:8080)
./gradlew clean test -Denvironment=local
```

The Serenity report is generated at `target/site/serenity/index.html`.

## Covered scenarios

- Health check (`/ping`)
- Create booking: positive, data-driven (Scenario Outline), empty body, malformed JSON
- Get booking: by id, list of ids, filter by name, non-existent id
- Update booking: PUT and PATCH with a valid token, without token, with an invalid token
- Delete booking: with a valid token, without token, with an invalid token
- End-to-end lifecycle: create → read → update → delete → verify 404

Each scenario creates its own data and cleans it up in an `@After` hook.

## Known quirks of the system under test

- `POST /booking` with an empty JSON body returns **500** instead of 400.
- Successful `DELETE` returns **201** instead of 204.
- Successful `GET /ping` returns **201** instead of 200.
- A multi-value `Accept` header (REST Assured's default `application/json, application/javascript, text/javascript, text/json`) makes the API answer **418 I'm a teapot**; the client sends a single `Accept: application/json` instead.

These are documented as expected values in the scenarios so that the suite stays green, and they are listed here as findings.
