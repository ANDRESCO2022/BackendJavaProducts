# Backend Java Products - API REST

Este es un backend desarrollado con Spring Boot que proporciona una API REST para la gestión de usuarios y productos con autenticación JWT.

## ¿Cómo utilizar estos agentes?

Los "agentes" se refieren a los diferentes endpoints de la API que manejan las operaciones del sistema. A continuación se explica cómo usar cada uno:

## Configuración Inicial

### Requisitos Previos
- Java 17 o superior
- MySQL 8.0 o superior
- Maven 3.6 o superior

### Base de Datos
Crear una base de datos MySQL llamada `bd_api_crud` y las tablas necesarias:

```sql
-- Crear base de datos
CREATE DATABASE bd_api_crud;
USE bd_api_crud;

-- Crear tabla de roles
CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- Crear tabla de usuarios
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE
);

-- Crear tabla de productos
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sku VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price INTEGER NOT NULL
);

-- Crear tabla de relación usuarios-roles
CREATE TABLE users_roles (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Insertar roles básicos
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

-- Insertar usuario administrador inicial (password: admin123)
INSERT INTO users (username, password, enabled) VALUES 
('admin', '$2a$10$GR3OqYKKHJzK0Z9oGRKHXOhJdFLUl.YFwLKvOYSJB4z4JzJYz.1qO', true);

-- Asignar rol de administrador al usuario admin
INSERT INTO users_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';

-- Insertar usuario básico de ejemplo (password: user123)
INSERT INTO users (username, password, enabled) VALUES 
('usuario1', '$2a$10$ZTOhKOeGQZPJ3w3fwHJFy.Z8K0zJFKzK0Z9oGRKHXOhJdFLUl.YFw', true);

-- Asignar rol de usuario al usuario básico
INSERT INTO users_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'usuario1' AND r.name = 'ROLE_USER';
```

**Usuarios de ejemplo creados:**
- **admin** / admin123 (ROLE_ADMIN)
- **usuario1** / user123 (ROLE_USER)

### Configuración
El archivo `application.properties` contiene la configuración de la base de datos:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bd_api_crud
spring.datasource.username=root
spring.datasource.password=sasa1234
```

### Ejecutar la Aplicación
```bash
./mvnw spring-boot:run
```

## Guía de Inicio Rápido

### 1. Configurar el entorno
```bash
# Clonar el repositorio
git clone <repository-url>
cd BackendJavaProducts

# Asegurar permisos para Maven wrapper
chmod +x mvnw
```

### 2. Configurar MySQL
```bash
# Iniciar MySQL
sudo systemctl start mysql

# Conectar a MySQL como root
mysql -u root -p

# Ejecutar el script SQL proporcionado arriba
```

### 3. Compilar y ejecutar
```bash
# Compilar el proyecto
./mvnw clean compile

# Ejecutar tests (opcional)
./mvnw test

# Ejecutar la aplicación
./mvnw spring-boot:run
```

### 4. Probar la API
```bash
# Verificar que la aplicación esté corriendo
curl http://localhost:8080/api/users

# Hacer login con usuario admin
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'
```

## Solución de Problemas

### Error de conexión a MySQL
```
Error: Communications link failure
```
**Solución:**
- Verificar que MySQL esté corriendo: `sudo systemctl status mysql`
- Verificar credenciales en `application.properties`
- Asegurar que la base de datos `bd_api_crud` existe

### Error de autenticación JWT
```json
{
    "error": "JWT signature does not match",
    "message": "Token invalido"
}
```
**Solución:**
- Obtener un nuevo token haciendo login
- Verificar que el token no haya expirado (duración: 1 hora)
- Asegurar que el header Authorization tenga el formato: `Bearer {token}`

### Error de permisos
```json
{
    "status": 403,
    "error": "Forbidden"
}
```
**Solución:**
- Verificar que el usuario tenga el rol necesario
- Para operaciones de ADMIN, usar un token de usuario con rol ROLE_ADMIN
- Para operaciones de USER, usar un token de usuario con rol ROLE_USER o ROLE_ADMIN

### Error de validación
```json
{
    "price": "El campo price debe ser mayor a 500"
}
```
**Solución:**
- Revisar las validaciones en la sección "Validaciones"
- Asegurar que todos los campos requeridos estén presentes
- Verificar que los valores cumplan con las restricciones

## Agentes de la API (Endpoints)

### 1. Agente de Autenticación

#### Login (Obtener Token JWT)
```http
POST http://localhost:8080/login
Content-Type: application/json

{
    "username": "admin",
    "password": "password123"
}
```

**Respuesta exitosa:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "admin",
    "message": "hola admin has iniciado sesion exitosamente"
}
```

### 2. Agente de Gestión de Usuarios

#### Listar Usuarios (Público)
```http
GET http://localhost:8080/api/users
```

#### Registrar Usuario (Público)
```http
POST http://localhost:8080/api/users/register
Content-Type: application/json

{
    "username": "nuevousuario",
    "password": "password123"
}
```

#### Crear Usuario (Solo ADMIN)
```http
POST http://localhost:8080/api/users
Content-Type: application/json
Authorization: Bearer {token}

{
    "username": "admin2",
    "password": "password123",
    "admin": true
}
```

### 3. Agente de Gestión de Productos

#### Listar Productos (ADMIN o USER)
```http
GET http://localhost:8080/api/products
Authorization: Bearer {token}
```

#### Obtener Producto por ID (ADMIN o USER)
```http
GET http://localhost:8080/api/products/1
Authorization: Bearer {token}
```

#### Crear Producto (Solo ADMIN)
```http
POST http://localhost:8080/api/products
Content-Type: application/json
Authorization: Bearer {token}

{
    "sku": "PROD-001",
    "name": "Producto Ejemplo",
    "description": "Descripción del producto",
    "price": 1000
}
```

#### Actualizar Producto (Solo ADMIN)
```http
PUT http://localhost:8080/api/products/1
Content-Type: application/json
Authorization: Bearer {token}

{
    "sku": "PROD-001-UPD",
    "name": "Producto Actualizado",
    "description": "Descripción actualizada",
    "price": 1500
}
```

#### Eliminar Producto (Solo ADMIN)
```http
DELETE http://localhost:8080/api/products/1
Authorization: Bearer {token}
```

## Roles y Permisos

### Roles Disponibles
- **USER**: Usuario básico con permisos de lectura
- **ADMIN**: Administrador con permisos completos

### Matriz de Permisos

| Endpoint | Método | Público | USER | ADMIN |
|----------|--------|---------|------|-------|
| `/login` | POST | ✅ | ✅ | ✅ |
| `/api/users` | GET | ✅ | ✅ | ✅ |
| `/api/users/register` | POST | ✅ | ✅ | ✅ |
| `/api/users` | POST | ❌ | ❌ | ✅ |
| `/api/products` | GET | ❌ | ✅ | ✅ |
| `/api/products/{id}` | GET | ❌ | ✅ | ✅ |
| `/api/products` | POST | ❌ | ❌ | ✅ |
| `/api/products/{id}` | PUT | ❌ | ❌ | ✅ |
| `/api/products/{id}` | DELETE | ❌ | ❌ | ✅ |

## Ejemplos de Uso con cURL

### 1. Login como administrador
```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

**Respuesta:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjoiW3tcImF1dGhvcml0eVwiOlwiUk9MRV9BRE1JTlwifV0iLCJ1c2VybmFtZSI6ImFkbWluIiwiaWF0IjoxNjQwOTk1MjAwLCJleHAiOjE2NDA5OTg4MDB9.xyz123",
    "username": "admin",
    "message": "hola admin has iniciado sesion exitosamente"
}
```

### 2. Login como usuario normal
```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario1",
    "password": "user123"
  }'
```

### 3. Registrar un nuevo usuario
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "nuevouser",
    "password": "password123"
  }'
```

### 4. Crear un producto (como ADMIN)
```bash
# Usar el token obtenido del login de admin
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.xyz123..." \
  -d '{
    "sku": "LAPTOP-001",
    "name": "Laptop Gaming",
    "description": "Laptop para gaming de alta gama",
    "price": 25000
  }'
```

### 5. Listar productos (como USER o ADMIN)
```bash
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer {tu_token_aqui}"
```

### 6. Obtener un producto específico
```bash
curl -X GET http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer {tu_token_aqui}"
```

### 7. Actualizar un producto (solo ADMIN)
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {admin_token}" \
  -d '{
    "sku": "LAPTOP-001-V2",
    "name": "Laptop Gaming Actualizada",
    "description": "Laptop para gaming de alta gama - Versión mejorada",
    "price": 28000
  }'
```

### 8. Eliminar un producto (solo ADMIN)
```bash
curl -X DELETE http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer {admin_token}"
```

## Validaciones

### Usuario
- `username`: Requerido, único, 3-12 caracteres
- `password`: Requerido

### Producto
- `sku`: Requerido, único
- `name`: Requerido, 3-20 caracteres
- `description`: Requerido
- `price`: Requerido, mínimo 500

## Manejo de Errores

### Errores de Validación (400)
```json
{
    "username": "El campo username no puede estar vacío",
    "price": "El campo price debe ser mayor a 500"
}
```

### Error de Autenticación (401)
```json
{
    "message": "Error de autenticacion: credenciales incorrectas",
    "error": "Bad credentials"
}
```

### Token Inválido (401)
```json
{
    "error": "JWT signature does not match locally computed signature",
    "message": "Token invalido"
}
```

### Recurso No Encontrado (404)
```json
{
    "status": 404,
    "message": "Producto no encontrado"
}
```

## Configuración de CORS

La API está configurada para permitir solicitudes desde:
- `http://localhost:4200` (Angular frontend)
- Cualquier origen (`*`) para desarrollo

## Tecnologías Utilizadas

- **Spring Boot 3.2.2**: Framework principal
- **Spring Security**: Autenticación y autorización
- **JWT (JSON Web Tokens)**: Autenticación stateless
- **Spring Data JPA**: Persistencia de datos
- **MySQL**: Base de datos
- **Bean Validation**: Validación de datos
- **Maven**: Gestión de dependencias

## Estructura del Proyecto

```
src/main/java/
├── controllers/          # Agentes REST (UserController, ProductController)
├── entities/            # Modelos de datos (User, Product, Role)
├── Services/            # Lógica de negocio
├── repositories/        # Acceso a datos
├── security/           # Configuración de seguridad y JWT
└── validation/         # Validaciones personalizadas
```

## Notas Adicionales

- Los tokens JWT tienen una duración de 1 hora
- Las contraseñas se encriptan usando BCrypt
- La aplicación usa CORS para permitir acceso desde frontends
- Los usuarios registrados automáticamente obtienen el rol USER
- Solo los ADMIN pueden crear otros usuarios con rol ADMIN
