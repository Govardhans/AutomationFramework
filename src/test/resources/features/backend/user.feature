#Author: govardhan.h.sanap@gmail.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
@backend
Feature: backend test cases
  backend test cases template

  #To test this scenario
  #I create new user using random email address
  #then create new user using same details
  @TC0001 @createUser
  Scenario Outline: TC0001 - emails are unique within the system and should return an error if already used
    Given api is accessible
    When create new user using query "mutation ($input : CreateUserInput!) { createUser(input: $input){ ok error user{ uuid, email, firstName, lastName, newsletter } }}"
      | firstName   | lastName   | email   | newsletter   |
      | <firstName> | <lastName> | <email> | <newsletter> |
    Then created user response body should have
      | ok   | error |
      | true | null  |
    And created user's details in response body should match
      | firstName   | lastName   | email   | newsletter   |
      | <firstName> | <lastName> | <email> | <newsletter> |
    When retrieve created user using query "query{ users { uuid, email, firstName, lastName, newsletter, createdAt, lastModifiedAt }}"
    And retrieved user's details in response body should match
      | firstName   | lastName   | email   | newsletter   |
      | <firstName> | <lastName> | <email> | <newsletter> |
    When create another user using newly created users details
    Then created user response body should have
      | ok    | error           |
      | false | ALREADY_CREATED |

    Examples: 
      | firstName | lastName | email         | newsletter |
      | Govardhan | Sanap    | $random_email | true       |

  #to test this have created data driven test case
  #using invalid email address in email
  @TC0002
  Scenario Outline: TC0002 - emails have to be valid and properly formatted
    Given api is accessible
    When create new user using query "mutation ($input : CreateUserInput!) { createUser(input: $input){ ok error user{ firstName } }}"
      | firstName   | lastName   | email   | newsletter   |
      | <firstName> | <lastName> | <email> | <newsletter> |
    Then created user response body should have
      | ok    | error         |
      | false | INVALID_EMAIL |

    Examples: 
      | firstName | lastName | email    | newsletter |
      | Govardhan | Sanap    | @xyz.com | true       |
      | Govardhan | Sanap    | abc@.com | true       |
      | Govardhan | Sanap    | .com     | true       |

  @TC0003
  Scenario Outline: TC0003 - if a user is updated and the uuid does not match one in the system or is invalid errors should be returned
    Given api is accessible
    When update user using query "mutation ($input : UpdateUserInput!) { updateUser(input: $input){ ok error user{ firstName } } }"
      # uuid is "userIdNotFound
      | firstName   | lastName   | email   | newsletter   | uuid   |
      | <firstName> | <lastName> | <email> | <newsletter> | <uuid> |
    Then updated user response body should have
      | ok    | error        |
      | false | INVALID_UUID |

    Examples: 
      | firstName | lastName | email       | newsletter | uuid           |
      | Govardhan | Sanap    | abc@xyz.com | true       | userIdNotFound |

  @TC0004
  Scenario Outline: TC0004 - if a user is updated and the email is already used in the system by another account an error is returned
    Given api is accessible
    And system must have atleast one user
    When create new user using query "mutation ($input : CreateUserInput!) { createUser(input: $input){ ok error user{ uuid, email, firstName, lastName, newsletter   } }}"
      | firstName | lastName | email         | newsletter |
      | Govardhan | Sanap    | $random_email | true       |
    Then created user response body should have
      | ok   | error |
      | true | null  |
    And created user's details in response body should match
      | firstName   | lastName   | email   | newsletter   |
      | <firstName> | <lastName> | <email> | <newsletter> |
    And update user using query "mutation ($input : UpdateUserInput!) { updateUser(input: $input){ ok error user{ firstName } } }"
      | firstName   | lastName   | email            | newsletter   | uuid             |
      | <firstName> | <lastName> | $existingEmailId | <newsletter> | $createdUserUuid |
    Then updated user response body should have
      | ok    | error           |
      | false | ALREADY_CREATED |

    Examples: 
      | firstName | lastName | email       | newsletter |
      | Govardhan | Sanap    | abc@xyz.com | true       |

  @TC0005
  Scenario: TC0005 - validate create users schema
    Given api is accessible
    When create user and validate schema "jsonSchema/createUser.json" using query "mutation ($input : CreateUserInput!) { createUser(input: $input){ ok error user{ uuid,    email,    firstName,    lastName,    newsletter,    createdAt,    lastModifiedAt   } }}"
      | firstName | lastName | email         | newsletter |
      | Govardhan | Sanap    | $random_email | true       |

  @TC0006
  Scenario Outline: TC0005 - validate update users schema
    Given api is accessible
    When create new user using query "mutation ($input : CreateUserInput!) { createUser(input: $input){ ok error user{ uuid, email, firstName, lastName, newsletter   } }}"
      | firstName   | lastName   | email   | newsletter   |
      | <firstName> | <lastName> | <email> | <newsletter> |
    Then created user response body should have
      | ok   | error |
      | true | null  |
    And update user and validate schema "jsonSchema/updateUser.json" using query "mutation ($input : UpdateUserInput!) { updateUser(input: $input){ ok error user{  uuid,    email,    firstName,    lastName,    newsletter,    createdAt,    lastModifiedAt   } } }"
      | firstName   | lastName   | email   | newsletter   | uuid             |
      | <firstName> | <lastName> | <email> | <newsletter> | $createdUserUuid |

    Examples: 
      | firstName | lastName | email         | newsletter |
      | Govardhan | Sanap    | $random_email | true       |

  #####
  #   #
  #####
  @TC0007
  Scenario: TC0007 - delete all users
    Given api is accessible
    When retrieve all users using query "query{ users { uuid, email, firstName, lastName, newsletter, createdAt, lastModifiedAt }}"
    And delete all users using query "mutation ($uuid  : String!) {  deleteUser(uuid: $uuid){    ok     }}"
    When retrieve all users using query "query{ users { uuid, email, firstName, lastName, newsletter, createdAt, lastModifiedAt }}"
    Then user count shoud be 0

  @TC0008
  Scenario: TC0008 - delete user which is not in the system
    Given api is accessible
    When delete user using query "mutation ($uuid  : String!) {  deleteUser(uuid: $uuid){    ok     }}"
      | uuid         |
      | randomString |
    Then delete user response body should have
      | ok   | error           |
      | true | "error Message" |
