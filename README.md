# Franchises CRUD API

API backend en Spring Boot para gestionar franquicias, sucursales y productos.

**Base URL (API):** `http://localhost:8080`

## 📬 Endpoints

### Franchises

`POST http://localhost:8080/api/franchises` → Crear franquicia

```json
{
  "name": "Franquicia Central",
  "document": "NIT-900123456"
}
```

`POST http://localhost:8080/api/franchises/{franchiseId}/branches` → Agregar sucursal a una franquicia (reemplaza `{franchiseId}` por el id, ej. `1`)

```json
{
  "name": "Sucursal Norte"
}
```

`PATCH http://localhost:8080/api/franchises/{franchiseId}/name` → Actualizar nombre de la franquicia

```json
{
  "name": "Nuevo nombre comercial"
}
```

`GET http://localhost:8080/api/franchises/{franchiseId}/top-stock-products-by-branch` → Producto con mayor stock por sucursal (sin cuerpo)

---

### Branches

`POST http://localhost:8080/api/branches/{branchId}/products` → Agregar producto a una sucursal

```json
{
  "name": "Producto ejemplo",
  "stock": 100
}
```

`DELETE http://localhost:8080/api/branches/{branchId}/products/{productId}` → Eliminar producto de la sucursal (sin cuerpo)

`PATCH http://localhost:8080/api/branches/{branchId}/name` → Actualizar nombre de la sucursal

```json
{
  "name": "Sucursal Centro"
}
```

---

### Products

`PATCH http://localhost:8080/api/products/{productId}/stock` → Actualizar stock del producto

```json
{
  "stock": 50
}
```

`PATCH http://localhost:8080/api/products/{productId}/name` → Actualizar nombre del producto

```json
{
  "name": "Producto renombrado"
}
```

> Documentacion OpenAPI: [`docs/openapi.yaml`](docs/openapi.yaml) (importar en [Swagger Editor](https://editor.swagger.io/)).

## Docker (Plus de la prueba)

### Requisitos

- Docker Desktop **encendido** (icono de ballena en la bandeja de Windows)
- Puertos libres: **8080** (API) y **5433** (Postgres en el host; ver abajo)

### Stack recomendado: API + Postgres en Docker

El `docker-compose.yml` levanta **dos servicios**: la API y PostgreSQL. La API se conecta a la BD por red interna (`db:5432`). **No necesitas Supabase** para que Docker funcione.

La API arranca con el perfil Spring `docker` (`application-docker.properties`), asi no depende del default de `application.properties` (Supabase) si algo falla al inyectar variables.

**Importante:** usa siempre compose desde la carpeta del proyecto. Si ves un contenedor con nombre raro tipo `optimistic_...` en los logs, suele ser un `docker run` suelto **sin** variables: para y ejecuta solo `docker compose up --build`.

```bash
docker compose down -v
docker compose up --build
```

- API: `http://localhost:8080`
- Postgres expuesto en tu PC: `localhost:5433` (usuario `postgres`, contraseña `postgres`, base `franchises`)  
  Se usa **5433** en el host para no chocar con un PostgreSQL instalado en Windows que suele usar **5432**.

#### Si falla el arranque

| Síntoma | Qué hacer |
|--------|-----------|
| `port is already allocated` en 8080 | Cierra otra app que use 8080 o cambia en `docker-compose.yml` a `"8081:8080"` |
| `port is already allocated` en 5433 | Cambia a `"5434:5432"` en la sección `db` |
| Postgres no pasa a Healthy | Espera 30–60 s; si sigue mal: `docker compose logs db` |
| `Cannot connect to the Docker daemon` | Abre Docker Desktop y espera a que diga “Running” |

### Opción: solo imagen de la API (sin compose)

**No uses el botón "Run" de Docker Desktop sin variables:** la app no tendrá `SPRING_DATASOURCE_URL` correcta ni red hacia `db` (ese nombre solo existe dentro de `docker compose`).

Requiere **pasar** `SPRING_DATASOURCE_*` apuntando a una BD que el contenedor pueda alcanzar:

- **Postgres del mismo proyecto en el host** (tras `docker compose up -d db` o con el puerto **5433** mapeado): usa `host.docker.internal` y el puerto del host (**5433**), no `db:5432`.
- O **Supabase** / cualquier Postgres accesible desde la red del contenedor.

### Requisitos (solo si usas Supabase desde el host)

- Docker Desktop
- Acceso a PostgreSQL (Supabase)

### 1) Configurar variables de entorno

1. Copia `.env.example` a `.env`.
2. Completa tus credenciales de Supabase.

Ejemplo:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://your-supabase-host:5432/postgres?sslmode=require
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password
```

### 2) Levantar con Docker Compose

```bash
docker compose up --build
```

La API queda disponible en:

`http://localhost:8080`

### 3) Levantar con Docker (sin compose)

Build (ejemplo de tag):

```bash
docker build -t franquicias .
```

**Run** con Postgres en tu máquina (Compose con `db` en marcha, puerto **5433** en el host):

Las variables de entorno **pisan** la URL del perfil `docker`; asi puedes usar `SPRING_PROFILES_ACTIVE=docker` y seguir apuntando al Postgres del host.

```bash
docker run --rm -p 8080:8080 `
  -e SPRING_PROFILES_ACTIVE=docker `
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5433/franchises `
  -e SPRING_DATASOURCE_USERNAME=postgres `
  -e SPRING_DATASOURCE_PASSWORD=postgres `
  franquicias
```

Supabase (sin perfil `docker` o con URL propia):

```bash
docker run --rm -p 8080:8080 `
  -e SPRING_DATASOURCE_URL="jdbc:postgresql://your-supabase-host:5432/postgres?sslmode=require" `
  -e SPRING_DATASOURCE_USERNAME=postgres `
  -e SPRING_DATASOURCE_PASSWORD=your_password `
  franquicias
```

> **PowerShell:** comillas y backtick `` ` `` para multilínea. **CMD:** usa `^` en lugar de backtick.

#### Error `Unable to determine Dialect without JDBC metadata`

Suele aparecer cuando **no hay conexión JDBC** (URL incorrecta, `db` sin red Compose, o Postgres apagado). Corrige la URL y el puerto; no basta con solo reconstruir la imagen.

## Notas

- La configuracion de base de datos se resuelve por variables de entorno.
- No se exponen credenciales reales en el repositorio.

## Plus: Programacion Funcional

La capa de servicios aplica un estilo funcional con:

- DTOs inmutables usando `record`.
- Transformaciones con `stream().map(...).toList()`.
- `Optional` donde el API lo amerita (p. ej. `repository.findById(...).orElseThrow(...)`).
- Composicion `persistir + mapeo` con `Function<T, R>` (`apply` sobre entidades ya cargadas), sin `Optional.of(x)` artificial cuando `x` no es opcional.
- Funciones reutilizables con `Function<T, R>` para mapear entidad a respuesta.
