@smoke
Feature: Service health check

  Scenario: Service is up and running
    When I ping the service
    Then the response status code is 201
