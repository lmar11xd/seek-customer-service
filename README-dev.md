"# seek-customer-service" 

# Configuración Docker
### Vamos a necesitar 2 contenedores (APIRest, MysQL): Abrir PowerShell cualquier ruta
```
docker network create seek_network (Crear Red para conectar los contenedores)
docker network ls (Listar redes)
docker pull mysql (Descargar imagen de MySQL)
```

Levantar Contenedor MySQL con la red creada
```
docker run -d --name mysqldb --network seek_network -p 3305:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql:latest
docker ps (Listar contenedores)
```

Iniciar MySQL
```
docker exec -it mysqldb mysql -u root -p
show databases; (Listar base de datos)
create database seekdb; (Crear base de datos)
```

### Configurar APIRest
Crear Dockerfile en raiz del proyecto y configurarlo
Modificar application.properties
nombre_contenedor_mysql: seekdb
username_contenedor_mysql: root
pass_contenedor_mysql: 123456
```
spring.datasource.url=jdbc:mysql://mysqldb:3306/{{nombre_contenedor_mysql}}?useSSL=false&serverTimezone=UTC
spring.datasource.username={{username_contenedor_mysql}}
spring.datasource.password={{pass_contenedor_mysql}}
```

Crear Jar: En una Terminal de la raiz del proyecto
```
mvn clean install -DskipTests (Evitamos los test para que no verifiquen conexión a bd)
```

Creamos imagen de APIRest: En una Terminal de la raiz del proyecto
```
docker build -t img_seek_customer_service . (Creamos imagen de la api en docker)
```

Creamos contenedor: En Powershell cualquier ruta
```
docker run -d --name container_seek_customer_service --network seek_network -p 8085:8080 img_seek_customer_service:latest
```
