@regression
Feature: Get booking

  Background:
    Given an existing booking

  @smoke
  Scenario: Get an existing booking by id
    When I request the booking by id
    Then the response status code is 200
    And the returned booking matches the stored data

  Scenario: Get all booking ids
    When I request all booking ids
    Then the response status code is 200
    And the list of booking ids is not empty
    And the list of booking ids contains the created booking

  Scenario: Filter booking ids by first and last name
    When I request booking ids filtered by firstname and lastname of the booking
    Then the response status code is 200
    And the list of booking ids contains the created booking

  @negative
  Scenario: Get a non-existent booking returns 404
    When I request the booking with id 999999999
    Then the response status code is 404
