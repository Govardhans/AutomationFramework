#Author: govardhan.h.sanap@gmail.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
@e2e
Feature: user End to End Test
  USER CURD test cases

  @T0001
  Scenario Outline: T0001 - Creaet User from fontend and check details are updated in backend
    Given frontend is accessible
    When navigate to user page
    Then user should landed on USER_PAGE
    And click on create user button
    Then create new user page should be displayed
    When create new user using below details
      | firstName   | lastName   | email   | newsletter   |
      | <firstName> | <lastName> | <email> | <newsletter> |
    Then user should landed on USER_PAGE
    And new user should be added in user table at last row. save user details in to "GUI_USER_DETAILS" 
    #And verify that user details on user page should be same as details provided in create user step
    And verify that "GUI_USER_DETAILS" are same as details provided in create user step
    When retrieve user using query "query{ users { uuid, email, firstName, lastName, newsletter, createdAt, lastModifiedAt }}" and email "$prv_user_email_id"
    Then verify frontend and backend user details

    Examples: 
      | firstName | lastName | email         | newsletter |
      | Govardhan | Sanap    | $random_email | true       |
  