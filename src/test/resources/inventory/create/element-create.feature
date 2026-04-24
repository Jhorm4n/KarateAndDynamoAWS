Feature: Certificación de inventario en DynamoDB (creación → consulta → eliminación)

  Background:
    * def ItemFactory = Java.type('utils.ItemFactory')
    * def cfg = karate.get('aws')
    * def DynamoHelper = Java.type('utils.DynamoHelper')
    * def ddb = new DynamoHelper(cfg.table, cfg.key, cfg.secret, cfg.region)

  Scenario: Intake of new elements
    * def before = ddb.scanAll()
    * def newItem = ItemFactory.makeItem()
    * print 'Insertando:', newItem
    * eval ddb.putItem(newItem)
    * print 'Después del insert, scan:', ddb.scanAll()
    * def after = ddb.scanAll()
    Then match after[*].productId contains newItem.productId
