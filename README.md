# RetoKarateAndDynamo

Proyecto de pruebas automáticas que combina Karate y DynamoDB.

## Objetivo

Proveer una estructura limpia para:
- ejecutar pruebas Karate con datos reales,
- validar operaciones de DynamoDB,
- mantener una configuración segura y estándar de Gradle.

## Arquitectura

La estructura se basa en convenciones Gradle:

- `src/main/java/`:
  - código de aplicación principal si se necesitara.
- `src/test/java/`:
  - clases Java de prueba y utilidades.
  - runners de Karate (`inventory.*.Element*Runner`).
  - helper de DynamoDB (`utils.DynamoHelper`).
  - fábrica de datos de prueba (`factories.ItemFactory`).
  - prueba de integración general (`inventory.ManagementTest`).
- `src/test/resources/`:
  - archivos de configuración de Karate y logging.
  - archivos `.feature` de Karate.
  - esta ubicación es la recomendada por Gradle para recursos de prueba.

## Archivos principales

### `build.gradle`
Define las dependencias y el conjunto de pruebas:

- Karate JUnit 5 runner.
- AWS SDK DynamoDB.
- JUnit 5.
- Logback y SLF4J para logging de pruebas.
- Plugins y configuración de sourceSets para usar:
  - `src/test/java`
  - `src/test/resources`

### `settings.gradle`
Nombre del proyecto Gradle.

### `./gradlew`, `./gradlew.bat`, `gradle/wrapper/gradle-wrapper.properties`
Permiten ejecutar Gradle sin depender de una instalación local específica.

### `src/test/resources/karate-config.js`
Carga la configuración de AWS de forma segura:
- usa variables de entorno `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_REGION`, `DDB_TABLE`
- evita credenciales hardcodeadas
- permite sobrescribir con `karate.properties` si es necesario

### `src/test/resources/logback-test.xml`
Configura el logging de las pruebas:
- salida por consola
- nivel `DEBUG` para Karate
- nivel `INFO` para root

### `src/test/resources/inventory/*.feature`
Contiene los escenarios de prueba:
- `create/element-create.feature`: crea elemento en DynamoDB y valida su existencia.
- `create/element-create-snippets.feature`: escenario reutilizable para creación.
- `get/element-get.feature`: consulta elementos y valida datos esperados.
- `delete/element-delete.feature`: elimina elemento y valida conteos.

### `src/test/java/utils/DynamoHelper.java`
Cliente helper para DynamoDB que encapsula:
- `putItem()`
- `getById()`
- `scanAll()`
- `deleteById()`

Incluye:
- conversión de datos entre Java y `AttributeValue`
- logging
- manejo básico de errores
- clave primaria configurable (`productId` por defecto)

### `src/test/java/utils/ItemFactory.java`
Generador de datos de prueba realistas con DataFaker:
- produce `productId`, `name`, `category`, `quantity`, `price`
- permite sobrescribir valores opcionales

### `src/test/java/inventory/ManagementTest.java`
Test JUnit que:
- ejecuta Karate en paralelo sobre la carpeta `inventory`
- genera reporte Cucumber en `build`

### `src/test/java/inventory/*/Element*Runner.java`
Runners de Karate para ejecutar cada feature desde JUnit 5.

## Por qué esta estructura

- Respeta las convenciones Gradle.
- Separa código fuente de pruebas y recursos.
- Facilita la ejecución con Gradle y herramientas IDE.
- Hace más claro qué es configuración (`src/test/resources`) y qué es lógica Java (`src/test/java`).
- Mejora seguridad al centralizar configuración sensible en variables de entorno en lugar de archivos versionados.

## Cómo ejecutar

1. Definir variables necesarias:
   - `AWS_ACCESS_KEY_ID`
   - `AWS_SECRET_ACCESS_KEY`
   - `AWS_REGION`
   - `DDB_TABLE`

2. Ejecutar todos los tests:
```bash
./gradlew test
```

3. Ejecutar un runner específico:
```bash
./gradlew test --tests inventory.get.ElementGetRunner
```

4. Revisar reporte HTML:
- resultado en `build`
- el reporte Cucumber se genera desde `inventory.ManagementTest`

## Notas

- No almacenes credenciales en el repositorio.
- Usa `.gitignore` para ignorar archivos temporales, IDE y credenciales.
- El proyecto está diseñado para pruebas de integración con DynamoDB, no para producción de aplicación backend.
