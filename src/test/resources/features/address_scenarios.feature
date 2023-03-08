Feature: Address

#  As Abneco Delivery,
#  I want to allow the sellers to have an address,
#  So that they can calculate the fee to deliver their products.

  Scenario: valid address data
    Given the following address data:
      | cep      | complemento | numero |
      | 68610970 | lado impar  | 14     |
      | 04855105 |             | 157    |
    When a request to register address with valid data is made
    Then the addres should be successfully registered


#  As Abneco Delivery,
#  I want to ensure that invalid address data is not registered in the system,
#  so that the integrity of the data is maintained.

  Scenario: invalid address data
    Given the following invalid address data:
      | cep       | complemento | numero |
      | 6861097a  | lado impar  | 14     |
      | 048551059 |             | 157    |
    When a request to register address with invalid data is made
    Then the addres shouldn't be successfully registered


#  As Abneco Delivery,
#  I want to ensure that the address number is required when registering an address,
#  so that the address information is complete and accurate.

  Scenario: null number in addressForm
    Given the address form with null number
    When a request to register address with null number is made
    Then the addres shouldn't be registered because number cannot be null


  Scenario: getting all addresses
    When request is made to get all addresses
    Then all addresses should be returned