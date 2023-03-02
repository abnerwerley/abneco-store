Feature: Seller


#  As a system,
#  I want to allow the registration of new sellers,
#  So that they can use our services.

  Scenario: Register a new seller
    Given the following seller data:
      | name         | email          | password     | phoneNumber  | cnpj           |
      | joao         | joao@gmail.com | 123456789    | 111243524503 | 99887766554433 |
      | Ana da Silva | ana@gmail.org  | ana.silva123 | 231243524503 | 12345678910123 |
    When a request to register the seller is made
    Then the seller should be successfully registered


#  As a system,
#  I want to ensure that a user with an incorrect email format cannot be registered,
#  So that I can maintain data accuracy and prevent errors.

  Scenario: shouldn't register a user with email in wrong format
    Given the following seller data with wrong email:
      | name | email      | password  | phoneNumber  | cnpj           |
      | joao | joao@gmail | 123456789 | 111243524503 | 99887766554433 |
      | ana  | ana@gmail. | 12345678  | 231243524503 | 12345678910123 |
    When a request to register the seller with incorrect email is made
    Then exception thrown explaining that email has incorrect format

#    As a system,
#    I want to ensure that a seller cannot be registered with an invalid cnpj
#    To avoid inconsistences in registration data and legal problems

  Scenario: shouldn't register a user with incorrect cnpj
    Given seller with incorrect cnpjs
      | cnpj           |
      | 9988776655443  |
      | 1234567891012a |
    When a request to register the seller with incorrect cnpj is made
    Then exception thrown explaining that cnpj should have only 14 chars and only numbers

