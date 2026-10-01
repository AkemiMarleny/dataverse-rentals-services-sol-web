# Guía de ejecución — Dataverse Rentals Services

Guía paso a paso para levantar el proyecto en una máquina local (entorno de desarrollo).

## Requisitos previos

| Herramienta | Versión | Cómo verificarlo |
|---|---|---|
| JDK | 24 | `java -version` |
| Docker | reciente | `docker --version` |
| Docker Compose | v2 | `docker compose version` |

> Maven no es necesario: el proyecto incluye el wrapper `./mvnw`.

## Paso 1 — Levantar las dependencias locales

El proyecto necesita **PostgreSQL** (base de datos) y **LocalStack** (emulador de S3
para almacenamiento de archivos). Ambos están definidos en `compose.yml`:

```bash
# PostgreSQL (puerto 5432) + LocalStack (puerto 4566)
docker compose up -d
```

> Alternativa con los atajos del `makefile`: `make docker.compose`.

Verifica que ambos servicios estén arriba:

```bash
docker compose ps
```

Debes ver `db` y `localstack` en estado `Up`. El resultado esperado:

| Servicio | Imagen | Puerto | Credenciales |
|---|---|---|---|
| `db` | `postgres:18.3-alpine` | `5432` | usuario `admin`, contraseña `admin`, base `dataverse_db` |
| `localstack` | `localstack/localstack:4.2.0` | `4566` | servicio S3, región `us-east-1` |

## Paso 2 — Configurar las variables de entorno

La aplicación lee todas sus configuraciones desde variables de entorno (ver
`src/main/resources/application.properties`). Sin ellas **no arranca**.

### Variables obligatorias (sin valor por defecto)

| Variable | Descripción |
|---|---|
| `DATASOURCE_URL` | URL JDBC de PostgreSQL, p. ej. `jdbc:postgresql://localhost:5432/dataverse_db` |
| `DB_USERNAME` | Usuario de la base de datos (local: `admin`) |
| `DB_PASSWORD` | Contraseña de la base de datos (local: `admin`) |
| `JWT_SECRET` | Secreto para firmar los tokens JWT (mín. 32 bytes recomendado) |
| `MFA_ENCRYPTOR_PASSWORD` | Contraseña del cifrado de secretos MFA |
| `MFA_ENCRYPTOR_SALT` | Salt del cifrado de secretos MFA (16 caracteres) |

### Variables opcionales (con valor por defecto)

| Variable | Por defecto | Descripción |
|---|---|---|
| `JWT_SHORT_EXPIRATION` | `180000` | Expiración del access token (ms) |
| `JWT_REFRESH_EXPIRATION` | `86400000` | Expiración del refresh token (ms) |
| `S3_REGION` | `us-east-1` | Región de S3 |
| `S3_BUCKET` | `dataverse-vouchers` | Bucket S3 (se crea automáticamente al arrancar) |
| `S3_ENDPOINT` | *(vacío)* | Endpoint S3; si está vacío se usa AWS real |
| `S3_ACCESS_KEY_ID` | *(vacío)* | Access key de S3 |
| `S3_SECRET_ACCESS_KEY` | *(vacío)* | Secret key de S3 |
| `S3_PATH_STYLE_ACCESS` | `false` | Usar URLs path-style (requerido con LocalStack) |

> **Perfil `dev`:** el archivo `application-dev.properties` ya configura S3 contra
> LocalStack (`http://localhost:4566`, credenciales `test`/`test`, path-style `true`)
> y habilita Swagger. Para desarrollo local alcanza con las variables obligatorias
> y activar este perfil (Paso 3).

### Cómo definirlas

**Opción A — por terminal (una sesión):**

```bash
export SPRING_PROFILES_ACTIVE=dev
export DATASOURCE_URL=jdbc:postgresql://localhost:5432/dataverse_db
export DB_USERNAME=admin
export DB_PASSWORD=admin
export JWT_SECRET="cambia-este-secreto-por-uno-seguro-de-al-menos-32-bytes"
export MFA_ENCRYPTOR_PASSWORD="clave-mfa-local"
export MFA_ENCRYPTOR_SALT="salt-local-16chars"
```

**Opción B — archivo `.env` en la raíz del proyecto:**

```bash
# .env — NO versionarlo (ya está en .gitignore)
SPRING_PROFILES_ACTIVE=dev
DATASOURCE_URL=jdbc:postgresql://localhost:5432/dataverse_db
DB_USERNAME=admin
DB_PASSWORD=admin
JWT_SECRET=cambia-este-secreto-por-uno-seguro-de-al-menos-32-bytes
MFA_ENCRYPTOR_PASSWORD=clave-mfa-local
MFA_ENCRYPTOR_SALT=salt-local-16chars
```

Cárgalo en la sesión antes de ejecutar la app:

```bash
set -a
source .env
set +a
```

## Paso 3 — Ejecutar la aplicación

Con las dependencias levantadas y las variables cargadas:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

> Alternativa con el `makefile`: `make build` compila el proyecto
> (`./mvnw clean install -DskipTests`).

### Qué ocurre al arrancar (en orden)

1. **Flyway** aplica las migraciones de `src/main/resources/db/migration`
   (`V1.0__Initial.sql`, `V1.1__Enable_Unaccent.sql`, `V1.2__Data.sql`).
2. La app valida el esquema contra las entidades JPA (`ddl-auto=validate`).
3. **S3**: se crea el bucket (`dataverse-vouchers` por defecto) en LocalStack si no existe.
4. **Seed de datos**: se crean los usuarios por defecto (si no existen) — ver Paso 4.

La aplicación queda lista cuando aparece en el log:

```
Started DataverserentalsApplication
```

## Paso 4 — Verificar

**Swagger UI** (solo disponible con el perfil `dev`):

- <http://localhost:8080/swagger-ui.html>

**Login de prueba** — la app expone los endpoints públicos
`POST /api/v1/auth/login`, `POST /api/v1/auth/logout` y `POST /api/v1/auth/refresh`:

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"nombreUsuario": "dataverseadmin", "contrasena": "dataverseadmin"}'
```

**Usuarios creados por el seed** (`DataInitializer`):

| Usuario | Contraseña | Rol |
|---|---|---|
| `dataverseadmin` | `dataverseadmin` | `ADMINISTRADOR` |
| `dataversesys` | `dataversesys` | `SYSTEM` |

> Son credenciales **solo de desarrollo** y solo se crean la primera vez.

## Paso 5 — Detener los servicios

```bash
docker compose stop   # detiene los contenedores y conserva los datos en .docker/
docker compose start  # los vuelve a encender
```

Para eliminar contenedores **y datos** (empezar de cero):

```bash
docker compose down
```

## Problemas comunes

| Síntoma | Causa probable | Solución |
|---|---|---|
| `Connection refused` al conectar a PostgreSQL | Los contenedores no están arriba | `docker compose up -d` y `docker compose ps` |
| Puerto `5432` o `4566` ya ocupado | Otro proceso usando el puerto | Detenerlo, o cambiar el mapeo de puertos en `compose.yml` |
| `Unable to obtain connection` / credenciales inválidas | `DATASOURCE_URL`, `DB_USERNAME` o `DB_PASSWORD` incorrectas | Revisar el Paso 2 (local: `admin`/`admin`) |
| Error de Flyway `Validate failed` | Migraciones sin aplicar o esquema modificado a mano | `docker compose down -v` para resetear la base y volver a `up -d` |
| Bucket S3 no disponible al arrancar | LocalStack caído o `S3_ENDPOINT` vacío | Levantar LocalStack; en `dev` el endpoint debe ser `http://localhost:4566` |
| Swagger no carga | No se activó el perfil `dev` | Ejecutar con `-Dspring-boot.run.profiles=dev` |
| `Invalid secret` / error al firmar JWT | `JWT_SECRET` demasiado corto o con caracteres inválidos | Usar una cadena larga y estable (mín. 32 bytes) |

## Referencias

- `compose.yml` — servicios PostgreSQL y LocalStack
- `src/main/resources/application.properties` — variables de entorno leídas
- `src/main/resources/application-dev.properties` — configuración del perfil `dev`
- `makefile` — atajos: `make docker.compose`, `make docker.start`, `make docker.stop`, `make build`