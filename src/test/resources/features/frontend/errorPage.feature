@frontend
Feature: Error Page
  Error Page test cases

  @T0001
  Scenario Outline: T0001 - Verify if error page is displayed
    Given frontend is accessible
    When navigate to "/user1" page
    Then error page should displayed msg "It seems like we couldn't find the page you were looking for"
    And verify home button is present on error page
    When click on home page button
    Then user page should be displayed

    Examples: 
      | firstName | lastName | email         | newsletter |
      | Govardhan | Sanap    | $random_email | true       |
