Feature: Certificación de inventario en DynamoDB (creación → consulta → eliminación)

  Background:
    * def ItemFactory = Java.type('utils.ItemFactory')
    * def cfg = karate.get('aws')
    * def DynamoHelper = Java.type('utils.DynamoHelper')
    * def ddb = new DynamoHelper(cfg.table, cfg.key, cfg.secret, cfg.region)
    * call read("../create/element-create-snippets.feature@CreateElement")

  Scenario: Deletion of an item.
    * def before = ddb.scanAll()
    * def beforeCount = before.length
    * print 'Elements:', before
    * ddb.deleteById(idNewElement)
    * def after = ddb.scanAll()
    * print 'Elements:', after
    * def afterCount = after.length
    Then match afterCount == beforeCount - 1
    And match after[*].productId !contains newItem.productId
