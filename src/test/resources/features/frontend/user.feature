#Author: govardhan.h.sanap@gmail.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
@frontend
Feature: Frontend Test Cases
  USER CURD test cases

  @T0001
  Scenario Outline: T0001 - Test create user functionality - frontend
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

    Examples: 
      | firstName | lastName | email         | newsletter |
      | Govardhan | Sanap    | $random_email | true       |

  @T0002
  Scenario Outline: T0002 - emails are unique within the system and should return an error if already used
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
    When click on create user button
    And create new user using below details
      | firstName   | lastName   | email              | newsletter   |
      | <firstName> | <lastName> | $prv_user_email_id | <newsletter> |
    Then Error message "This email has already been used" should popup

    Examples: 
      | firstName | lastName | email         | newsletter |
      | Govardhan | Sanap    | $random_email | true       |

  @T0003
  Scenario Outline: T0003 - emails have to be valid and properly formatted
    Given frontend is accessible
    When navigate to user page
    Then user should landed on USER_PAGE
    And click on create user button
    Then create new user page should be displayed
    When create new user using below details
      | firstName   | lastName   | email   | newsletter   |
      | <firstName> | <lastName> | <email> | <newsletter> |
    Then Error message "Please enter a valid email." should display under email address

    Examples: 
      | firstName | lastName | email    | newsletter |
      | Govardhan | Sanap    | @xyz.com | true       |
      | Govardhan | Sanap    | abc@xyz  | true       |
      | Govardhan | Sanap    | abc@.com | true       |
      | Govardhan | Sanap    | .com     | true       |

  @T0004
  Scenario Outline: T0004 - Delete user functionality
    Given frontend is accessible
    When navigate to user page
    Then user should landed on USER_PAGE
    And click on create user button
    Then create new user page should be displayed
    When create new user using below details
      | firstName   | lastName   | email   | newsletter   |
      | <firstName> | <lastName> | <email> | <newsletter> |
    When delete user by
      | email              |
      | $prv_user_email_id |
    Then user "$prv_user_email_id" should be removed from user table

    Examples: 
      | firstName | lastName | email         | newsletter |
      | Govardhan | Sanap    | $random_email | false      |

  @skip
  Scenario Outline: T0005 - Delete user functionality
    Given frontend is accessible
    When navigate to user page
    Then user should landed on USER_PAGE
    When delete user by
      | email   |
      | <email> |
    Then user "<email>" should be removed from user table

    Examples: 
      | email                       |
      | govardhan@gmail.com         |
      | govardhan2@gmail.com        |
      | govardhan.h.sanap@gmail.com |
      | kanchanbanger@gmail.com     |
