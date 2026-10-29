@regression
Feature: Create booking
  As an API client
  I want to create bookings
  So that guests can reserve rooms

  @smoke
  Scenario: Successfully create a booking with valid data
    Given I have valid booking data
    When I send a request to create the booking
    Then the response status code is 200
    And the response contains a booking id
    And the booking in the response matches the sent data
    And the response body matches the "booking-response.json" schema

  Scenario Outline: Create bookings with different deposit and price values
    Given I have booking data with:
      | firstname   | lastname   | totalprice   | depositpaid   |
      | <firstname> | <lastname> | <totalprice> | <depositpaid> |
    When I send a request to create the booking
    Then the response status code is 200
    And the response contains a booking id
    And the booking in the response matches the sent data

    Examples:
      | firstname | lastname | totalprice | depositpaid |
      | Anna      | Ivanova  | 150        | true        |
      | Max       | Petrov   | 999        | false       |
      | Olga      | Sidorova | 1          | true        |

  @negative
  Scenario: Create booking with empty body is rejected
    When I send a request to create a booking with body:
      """
      {}
      """
    Then the response status code is 500

  @negative
  Scenario: Create booking with malformed JSON is rejected
    When I send a request to create a booking with body:
      """
      { "firstname": "Broken"
      """
    Then the response status code is 400
