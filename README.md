# Test Dev Backend

API REST para la gestion de clientes, bodegas, puertos y envios (terrestres y maritimos), con autenticacion JWT.

## Frontend del proyecto

- Aplicacion desplegada: https://test-dev-front-nine.vercel.app
- Repositorio frontend: agrega aqui el enlace de tu repo frontend

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Security (JWT)
- Spring Data JPA
- MySQL
- Maven Wrapper
- Swagger / OpenAPI

## Estructura principal

- `src/main/java/com/testdev/test_dev/controller`: endpoints REST
- `src/main/java/com/testdev/test_dev/service`: logica de negocio
- `src/main/java/com/testdev/test_dev/repository`: acceso a datos
- `src/main/java/com/testdev/test_dev/model`: entidades del dominio
- `src/main/java/com/testdev/test_dev/config`: seguridad y configuracion JWT
- `src/main/resources/application.properties`: configuracion de la app

## Variables de entorno requeridas

Define estas variables antes de iniciar:

- PORT
- DB_HOST
- DB_PORT
- DB_NAME
- DB_USERNAME
- DB_PASSWORD
- SECURITY_USER
- SECURITY_PASSWORD
- JWT_SECRET
- JWT_EXPIRATION

## Ejecucion local

```powershell
$env:PORT='8081'
$env:DB_HOST='localhost'
$env:DB_PORT='3306'
$env:DB_NAME='test_dev'
$env:DB_USERNAME='root'
$env:DB_PASSWORD='tu_password'
$env:SECURITY_USER='admin'
$env:SECURITY_PASSWORD='admin123'
$env:JWT_SECRET='tu_clave_jwt'
$env:JWT_EXPIRATION='3600000'
./mvnw.cmd spring-boot:run
```

## Endpoints utiles

- Health: http://localhost:8081/actuator/health
- Swagger UI: http://localhost:8081/swagger-ui.html
- OpenAPI docs: http://localhost:8081/api-docs

## Autenticacion

### Login

- Metodo: `POST`
- Endpoint: `/auth/login`
- Body:

```json
{
  "username": "admin",
  "password": "password123"
}
```

Luego usa el token en el header:

```http
Authorization: Bearer <token>
```

## CORS

Orgenes permitidos actualmente:

- http://localhost:4200
- http://localhost:8081
- https://test-dev-front-nine.vercel.app

## Nota

La documentacion tecnica extendida esta en `HELP.md`.
