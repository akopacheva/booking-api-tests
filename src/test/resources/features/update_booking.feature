@regression
Feature: Update booking

  Background:
    Given an existing booking

  Scenario: Fully update a booking with a valid token
    Given I am authenticated
    When I update the booking with new data
    Then the response status code is 200
    And the returned booking matches the stored data

  Scenario: Partially update a booking with a valid token
    Given I am authenticated
    When I partially update the booking with:
      | firstname | totalprice |
      | Updated   | 777        |
    Then the response status code is 200
    And the returned booking matches the stored data

  @negative
  Scenario: Update without token is forbidden
    Given I am not authenticated
    When I update the booking with new data
    Then the response status code is 403

  @negative
  Scenario: Update with invalid token is forbidden
    Given I use an invalid token
    When I update the booking with new data
    Then the response status code is 403
