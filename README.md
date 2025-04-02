# Guía: seek-customer-service
Microservicio para gestión de clientes.

## Despliegue en AWS: AWS BEANSTALK
El microservicio a sido desplegado en AWS BEANSTALK y con una base de datos RDS (MySQL)

URL: http://seek-customer-ms.us-east-1.elasticbeanstalk.com

_**Endpoints:**_

```
api/auth/token (Generar token)
api/customers/create (Crear Cliente)
api/customers (Listar Clientes)
api/customers/statistics (Obtener Estadísticas de los Clientes)
```

## Pruebas
Para utilizar los endpoints necesitamos generar un token

### Generar Token
Generar token para consumir los endpoints. Token tiene vigencia de 1 hora

_**Curl**_
```
curl --location --request POST 'http://seek-customer-ms.us-east-1.elasticbeanstalk.com/api/auth/token?username=test'
```

### Crear Cliente
Registrar cliente con los siguientes datos: nombre, apellido, edad y fecha de nacimiento.   
Valida si la edad es menor que 0 (retorna 422).

_**Curl**_
```
curl --location 'http://seek-customer-ms.us-east-1.elasticbeanstalk.com/api/customers/create' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {reemplazar_por_token_generado}' \
--data '{
    "names": "Dina",
    "lastName": "Boluarte",
    "age": 62,
    "birthday": "1962-05-31"
}'
```

### Listar Clientes
Listar todos los clientes registrados con sus datos completos, 
además se agregaron cálculos derivados como esperanza de vida y signo del sodiaco.

_**Curl**_
```
curl --location 'http://seek-customer-ms.us-east-1.elasticbeanstalk.com/api/customers' \
--header 'Authorization: Bearer {reemplazar_por_token_generado}'
```

### Obtener Estadísticas/Métricas
Obtener el promedio y la desciación estándar de las edades de los clientes registrados
_**Curl**_
```
curl --location 'http://seek-customer-ms.us-east-1.elasticbeanstalk.com/api/customers/statistics' \
--header 'Authorization: Bearer {reemplazar_por_token_generado}'
```

### Documentación con Swagger
http://seek-customer-ms.us-east-1.elasticbeanstalk.com/swagger-ui/index.html
