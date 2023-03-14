Feature: Address

#  As Abneco Delivery,
#  I want to allow the sellers to have an address,
#  So that they can calculate the fee to deliver their products.

  Scenario: register address with valid address data
    Given the following address data:
      | sellerId                      | cep      | complemento | numero |
      | asdkfjcakdjflajk-dfcladjfasdf | 68610970 | lado impar  | 14     |
      | askdfqieoakjdlcn-mnxacmnaskj  | 04855105 |             | 157    |
    When a request to register address with valid data is made
    Then the addres should be successfully registered


#  As Abneco Delivery,
#  I want to ensure that invalid address data is not registered in the system,
#  so that the integrity of the data is maintained.

  Scenario: register address with invalid address data
    Given the following invalid address data:
      | sellerId                      | cep       | complemento | numero |
      | asdkfjcakdjflajkd-fcladjfasdf | 6861097a  | lado impar  | 14     |
      | lfkajdlpoiuqglsd-kjcakdnxalsd | 048551059 |             | 157    |
    When a request to register address with invalid data is made
    Then the addres shouldn't be successfully registered


#  As Abneco Delivery,
#  I want to ensure that the address number is required when registering an address,
#  so that the address information is complete and accurate.

  Scenario: register address with null number in addressForm
    Given the address form with null number
    When a request to register address with null number is made
    Then the addres shouldn't be registered because number cannot be null

#  As Abneco Delivery,
#  I want to be able to find all addresses saved in the database.

  Scenario: getting all addresses
    When request is made to get all addresses
    Then all addresses should be returned

#  As Abneco Delivery,
#  I want any user to be able to delete their address.

  Scenario: deleting user address
    Given an address id
    When request is made to delete address by id given
    Then address is deleted

