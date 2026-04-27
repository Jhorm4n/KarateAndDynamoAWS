Feature: Reusable Scenario for Create Element in Data Base
  Background:
    * def ItemFactory = Java.type('factories.ItemFactory')
    * def cfg = karate.get('aws')
    * def DynamoHelper = Java.type('utils.DynamoHelper')
    * def ddb = new DynamoHelper(cfg.table, cfg.key, cfg.secret, cfg.region)

  @CreateElement
  Scenario: Intake of new elements
    * def newItem = ItemFactory.makeItem()
    * print 'Insertando:', newItem
    * eval ddb.putItem(newItem)
    * print 'Después del insert, scan:', ddb.scanAll()
    * def idNewElement = newItem.productId
    * def nameNewElement = newItem.name
    * def categoryNewElement = newItem.category
    * def quantityElement = newItem.quantity
    * def priceElement = newItem.price
