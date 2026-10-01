# Dataverse Rentals Services

Servicio backend de Dataverse Rentals. Proyecto Spring Boot (Java 24) con
PostgreSQL (Flyway), S3 para almacenamiento de archivos y autenticación JWT + MFA.

## Requisitos

- **JDK 24**
- **Docker** + **Docker Compose** (para PostgreSQL y LocalStack de desarrollo)
- Maven no es necesario: se usa el wrapper `./mvnw`

## Cómo ejecutar el proyecto

Guía paso a paso con los prerrequisitos, las variables de entorno requeridas,
las dependencias locales (PostgreSQL / LocalStack) y la puesta en marcha:

[Guía de ejecución](docs/guia-ejecucion.md)

Resumen rápido:

```bash
# 1. Levantar dependencias locales (PostgreSQL + LocalStack)
docker compose up -d

# 2. Configurar variables de entorno (ver la guía completa)
export SPRING_PROFILES_ACTIVE=dev
export DATASOURCE_URL=jdbc:postgresql://localhost:5432/dataverse_db
export DB_USERNAME=admin
export DB_PASSWORD=admin
export JWT_SECRET="cambia-este-secreto-por-uno-seguro-de-al-menos-32-bytes"
export MFA_ENCRYPTOR_PASSWORD="clave-mfa-local"
export MFA_ENCRYPTOR_SALT="salt-local-16chars"

# 3. Ejecutar la aplicación
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Con el perfil `dev` activo, la API queda disponible en
`http://localhost:8080` y Swagger UI en `http://localhost:8080/swagger-ui.html`.