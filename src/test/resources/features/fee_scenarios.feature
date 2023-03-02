Feature: Fee

  Scenario: send an null cep
    Given null cep
    When request is made with null cep
    Then RequestException is thrown explaining that field cep is mandatory

  Scenario: send an invalid cep
    Given invalid cep
    When request is made with invalid cep
    Then RequestException is thrown explaining the error

  Scenario: send a valid cep
    Given valid cep
    When request is made
    Then FeeResponse is returned according to cep
