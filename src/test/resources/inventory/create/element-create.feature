Feature: Inventory Certification in DynamoDB (Item Creation)

  Background:
    * def ItemFactory = Java.type('factories.ItemFactory')
    * def cfg = karate.get('aws')
    * def DynamoHelper = Java.type('utils.DynamoHelper')
    * def ddb = new DynamoHelper(cfg.table, cfg.key, cfg.secret, cfg.region)

  @CreateItem
  Scenario: 001 - Intake of new elements
    # Given a new inventory item
    * def newItem = ItemFactory.makeItem()
    * print 'Insertando:', newItem

    # When the item is stored in DynamoDB
    * eval ddb.putItem(newItem)

    # Then the item should exist in the inventory
    * def stored = ddb.getById(newItem.productId)
    * match stored.productId == newItem.productId

