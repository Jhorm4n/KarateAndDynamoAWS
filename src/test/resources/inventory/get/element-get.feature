Feature: Inventory Certification in DynamoDB (Get Item)

  Background:
    * def cfg = karate.get('aws')
    * def DynamoHelper = Java.type('utils.DynamoHelper')
    * def ddb = new DynamoHelper(cfg.table, cfg.key, cfg.secret, cfg.region)

    @GetItem
  Scenario Outline: 001 - Query of item in the table
    # Given an existing inventory item id
    * print 'Querying item with id:', productId

    # When the item is queried from DynamoDB
    * def item = ddb.getById(productId)
    * print 'Element:', item


    # Then the item data should match the expected values
    Then match name == item.name
    And match parseFloat(price) == item.price
    And match parseInt(quantity) == item.quantity

    Examples:
      |productId                             |name                      |price   |quantity|
      |p-31107adc-3502-416e-8492-9ef7e7254ffa|Fantastic Marble Lamp     | 431.92 |   86   |
      |p-0845cf2e-ef7d-4b4c-9b04-69e7660cd387|Heavy Duty Concrete Table | 127.24 |   40   |
