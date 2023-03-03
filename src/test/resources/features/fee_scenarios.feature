Feature: Fee

#  As Abneco Delivery,
#  I want to ensure that a request containing a null cep field is not processed,
#  So that the system can maintain data integrity and avoid errors.

  Scenario: send an null cep
    Given null cep
    When request is made with null cep
    Then RequestException is thrown explaining that field cep is mandatory

#  As Abneco Delivery,
#  I want to ensure that requests with invalid cep fields (shorter than 6 chars, and containing letters) are not processed,
#  So that I can maintain data accuracy and avoid errors.

  Scenario: send an invalid cep
    Given invalid cep
    When request is made with invalid cep
    Then RequestException is thrown explaining the error


#  As Abneco Delivery,
#  I want to allow requests with valid cep fields to be processed,
#  So that I can provide accurate fee responses to users.

  Scenario: send a valid cep
    Given valid cep
    When request is made
    Then FeeResponse is returned according to cep
