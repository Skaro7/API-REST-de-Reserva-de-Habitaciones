# Hotel Room Reservation API

A REST API for managing users, rooms and reservations built with Java, Spring Boot, Spring Data JPA and H2 in-memory database.

---

## Requirements

- Java 21
- Maven

---

## How to run

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

---

## H2 Console (Database viewer)

You can inspect the database directly in your browser:

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: *(leave empty)*

---

## How to test the API

You can use [Postman](https://www.postman.com/) or any HTTP client.

All request bodies must be sent as **JSON**. In Postman, go to **Body → raw → JSON**.

---

## Users

### Create a user

```
POST http://localhost:8080/users
```

**Request body:**
```json
{
    "name": "John Doe",
    "email": "john@example.com"
}
```

**Success response — 201 Created:**
```json
{
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com"
}
```

**Error — 400 Bad Request** (missing or invalid fields):
```json
{
    "status": 400,
    "error": "Bad Request",
    "message": "name: El nombre es obligatorio",
    "timestamp": "2026-05-22T18:00:00"
}
```

**Error — 409 Conflict** (email already registered):
```json
{
    "status": 409,
    "error": "Conflict",
    "message": "El email ya está registrado",
    "timestamp": "2026-05-22T18:00:00"
}
```

---

### Get all users

```
GET http://localhost:8080/users
```

**Success response — 200 OK:**
```json
[
    {
        "id": 1,
        "name": "John Doe",
        "email": "john@example.com"
    }
]
```

---

### Get user by ID

```
GET http://localhost:8080/users/{id}
```

**Example:**
```
GET http://localhost:8080/users/1
```

**Success response — 200 OK:**
```json
{
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com"
}
```

**Error — 404 Not Found:**
```json
{
    "status": 404,
    "error": "Not Found",
    "message": "Usuario no encontrado",
    "timestamp": "2026-05-22T18:00:00"
}
```

---

## Rooms

### Create a room

```
POST http://localhost:8080/rooms
```

**Request body:**
```json
{
    "roomNumber": 101,
    "capacity": 2,
    "pricePerNight": 85.00
}
```

**Success response — 201 Created:**
```json
{
    "id": 1,
    "roomNumber": 101,
    "capacity": 2,
    "pricePerNight": 85.00
}
```

**Error — 400 Bad Request** (invalid capacity or price):
```json
{
    "status": 400,
    "error": "Bad Request",
    "message": "capacity: must be greater than 0",
    "timestamp": "2026-05-22T18:00:00"
}
```

**Error — 409 Conflict** (room number already exists):
```json
{
    "status": 409,
    "error": "Conflict",
    "message": "Numero de habitacion ya existe",
    "timestamp": "2026-05-22T18:00:00"
}
```

---

### Get all rooms

```
GET http://localhost:8080/rooms
```

**Success response — 200 OK:**
```json
[
    {
        "id": 1,
        "roomNumber": 101,
        "capacity": 2,
        "pricePerNight": 85.00
    }
]
```

---

### Get room by ID

```
GET http://localhost:8080/rooms/{id}
```

**Example:**
```
GET http://localhost:8080/rooms/1
```

**Success response — 200 OK:**
```json
{
    "id": 1,
    "roomNumber": 101,
    "capacity": 2,
    "pricePerNight": 85.00
}
```

**Error — 404 Not Found:**
```json
{
    "status": 404,
    "error": "Not Found",
    "message": "Habitacion no encontrada",
    "timestamp": "2026-05-22T18:00:00"
}
```

---

## Reservations

> **Important:** You need to create a user and a room before creating a reservation.

### Create a reservation

```
POST http://localhost:8080/reservations
```

**Request body:**
```json
{
    "userId": 1,
    "roomNumber": 101,
    "startDate": "2026-06-01",
    "endDate": "2026-06-05"
}
```

Dates must be in `YYYY-MM-DD` format. The start date must be before the end date.

**Success response — 201 Created:**
```json
{
    "id": 1,
    "user": {
        "id": 1,
        "name": "John Doe",
        "email": "john@example.com"
    },
    "room": {
        "id": 1,
        "roomNumber": 101,
        "capacity": 2,
        "pricePerNight": 85.00
    },
    "startDate": "2026-06-01",
    "endDate": "2026-06-05",
    "status": "ACTIVE",
    "createdAt": "2026-05-22T18:00:00",
    "cancelledAt": null
}
```

**Error — 404 Not Found** (user or room does not exist):
```json
{
    "status": 404,
    "error": "Not Found",
    "message": "Usuario no encontrado",
    "timestamp": "2026-05-22T18:00:00"
}
```

**Error — 409 Conflict** (overlapping dates):
```json
{
    "status": 409,
    "error": "Conflict",
    "message": "La habitación ya está reservada en esas fechas",
    "timestamp": "2026-05-22T18:00:00"
}
```

**Error — 409 Conflict** (invalid dates):
```json
{
    "status": 409,
    "error": "Conflict",
    "message": "La fecha de inicio no puede ser posterior a la fecha de fin",
    "timestamp": "2026-05-22T18:00:00"
}
```

---

### Get all reservations

```
GET http://localhost:8080/reservations
```

**Success response — 200 OK:**
```json
[
    {
        "id": 1,
        "user": { ... },
        "room": { ... },
        "startDate": "2026-06-01",
        "endDate": "2026-06-05",
        "status": "ACTIVE",
        "createdAt": "2026-05-22T18:00:00",
        "cancelledAt": null
    }
]
```

---

### Get reservation by ID

```
GET http://localhost:8080/reservations/{id}
```

**Example:**
```
GET http://localhost:8080/reservations/1
```

**Success response — 200 OK:**
```json
{
    "id": 1,
    "user": { ... },
    "room": { ... },
    "startDate": "2026-06-01",
    "endDate": "2026-06-05",
    "status": "ACTIVE",
    "createdAt": "2026-05-22T18:00:00",
    "cancelledAt": null
}
```

**Error — 404 Not Found:**
```json
{
    "status": 404,
    "error": "Not Found",
    "message": "Reserva no encontrada",
    "timestamp": "2026-05-22T18:00:00"
}
```

---

### Get reservations by user

```
GET http://localhost:8080/users/{userId}/reservations
```

**Example:**
```
GET http://localhost:8080/users/1/reservations
```

**Success response — 200 OK:**
```json
[
    {
        "id": 1,
        "user": { ... },
        "room": { ... },
        "startDate": "2026-06-01",
        "endDate": "2026-06-05",
        "status": "ACTIVE",
        "createdAt": "2026-05-22T18:00:00",
        "cancelledAt": null
    }
]
```

---

### Cancel a reservation

```
PATCH http://localhost:8080/reservations/{id}/cancel
```

**Example:**
```
PATCH http://localhost:8080/reservations/1/cancel
```

No request body needed.

**Success response — 204 No Content** (empty body)

**Error — 404 Not Found:**
```json
{
    "status": 404,
    "error": "Not Found",
    "message": "Reserva no encontrada",
    "timestamp": "2026-05-22T18:00:00"
}
```

**Error — 409 Conflict** (already cancelled):
```json
{
    "status": 409,
    "error": "Conflict",
    "message": "La reserva no puede ser cancelada porque ya está cancelada",
    "timestamp": "2026-05-22T18:00:00"
}
```

---

## HTTP Status codes

| Code | Meaning | When |
|------|---------|------|
| 200 | OK | Successful GET |
| 201 | Created | User, room or reservation created |
| 204 | No Content | Reservation cancelled |
| 400 | Bad Request | Invalid input data |
| 404 | Not Found | Resource does not exist |
| 409 | Conflict | Duplicate or business rule violation |

---

## Overlap rule

A room cannot have two active reservations with overlapping dates. The same rule applies to users.

The checkout date is **not** considered occupied. Example:

```
Reservation A: June 10 → June 15
Reservation B: June 15 → June 20
```

These two reservations are **not** considered overlapping.
