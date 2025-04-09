# SyncDriverSMU

## Descripción

**SyncDriverSMU** es una herramienta desarrollada en Java con Spring Boot que automatiza la sincronización de productos, ingredientes y datos nutricionales entre una base de datos central y balanzas de tipo HPRT utilizadas en supermercados del grupo SMU. El sistema está diseñado para realizar cargas masivas, cargas diferenciales (basadas en productos actualizados o eliminados), y para garantizar la consistencia de los datos mediante mecanismos de respaldo, validación y control.

## Tabla de Contenidos

- [Instalación](#instalación)
- [Uso](#uso)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Bibliotecas y Dependencias](#bibliotecas-y-dependencias)
- [Características](#características)
- [Licencia](#licencia)
- [Contacto](#contacto)

## Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/drachens/SyncDriverSMU.git
   cd SyncDriverSMU
   ```

2. Asegúrate de tener Java 17+ y Maven instalado.

3. Configura el archivo `application.properties`:
   ```properties
   directory.pendings=./pendings/
   directory_backup=./backup/
   DB_TYPE=informix
   DB_USER=
   DB_PASSWORD=
   SERVERNAME=
   PORT_NUMBER=
   SID=
   INFORMIX_SERVER=
   CHAR_ENCODING=en_US.8859-1
   CHAR_ENCODING_CLT=en_US.8859-1
   ```

4. Compila el proyecto:
   ```bash
   mvn clean install
   ```

5. Ejecuta la aplicación:
   ```bash
   java -jar target/SyncDriverSMU.jar
   ```

## Uso
- La ejecución principal se encuentra en `ServiceController`, el cual puede ser ejecutado como servicio o CLI.
- El sistema realiza las siguientes operaciones:
  - Consulta de balanzas habilitadas.
  - Determinación de artículos a eliminar.
  - Determinación de artículos a actualizar.
  - Generación de archivos `plu_{balId}.txt` y `note1_{balId}.txt` para ser enviados a las balanzas.
  - Eliminación y carga condicional de datos (PLU y Nota1).
  - Actualización de estado de carga por balanza.

## Tecnologías Utilizadas

- Java 17
- Spring Boot
- JDBC / JdbcTemplate
- SLF4J + Logback
- Maven
- Base de datos Informix (via JDBC)
- Arquitectura basada en capas (Controller, Service, DAO, DTO, Model)

## Bibliotecas y Dependencias

- **Spring Boot Starter**: Proporciona el núcleo de la aplicación, soporte para beans, configuración automática y ciclos de vida.
- **Spring Boot Starter JDBC**: Para la conexión y manejo de bases de datos relacionales como Informix.
- **SLF4J + Logback**: Para manejo de Logs de forma flexible y eficiente.
- **Lombok**: Reduce la verbosidad del código Java con anotaciones como `@Getter`, `@Setter`, `@Builder`, etc.
- **JNA**: Permite acceso nativo a librerias dinámicas, utilizado para integrar las funciones nativas de la balanza HPRT.
- **Informix JDBC Driver**: Para conectividad directa a bases de datos Informix.
- **Commons Lang3**: Utilidades para manipulación avanzada de Strings y validaciones.
- **HikariCP**: Alternativa de alto rendimiento para el manejo de conexiones JDBC.
- **JUnit + Mockito**: Para pruebas y test de comportamiento.

## Características

- 🔄 Sincronización automática de datos por balanza.
- 🧠 Lógica condicional para cargas masivas o diferenciales.
- 📃 Escritura de archivos compatibles con balanzas HPRT.
- 🧲 Validación y transformación de datos a través de handlers.
- 🧼 Limpieza de datos anteriores en balanza antes de recarga.
- 🧩 Sistema extensible con servicios desacoplados.

## Licencia

Este proyecto está licenciado bajo la [MIT License](LICENSE).

## Contacto

Para consultas, sugerencias o soporte técnico, puedes escribir a:

📧 ipiermartiri@marsol.cl  
🔗 [github.com/drachens](https://github.com/drachens)

