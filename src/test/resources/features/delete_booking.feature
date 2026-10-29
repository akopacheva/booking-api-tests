@regression
Feature: Delete booking

  Background:
    Given an existing booking

  Scenario: Delete a booking with a valid token
    Given I am authenticated
    When I delete the booking
    Then the response status code is 201
    When I request the booking by id
    Then the response status code is 404

  @negative
  Scenario: Delete without token is forbidden
    Given I am not authenticated
    When I delete the booking
    Then the response status code is 403

  @negative
  Scenario: Delete with invalid token is forbidden
    Given I use an invalid token
    When I delete the booking
    Then the response status code is 403
