Feature: Certificación de inventario en DynamoDB (creación → consulta → eliminación)

  Background:
    * def ItemFactory = Java.type('factories.ItemFactory')
    * def cfg = karate.get('aws')
    * def DynamoHelper = Java.type('utils.DynamoHelper')
    * def ddb = new DynamoHelper(cfg.table, cfg.key, cfg.secret, cfg.region)
    * call read("../create/element-create-snippets.feature@CreateElement")

  Scenario: 001 - Deletion of an item.
    * print 'Eliminando item:', idNewElement
    * eval ddb.deleteById(idNewElement)
    * def deleted = ddb.getById(idNewElement)
    Then match deleted == null