Feature: Inventory Certification in DynamoDB (Delete Item)

  Background:
    * def ItemFactory = Java.type('factories.ItemFactory')
    * def cfg = karate.get('aws')
    * def DynamoHelper = Java.type('utils.DynamoHelper')
    * def ddb = new DynamoHelper(cfg.table, cfg.key, cfg.secret, cfg.region)
    * call read("../create/element-create-snippets.feature@CreateElementSnippet")

    @DeleteItem
  Scenario: 001 - Deletion of an item.
    # Given an existing inventory item
    * print 'Eliminando item:', idNewElement
    # When the item is deleted from DynamoDB
    * eval ddb.deleteById(idNewElement)
    # Then the item should no longer exist
    * def deleted = ddb.getById(idNewElement)
    Then match deleted == null