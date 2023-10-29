# Todo-App con Java, Maven, Swing, MySQL y Docker.

Este proyecto fue realizado como presentación para la **Academia Java + Springboot**, dictada por **Softtek** en 
conjunto con la **Universidad Siglo-XII**. El objetivo del proyecto fue desarrollar una aplicación de gestión de 
tareas en **Java**, donde se realice una conexión a una base de datos en **MySQL** para manejar la persistencia 
de datos. Para la interfaz de usuario, se eligió trabajar con **Swing**, una decisión basada en lo que estamos 
aprendiendo actualmente en el curso y para poner en práctica los conocimientos adquiridos hasta el momento.

![Funcionamiento de la aplicacion](https://github.com/nicomuros/actividad-m3-acsofttek/blob/main/capturas/Comprobacion%20con%20MySQL.gif?raw=true)
#
En la grabación superior se pueden observar las distintas etapas del CRUD, junto con el manejo de errores y el feedback
de los mismos al usuario:
* **Create:** Para poder realizar la carga de datos con éxito a la base de datos, es necesario que el usuario ingrese 
de forma **obligatoria**, tanto el título como la descripción de la tarea (tampoco puede ser una cadena de espacios en blanco).
En la grabación se puede observar como, en el momento en que el usuario intenta ingresar una tarea con formatos invalidos
se muestra un mensaje de error.
* **Read**: Las operaciones de lectura a la base de datos se realizan de forma automática cuando el usuario inicia la 
aplicación y cada vez que realiza una operación con éxito.
* **Update**: El proceso de actaualización de una tarea requiere que el usuario proporcione el identificador (seleccionando
la tarea en la tabla), además de al menos una modificación en el titulo o la descripción de la misma. Como cuando se añade
una tarea, es necesario que el nuevo título o descripción estén vacíos.
* **Delete**: Para poder eliminar una tarea, el usuario debe primero seleccionarla en la tabla, para poder obtener su
identificador. Cuando esta condición no se cumple, se muestra un mensaje de error al usuario.

En el [siguiente enlace](https://github.com/nicomuros/actividad-m3-acsofttek/blob/main/diagramas/Diagrama%20de%20secuencia.png?raw=true)
se puede ver un **diagrama de secuencia** que describe el comportamiento de la aplicación en cada una de las etapas.

# Uso de la aplicación
## Dependencias
Para poder ejecutar el proyecto es necesario tener instaladas y configuradas las siguientes dependencias: 

* **[Maven 3.9+](https://maven.apache.org/download.cgi)** 
* **[JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)**
* **[MySQL 8.0.33](https://downloads.mysql.com/archives/installer/)**
* **[Git](https://git-scm.com/downloads)**

Es importante tener configuradas las variables de entorno `JAVA_HOME` y `MAVEN_HOME` para ejecutar correctamente el 
empaquetado y la iniciación de la aplicación. 
## Instalación

Siga estos pasos para instalar la aplicación:

1. Clonar el repositorio desde GitHub al sistema local. Puedes hacerlo utilizando Git y ejecutando el siguiente comando en tu terminal:

`git clone https://github.com/nicomuros/actividad-m2-acsofttek.git`

2. Navegar al directorio de la aplicación:

`cd actividad-m3-acsofttek`

3. Utilizar Maven para compilar y construir la aplicación. Para ello, ejecutar el siguiente comando:

`mvn clean package`

Esto compilará el proyecto y creará un archivo JAR ejecutable en la carpeta "target", y creará un directorio /lib con las
librerias necesarias para poder ejecutar la aplicación.

4. Desplazarse al directorio /target.

`cd target`

5. Iniciar MySQL y crear una base de datos llamada `softtek`. Asegurarse que esté escuchando el puerto **3306**. Las
credenciales que posee de forma nativa la aplicación son `username: muros` `password: password`. En el directorio del 
proyecto, se encuentra un archivo `docker-compose.yml` con el cual levantar un contenedor de una imagen de MySQL 8.0.33 
con las configuraciones necesarias para ejecutar la aplicación.


6. Para levantar la base de datos usando el archivo docker-compose que se incluye en repositorio, asegurate de tener instalado
Docker y Docker Compose en tu sistema. Si no los tienes, puedes instalarlos desde sus webs oficiales. Yo recomiendo instalar
**[Docker Desktop](https://www.docker.com/products/docker-desktop/)**, que incluye Docker engine, Docker compose y la interfaz
CLI.


7. En la terminal de comandos, dirigirse al directorio del proyecto donde se encuentra el archivo docker-compose.yml
y ejecutar el comando:

`docker-compose up -d`
8. Ejecuta la aplicación JAR con el siguiente comando, reemplazando "nombre-del-archivo.jar" con el nombre real del archivo JAR generado:
`java -jar nombre-del-archivo.jar`

La aplicación se ejecutará y podrás interactuar con ella a través de la interfáz gráfica.

# Arquitectura de la aplicación.

## Plugins 

* **[MySQL Connector Java](https://mvnrepository.com/artifact/mysql/mysql-connector-java)**: Es un conector JDBC que proporciona
las herramientas necesarias para gestionar la conexión y las consultas de manera eficiente y segura
* **[Maven Dependency Plugin](https://mvnrepository.com/artifact/org.apache.maven.plugins/maven-dependency-plugin)**: Permite gestionar
las dependencias del proyecto, en este caso se utiliza para copiar todas las dependencias a una carpeta particular en el momento
en que se realiza el empaquetado de la aplicación a un archivo .jar logrando, de esta forma, que el proyecto tenga lo necesario
para poder ejecutarse de forma apropiada.
* **[Maven Jar Plugin](https://mvnrepository.com/artifact/org.apache.maven.plugins/maven-jar-plugin)**: En el proyecto se configuró
para que, junto con `Maven Dependency Plugin`, permita ser empaquetado a un archivo .jar junto con sus dependencias.

## Patrones de diseño

### Estructura N-Tier
La estructura N-Tier o el diseño en capas permite la separación de responsabilidades de cada componente de la aplicación,
lo cual lleva a una arquitectura modular y escalable. En esta arquitectura, se dividen las funcionalidades de la aplicación en varias capas o 
niveles claramente definidos, cada una con un propósito específico, lo cual es una buena dirección para poder implementar el principio de 
responsabilidad única:

1. **Capa de Presentación (`/vista`):** Esta capa corresponde a la interfaz de usuario y se encarga de la presentación visual 
de la aplicación. En el contexto de una aplicación Java Swing, esta capa sería responsable de crear y gestionar las ventanas, 
formularios y elementos de la interfaz de usuario. Se comunica con la capa de servicio para solicitar y mostrar datos.

2. **Capa de Servicio (`/servicio`):** Coordina la interacción entre la capa de presentación y la capa de acceso a datos. Además
se encarga de realizar validaciones y de preparar los datos antes de ser enviados a la capa de acceso a datos.

3. **Capa de Acceso a Datos (`/repositorio`):** La capa de acceso a datos se encarga de la interacción con la base de datos. 
Incluye clases y componentes que se conectan a la base de datos, realizan consultas y operaciones CRUD
(Crear, Leer, Actualizar, Eliminar). Aquí es donde se gestionan las conexiones a la base de datos y se ejecutan consultas SQL.

4. **Capa de Modelo (`/modelo`):** En esta capa, se representan los datos y la estructura de la aplicación. Contiene las 
clases y estructuras de datos que reflejan la información que se almacena en la base de datos. El modelo puede incluir 
también la lógica de validación de datos.

5. **Capa de base de datos (`/db`):** Esta capa provee las configuraciones necesarias del Driver de MySQL y JDBC para poder
realizar las conexiones a la base de datos

5. **Capa de Excepciones (`/excepciones`):** Aquí se encuentran las excepciones personalizadas de la aplicación. Heredan directamente
de runtime excepcion.

[Aqui](https://github.com/nicomuros/actividad-m3-acsofttek/blob/main/diagramas/Diagrama%20de%20clases.png?raw=true) se encuentra un
diagrama de clases que ayuda a describir el tipo de relación que existe entre las distintas capas.

### Inyección de dependencias

El patrón de Inyección de Dependencias es una parte fundamental del diseño de la aplicación y ofrece una serie de ventajas 
clave en el enfoque de desarrollo:

#### Desacoplamiento
El patrón de Inyección de Dependencias promueve el desacoplamiento entre los componentes de la aplicación. Esto significa que 
las clases no están fuertemente ligadas a sus dependencias, lo que facilita la sustitución o el cambio de componentes sin 
afectar otras partes del sistema. Esta flexibilidad es esencial para mantener y evolucionar la aplicación con confianza.

#### Pruebas Unitarias
La inyección de dependencias simplifica el proceso de prueba unitaria. Se pueden proporcionar implementaciones de dependencias 
simuladas o en blanco durante las  (por ejemplo con Mockito), lo que permite aislar y probar componentes individualmente. 

#### Preparar la aplicación para la inversión de control

La Inyección de Dependencias (IoC) prepara nuestra aplicación para la implementación de Inversión de Control (IoC) proporcionada
por Spring en los Beans: instancias de la aplicación ubicadas en el **Spring Container** como únicas para cuando se necesiten.

### DAO
La interfaz `TareaRepositorio` define métodos que se encargan de la interacción con la base de datos, como agregar, seleccionar, verificar, 
modificar y eliminar tareas. Esto separa claramente la lógica de acceso a datos de la lógica de negocio relacionada con las tareas.

Además, abrLa interfaz TareaRepositorio define métodos que se encargan de la interacción con la base de datos, como agregar,
seleccionar, verificar, modificar y eliminar tareas. Esto separa claramente la lógica de acceso a datos de la lógica de 
negocio relacionada con las tareas.

