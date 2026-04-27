Feature: Inventory Certification in DynamoDB (Update Item)

  Background:
    * def ItemFactory = Java.type('factories.ItemFactory')
    * def cfg = karate.get('aws')
    * def DynamoHelper = Java.type('utils.DynamoHelper')
    * def ddb = new DynamoHelper(cfg.table, cfg.key, cfg.secret, cfg.region)
    * call read("../create/element-create-snippets.feature@CreateElementSnippet")

  @UpdateItem
  Scenario: 001 - Update of an existing item
    * def updatedItem = new Item()
    * updatedItem.productId = idNewElement
    * updatedItem.name = "Updated Name"
    * updatedItem.category = categoryNewElement
    * updatedItem.quantity = 100
    * updatedItem.price = 200.0
    * eval ddb.putItem(updatedItem)
    * def retrieved = ddb.getById(idNewElement)
    Then match retrieved.name == "Updated Name"