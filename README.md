# Gestion distribuida de parqueaderos

Laboratorio compuesto por dos microservicios y una base PostgreSQL:

- `vehiculos`: NestJS + TypeORM, puerto `3000`.
- `zonas`: Spring Boot + JPA, puerto `8080`.
- `postgres`: PostgreSQL 16, puerto `5432`.

## Ejecutar con Docker

```bash
docker compose up --build -d
docker compose ps
```

Para detener los servicios:

```bash
docker compose down
```

Para eliminar tambien los datos:

```bash
docker compose down -v
```

## API de vehiculos

Base URL: `http://localhost:3000/api/vehiculos`

```json
{
  "tipo": "Auto",
  "datos": {
    "placa": "ABC-1234",
    "marca": "Toyota",
    "modelo": "Corolla",
    "color": "Azul",
    "anio": 2024,
    "clasificacion": "Gasolina",
    "numeroPuertas": 4,
    "capacidadMaletero": 470
  }
}
```

Operaciones: `POST /`, `GET /`, `GET /{id}`, `PATCH /{id}` y `DELETE /{id}`.

## API de zonas

Base URL: `http://localhost:8080/api`

- `GET|POST /zonas`
- `PUT|DELETE /zonas/{id}`
- `GET|POST /espacios`
- `PUT /espacios`
- `PATCH /espacios/{id}/estado?estado=OCUPADO`
- `DELETE /espacios/{id}`
- `GET /espacios?estado=DISPONIBLE`
- `GET /espacios?zonaId={uuid}&estado=DISPONIBLE`

Ejemplo para crear una zona:

```json
{
  "nombre": "Zona Norte",
  "descripcion": "Ingreso principal",
  "tipo": "REGULAR",
  "capacidad": 20
}
```

Ejemplo para crear un espacio:

```json
{
  "codigo": "ESP-NORTE-01",
  "idZona": "UUID_DE_LA_ZONA",
  "descripcion": "Junto a la entrada",
  "tipo": "AUTO",
  "estado": "DISPONIBLE"
}
```

## API de personas, usuarios y roles

La misma logica esta implementada en los dos frameworks y utiliza las tablas
`persons`, `users`, `roles` y `user_role`.

- NestJS: `http://localhost:3000/api`
- Spring Boot: `http://localhost:8080/api`

Puedes ejecutar los mismos endpoints cambiando solamente el puerto:

- `POST|GET /personas`
- `GET|PATCH|DELETE /personas/{id}`
- `POST|GET /usuarios`
- `GET /usuarios/{id}`
- `POST|GET /roles`
- `POST /usuarios/{userId}/roles/{roleId}`
- `GET /usuarios/{userId}/roles`

Crear una persona:

```json
{
  "dni": "1723456789",
  "firstName": "Eduardo",
  "middleName": "Andres",
  "lastName": "Garcia",
  "email": "eduardo@example.com",
  "phone": "0991234567",
  "address": "Quito",
  "nationality": "Ecuatoriana"
}
```

Crear el usuario de esa persona:

```json
{
  "personId": "UUID_DE_LA_PERSONA",
  "password": "Password123"
}
```

El `username` se genera automaticamente con la estructura:

```text
inicialPrimerNombre + inicialSegundoNombre + apellidoPaterno + secuencia
```

Ejemplo: `Ana Gabriela Cudco` genera `agcudco1`; si ya existe,
el siguiente sera `agcudco2`.

Crear un rol:

```json
{
  "name": "CLIENTE",
  "description": "Cliente del parqueadero"
}
```

Los roles permitidos son `CLIENTE` y `OPERADOR`. La contrasena se almacena
como hash bcrypt y nunca se incluye en las respuestas.
