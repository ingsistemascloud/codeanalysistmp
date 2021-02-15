# CMS

CMS para gestionar Sitios Web de la UFPS.

## Requisitos:

- Tener Maven instalado en el sistema donde será generado el .WAR (versión: 3.6.2 o más reciente).
- Tener MySQL instalado en el servidor (versión: 5.7 o más reciente).
- Tener Tomcat instalado en el servidor (versión: 8.0 o más reciente).

## Pasos para la creación de un Sitio Web:

1. Crear base de datos

    Crear o solicitar la creación de una base de datos en MySQL.

2. Ejecutar .SQL

    Importar el archivo .SQL en la base de datos creada. <br/>
    **Observación:** El usuario que importe él .SQL debe tener permisos de: CREATE TABLE, CREATE TRIGGER, ALTER TABLE, INSERT.

3. Generar .WAR

    - Abrir el símbolo del sistema (CMD) y ubicarse sobre el código fuente del proyecto. Ejemplo:
        ```java
        cd C:\Users\Usuario\Documents\GestorUFPS
        ```
    - Ejecutar el siguiente comando Maven reemplazando los campos dependiendo a sus propiedades específicas:
    
        -Ddriver = El driver de conexión usado. Ejemplo: com.mysql.jdbc.Driver <br/>
        -Ddatabasetype = El sistema manejador de base de datos. Ejemplo: mysql, postgresql <br/>
        -Dip = La dirección IP de la base de datos. Ejemplo: 127.0.0.1, 35.203.35.232 <br/>
        -Dportdabase = Puerto de conexión de la base de datos. Ejemplo: 3306 <br/>
        -Duser = Usuario de la base de datos <br/>
        -Dpassword = Contraseña del usuario de la base de datos. <br/>
        -Ddatabasename = Nombre de la base de datos creada en el primer paso. <br/>
        -Dname = Nombre del sistema (colocar el mismo del dominio). Ejemplo: ingsistemas, ingcivil <br/>
        -Dnamedescription = Nombre completo del Sitio. Ejemplo: "Programa de Ingeniería Civil", "Programa de Ingeniería Mecánica". <br/>
        
        ```java
        mvn clean install  -Ddriver=com.mysql.jdbc.Driver -Ddatabasetype=mysql -Dip=35.203.35.232 -Dportdabase=3306 -Duser=admin -Dpassword=123454 -Ddatabasename=bdingsistemas -Dname=ingsistemas -Dnamedescription="Programa de Ingeniería de Sistemas"
        ```
4. Desplegar en el Servidor

    Dentro de la carpeta del código fuente del proyecto se creó una carpeta llamada: “Target”, allí encontrara él .WAR generado del paso anterior. A continuación, despliegue él .WAR en el Apache Tomcat.
