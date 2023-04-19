Feature: Purchase

#  As a user,
#  I want to purchase products by specifying the product and quantity that I want to purchase.
#  I want to submit a request to purchase the products, and upon successful purchase, I want to receive a confirmation.
  Scenario: Purchase products
    Given A product and quantity
    When a request to purchase is made
    Then products should be successfully purchased.

# As Abneco Delivery,
# I want to prevent users from purchasing a product with zero quantity
# To avoid any issues with the orders.
  Scenario: Purchase product zero quantity
    Given A product and 0 as quantity
    When a request to purchase 0 is made
    Then products shouldn't be purchased.