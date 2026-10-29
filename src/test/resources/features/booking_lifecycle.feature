@regression @e2e
Feature: Booking lifecycle (end-to-end)

  Scenario: Create, read, update, delete a booking
    Given I am authenticated
    And I have valid booking data
    When I send a request to create the booking
    Then the response status code is 200
    And the response contains a booking id
    When I request the booking by id
    Then the response status code is 200
    And the returned booking matches the stored data
    When I partially update the booking with:
      | additionalneeds |
      | Breakfast       |
    Then the response status code is 200
    When I delete the booking
    Then the response status code is 201
    When I request the booking by id
    Then the response status code is 404
