# RetoKarateAndDynamo: Automatización de Pruebas para Gestión de Inventario en DynamoDB

## 1. Resumen del Proyecto

Este proyecto es una suite de automatización de pruebas diseñada para validar la funcionalidad de un sistema de gestión de inventario que utiliza Amazon DynamoDB como base de datos. A alto nivel, automatiza las pruebas de operaciones como crear, leer, actualizar y eliminar elementos en una tabla de DynamoDB, asegurando que el sistema funcione correctamente sin necesidad de pruebas manuales cada vez.

El proyecto utiliza el Framework Karate para escribir y ejecutar estas pruebas. Karate es una herramienta que facilita la escritura de pruebas en un formato simple y legible, similar a escribir historias en inglés plano. Fue elegido porque simplifica las pruebas de API (que incluyen interacciones con bases de datos en este caso) e integra bien con proyectos Java. DynamoDB, un servicio de base de datos rápido y escalable de Amazon Web Services (AWS), fue seleccionado para el sistema de inventario porque maneja grandes cantidades de datos de manera eficiente y es comúnmente usado para aplicaciones que necesitan acceso rápido a elementos, como tiendas en línea o catálogos.

El objetivo principal de esta automatización es certificar que el sistema de inventario se comporte como se espera. Por ejemplo, verifica que cuando se crea un nuevo elemento en el inventario, se almacene correctamente en DynamoDB y pueda recuperarse después. Esto resuelve el problema de que las pruebas manuales sean lentas, propensas a errores y difíciles de repetir. Al automatizar estas verificaciones, el proyecto ayuda a detectar errores temprano, asegura la confiabilidad y permite probar el sistema rápidamente después de cambios, como actualizaciones de código o despliegues.

## 2. Tecnologías y Librerías Utilizadas

Este proyecto depende de varias herramientas y librerías para funcionar. A continuación, cada una se explica en términos simples, incluyendo qué es, por qué se usa aquí y qué problema resuelve.

- **Framework Karate**: Karate es una herramienta de código abierto para automatizar pruebas, especialmente las que involucran APIs (Interfaces de Programación de Aplicaciones, que son formas en que el software se comunica). En este proyecto, se usa para escribir y ejecutar pruebas de operaciones de DynamoDB, como crear o consultar elementos. Resuelve el problema de hacer que la escritura de pruebas sea simple y legible, incluso para principiantes, usando una sintaxis que parece inglés plano en lugar de código complejo. Esto reduce el tiempo necesario para crear pruebas y las hace más fáciles de mantener.

- **JUnit**: JUnit es una librería popular para ejecutar pruebas automatizadas en Java. Se usa aquí para ejecutar las pruebas de Karate, como se ve en las clases runner (como ElementCreateRunner.java). Resuelve el problema de necesitar una forma estándar de ejecutar y reportar pruebas en proyectos Java, asegurando que las pruebas se puedan integrar en procesos de construcción y pipelines de CI/CD (Integración Continua/Despliegue Continuo, que son formas automatizadas de construir y desplegar software).

- **Gradle**: Gradle es una herramienta de construcción que ayuda a gestionar dependencias del proyecto, compilar código y ejecutar tareas. En este proyecto, se usa para manejar la configuración del proyecto, incluyendo descargar librerías y ejecutar pruebas. Resuelve el problema de gestionar manualmente configuraciones complejas de proyectos, facilitando agregar nuevas librerías o ejecutar comandos sin errores.

- **AWS SDK para DynamoDB**: Este es un conjunto de herramientas proporcionadas por Amazon para interactuar con DynamoDB desde código Java. Se usa en clases utilitarias para realizar operaciones de base de datos como guardar o recuperar elementos. Resuelve el problema de necesitar una forma confiable de conectar y manipular datos de DynamoDB programáticamente, asegurando acceso seguro y eficiente a la base de datos.

- **Lombok**: Lombok es una librería que reduce el código repetitivo en Java generando automáticamente métodos comunes, como getters y setters. Se usa en clases modelo (como Item.java) para mantener el código limpio y enfocado. Resuelve el problema de escribir código repetitivo, que puede llevar a errores y hacer que los archivos sean más difíciles de leer.

- **cucumber-reporting**: Esta es una librería para generar reportes HTML detallados a partir de resultados de pruebas. Si se generan reportes en este proyecto, se usa para crear resúmenes amigables para el usuario de los resultados de las pruebas. Resuelve el problema de que los datos crudos de pruebas sean difíciles de entender, proporcionando reportes visuales que resaltan aprobaciones, fallos y detalles, facilitando que los equipos revisen los resultados de las pruebas.

## Arquitectura del Proyecto

Este proyecto sigue una arquitectura de automatización de pruebas basada en el patrón BDD (Desarrollo Guiado por Comportamiento), donde las pruebas se escriben en un lenguaje natural usando Karate. La arquitectura se divide en capas:

- **Capa de Pruebas (Karate Features)**: Contiene los archivos .feature que describen escenarios de prueba en Gherkin (un lenguaje simple para especificar comportamientos). Estas pruebas interactúan con la base de datos a través de llamadas a Java.

- **Capa de Utilidades (Java Classes)**: Incluye clases como DynamoHelper para manejar operaciones de bajo nivel con DynamoDB, encapsulando la lógica de acceso a datos.

- **Capa de Configuración**: Archivos como karate-config.js manejan configuraciones globales, como credenciales de AWS y hooks de limpieza.

- **Capa de Ejecución**: Runners de JUnit inician las pruebas, integradas con Gradle para construcción y ejecución.

Esta arquitectura promueve la separación de responsabilidades, reutilización y facilidad de mantenimiento, permitiendo pruebas deterministas y escalables para integraciones con DynamoDB.

## 3. Estructura del Proyecto

El proyecto está organizado en carpetas para mantener las cosas ordenadas y lógicas. Aquí hay un desglose de las carpetas principales y qué hacen:

- **src/test/java**: Esta carpeta contiene código Java relacionado con pruebas. Se divide en subcarpetas como `runners` (para clases que inician ejecuciones de pruebas, como ElementCreateRunner.java) y `utils` (para clases auxiliares, como DynamoHelper.java, que ayudan con tareas de base de datos). El propósito es separar la lógica de pruebas del código de aplicación principal, dejando claro qué es para pruebas versus producción.

- **src/test/resources**: Contiene archivos no Java para pruebas, incluyendo archivos de características de Karate (.feature, que son scripts para escenarios de prueba) y archivos de configuración. Subcarpetas pueden incluir `inventory` para características relacionadas con operaciones de inventario, y `snippets` para partes reutilizables de pruebas. Esta estructura distingue entre características de prueba ejecutables (que se ejecutan como pruebas completas) y snippets (piezas cortas y reutilizables de código de prueba).

- **src/main/java**: Contendría el código de aplicación principal si fuera una app completa, pero en este proyecto de pruebas, puede incluir clases modelo como Item.java. Estas son clases Java que representan estructuras de datos, usadas para definir cómo se ve un elemento de inventario.

- **Archivos de Configuración**: Archivos como `karate-config.js` (para configuraciones de Karate) y `build.gradle` (para configuración de Gradle) están típicamente en la raíz o en carpetas de config. Manejan configuraciones a nivel de proyecto, como cómo conectar a AWS.

En resumen, las características de prueba son los scripts de prueba reales, los snippets son ayudantes para reutilización, las clases utilitarias Java manejan tareas complejas como acceso a base de datos, y los archivos de config configuran el entorno. Esta separación asegura que el código sea reutilizable, las pruebas estén organizadas y los cambios sean fáciles de hacer sin afectar otras partes.

Para una vista más visual, aquí está la estructura de carpetas aproximada del proyecto:

```
RetoKarateAndDynamo/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── models/
│   │           └── Item.java
│   └── test/
│       ├── java/
│       │   ├── runners/
│       │   │   └── create/
│       │   │       └── ElementCreateRunner.java
│       │   └── utils/
│       │       └── DynamoHelper.java
│       └── resources/
│           ├── inventory/
│           │   ├── create/
│           │   │   └── element-create.feature
│           │   └── snippets/
│           │       └── common-steps.feature
│           └── karate-config.js
├── build.gradle
├── README.md
└── .gitignore
```

Esta estructura en árbol muestra cómo se organizan los archivos, facilitando la navegación y comprensión del proyecto.

## 4. Explicación de Clases Java Principales

Aquí profundizamos en las clases Java clave del proyecto. Cada una tiene un trabajo específico, y explicaremos por qué existe, cómo la usa Karate y cualquier decisión de diseño importante.

- **DynamoHelper**: Esta clase es una utilidad que ayuda a interactuar con DynamoDB. Existe para encapsular todas las operaciones de base de datos, como guardar o consultar elementos, para que las pruebas de Karate no necesiten manejar detalles de bajo nivel de la base de datos. Karate la usa llamando a sus métodos desde archivos de características, por ejemplo, para crear un elemento en la base de datos. Está diseñada para implementar AutoCloseable, lo que significa que cierra correctamente las conexiones de base de datos cuando termina, previniendo fugas de recursos (como mantener conexiones abiertas innecesariamente). El método getById se prefiere sobre scan porque los scans pueden ser lentos e ineficientes para tablas grandes, mientras que getById recupera directamente un elemento específico, haciendo las pruebas más rápidas y confiables. La consistencia de DynamoDB se maneja usando lecturas fuertemente consistentes cuando es necesario, asegurando que las pruebas vean los datos más recientes sin demoras.

- **Item (modelo)**: Esta es una clase Java simple que representa un elemento de inventario, con campos como ID, nombre y cantidad. Existe para proporcionar una forma fuertemente tipada de trabajar con datos, en lugar de usar Maps genéricos (que son flexibles pero propensos a errores). Karate la usa pasando objetos Item a métodos de DynamoHelper. Esta decisión de diseño mejora la seguridad del código, ya que atrapa errores de tipo en tiempo de compilación, y hace que el código sea más fácil de entender y mantener. Cada campo representa una pieza de información sobre el elemento, como su identificador único o nivel de stock.

## 5. Configuración de Karate

El archivo `karate-config.js` es un archivo JavaScript que configura ajustes globales para las pruebas de Karate. Define cosas como cómo conectar a AWS y qué hacer después de cada escenario de prueba.

Las credenciales de AWS (claves similares a nombre de usuario y contraseña para acceder a servicios de AWS) se cargan de manera flexible: primero desde propiedades del sistema Java (establecidas al ejecutar el programa), luego desde variables de entorno (establecidas en tu sistema operativo), y finalmente un fallback por defecto (como un archivo local). Este diseño permite que el proyecto se ejecute localmente en la máquina de un desarrollador (usando credenciales personales) o en entornos de CI/CD (usando credenciales automatizadas y seguras), resolviendo el problema de codificar información sensible.

El hook afterScenario se ejecuta después de cada prueba y asegura la limpieza, como cerrar conexiones de base de datos. Esto es importante porque previene que las pruebas dejen recursos abiertos, lo que podría causar problemas en pruebas subsiguientes o desperdiciar recursos del sistema.

## 6. Características y Escenarios de Karate

Una Característica en Karate es un archivo que describe un conjunto de pruebas relacionadas, como todas las pruebas para crear elementos de inventario. Representa una funcionalidad de alto nivel, como "gestión de inventario", y contiene escenarios (casos de prueba individuales) que verifican comportamientos específicos.

Las características de prueba ejecutables son las pruebas principales que se ejecutan y validan el sistema, mientras que las características snippet/ayudante son piezas cortas y reutilizables que se pueden llamar desde otras características, como un paso común de login. Esta distinción ayuda a evitar repetir código.

Las etiquetas son etiquetas agregadas a características o escenarios para organización:
- `@smoke`: Para pruebas rápidas y esenciales que verifican si lo básico funciona.
- `@regression`: Para pruebas que aseguran que nuevos cambios no rompan características existentes.
- `@snippet`: Marca características ayudantes que no deberían ejecutarse por sí solas.
- `@ignore`: Omite ciertas pruebas, útil para deshabilitarlas temporalmente.

Los snippets se excluyen de la ejecución para prevenir que se ejecuten como pruebas independientes, ya que están destinados a ser incluidos en otras características.

## 7. Ejecución de Pruebas

Para ejecutar pruebas localmente, usa comandos de Gradle en tu terminal. Antes de ejecutar, asegúrate de tener los prerrequisitos: Java 11 o superior instalado, Gradle instalado (o usa el wrapper `./gradlew`), y credenciales de AWS configuradas (ver variables abajo). Navega al directorio raíz del proyecto (`c:\Users\jbustamanteb\Proyectos\RetoKarateAndDynamo`).

Ejemplos de comandos:
- Ejecutar todas las pruebas: `./gradlew test`
- Ejecutar pruebas por etiqueta (ej. solo pruebas smoke): `./gradlew test -Dkarate.options="--tags @smoke"`
- Ejecutar un runner específico (como ElementCreateRunner): `./gradlew test --tests ElementCreateRunner`

Estos comandos asumen que Gradle está instalado y estás en la raíz del proyecto. Resuelven el problema de necesitar una forma simple de ejecutar pruebas sin configuraciones complejas.

### Variables y Configuraciones Requeridas
Para ejecutar el proyecto, debes proporcionar las siguientes variables de entorno o propiedades del sistema Java. Estas son necesarias para autenticar con AWS y acceder a DynamoDB:

- **AWS_ACCESS_KEY_ID**: Tu clave de acceso de AWS (una cadena alfanumérica que identifica tu cuenta de AWS). Se puede establecer como variable de entorno (`export AWS_ACCESS_KEY_ID=tu_clave`) o propiedad Java (`-Daws.accessKeyId=tu_clave`).
- **AWS_SECRET_ACCESS_KEY**: Tu clave secreta de AWS (una cadena más larga y segura). Similar, como variable de entorno o propiedad Java.
- **AWS_REGION**: La región de AWS donde está tu tabla de DynamoDB (ej. `us-east-1`). Establécela como variable de entorno o propiedad.
- **DYNAMODB_TABLE_NAME**: El nombre de la tabla de DynamoDB a probar (ej. `inventory-table`). Pasa esto como propiedad Java si no está codificado.

Si no se proporcionan, el proyecto puede usar un archivo de credenciales por defecto en `~/.aws/credentials`, pero se recomienda usar variables para seguridad. Asegúrate de que tu usuario de AWS tenga permisos para leer/escribir en DynamoDB. Para CI/CD, usa secretos de pipeline en lugar de variables de entorno.

Después de ejecutar, verifica los resultados en la consola y reportes generados.