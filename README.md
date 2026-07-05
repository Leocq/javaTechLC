# TechLab E-commerce API

API RESTful de e-commerce desarrollada con **Spring Boot** y **MySQL** (Proyecto Final).
Incluye un frontend simple (HTML + CSS + JS) que consume la API.

## Guía rápida (paso a paso)

Solo necesitás tener **Java 21+** instalado. **No** hace falta instalar Maven.

```bash
# 1. Clonar el repositorio
git clone https://github.com/Leocq/javaTechLC.git
cd javaTechLC

# 2. Cambiar a la rama DEV (ahí está el proyecto)
git checkout DEV

# 3. Ejecutar con el Maven Wrapper
#    Windows (PowerShell):
.\mvnw.cmd spring-boot:run
#    Linux / Mac:
./mvnw spring-boot:run
```

Cuando veas `Started EcommerceApplication`, abrí **http://localhost:8080** en el navegador.
Para detener la app: `Ctrl + C`.

## Tecnologías

- Java 21 (compatible con Java 24)
- Spring Boot 3.5 (Web, Data JPA, Validation)
- MySQL (base de datos principal) / H2 (base en memoria para pruebas)
- Maven (incluye **Maven Wrapper**, no hace falta instalar Maven)

## Requisitos

- **Java JDK 21 o superior** instalado (verificar con `java -version`).
- **NO** necesitás instalar Maven: el proyecto trae el **Maven Wrapper** (`mvnw`),
  que descarga y usa Maven automáticamente.

> El código está en la rama **`DEV`**. Después de clonar, ejecutá `git checkout DEV`.

## Cómo ejecutar

> **Importante:** usá siempre el wrapper, **no** el comando `mvn`.
> - En **Windows (PowerShell)**: `.\mvnw.cmd ...`
> - En **Linux / Mac**: `./mvnw ...`

### Opción A — H2 (en memoria, sin instalar nada) — recomendada para probar

Windows (PowerShell):
```powershell
.\mvnw.cmd spring-boot:run
```

Linux / Mac:
```bash
./mvnw spring-boot:run
```

Arranca con el perfil `h2` por defecto. Carga datos de ejemplo automáticamente.
- App/Frontend: http://localhost:8080
- Consola H2: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:techlab`, user `sa`, sin password)

### Opción B — MySQL (entrega final)

1. Tener MySQL corriendo y crear la base (o dejar que se cree sola):
   ```sql
   CREATE DATABASE techlab;
   ```
2. Ajustar usuario/password en `src/main/resources/application-mysql.properties`.
3. Ejecutar con el perfil `mysql`:

   Windows (PowerShell):
   ```powershell
   .\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=mysql"
   ```

   Linux / Mac:
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
   ```

## Endpoints principales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/productos` | Listar productos (`?nombre=` para buscar) |
| GET | `/api/productos/{id}` | Detalle de un producto |
| GET | `/api/productos/stock-bajo` | Productos con stock por debajo del mínimo |
| POST | `/api/productos` | Crear producto |
| PUT | `/api/productos/{id}` | Actualizar producto |
| DELETE | `/api/productos/{id}` | Eliminar producto |
| GET/POST/PUT/DELETE | `/api/categorias` | CRUD de categorías |
| POST | `/api/pedidos` | Crear pedido (valida y descuenta stock) |
| GET | `/api/pedidos` · `/api/pedidos/{id}` | Listar / ver pedido |
| PATCH | `/api/pedidos/{id}/estado?valor=CONFIRMADO` | Cambiar estado del pedido |
| GET | `/api/usuarios/{id}/pedidos` | Historial de pedidos de un usuario |

### Ejemplos de body

Crear producto (`POST /api/productos`):
```json
{
  "nombre": "Café Premium",
  "descripcion": "Café colombiano tostado",
  "precio": 12.50,
  "categoriaId": 1,
  "imagenUrl": "http://ejemplo.com/cafe.jpg",
  "stock": 100
}
```

Crear pedido (`POST /api/pedidos`):
```json
{
  "usuarioId": 1,
  "itemsPedido": [
    { "productoId": 1, "cantidad": 2 },
    { "productoId": 3, "cantidad": 1 }
  ]
}
```
Si algún producto no tiene stock suficiente, la API responde **400 Bad Request** con un mensaje claro.

## Estructura del proyecto

```
src/main/java/com/techlab
├── entity        Entidades JPA (Producto, Categoria, Usuario, Pedido, LineaPedido)
├── repository    Repositorios Spring Data JPA
├── service       Lógica de negocio
├── controller    Controladores REST
├── dto           Objetos de request/response + validaciones
├── exception     Excepciones y manejador global (HTTP 400/404)
└── config        CORS y carga de datos de ejemplo

src/main/resources/static   Frontend (index.html, app.js, styles.css)
```

## Modelo de datos

- **Producto**: id, nombre, descripción, precio, stock, imagenUrl, **categoría** (reemplaza la herencia Bebida/Comida por una relación a Categoría).
- **Categoría**: id, nombre.
- **Usuario**: id, nombre, email.
- **Pedido**: id, usuario, fecha, estado (PENDIENTE, CONFIRMADO, ENVIADO, ENTREGADO, CANCELADO), total, líneas.
- **LineaPedido**: producto, cantidad, precio unitario.
