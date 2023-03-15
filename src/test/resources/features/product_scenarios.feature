Feature: Product

  #As a seller,
  # I want to register a new product with valid data
  # so that it can be sold on the platform.

  Scenario: register product with valid product data
    Given valid product form data
    When a request to register product with valid data is made
    Then the product should be successfully registered
