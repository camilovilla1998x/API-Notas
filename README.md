# 📝 Notes API — Spring Boot RESTful Service

## 📌 Descripción

**Notes API** es una API REST desarrollada con **Spring Boot** que permite gestionar notas mediante operaciones CRUD (Create, Read, Update, Delete).  
El proyecto fue construido con un enfoque **profesional**, aplicando buenas prácticas de arquitectura, validaciones, manejo de excepciones y pruebas.

Este proyecto sirve como **base sólida para aplicaciones backend reales** y como práctica avanzada de Spring Boot.

---

## 🛠️ Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- MySQL
- Jakarta Bean Validation
- Lombok
- JUnit 5
- Mockito
- MockMvc
- Maven

---

## 📂 Estructura del proyecto

```
src/main/java/com/example/notes_api
│
├── controller
│   └── NoteController.java
│
├── service
│   ├── NoteService.java
│   └── impl
│       └── NoteServiceImpl.java
│
├── repository
│   └── NoteRepository.java
│
├── entity
│   └── Note.java
│
├── dto
│   ├── request
│   │   └── NoteRequest.java
│   ├── response
│   │   └── NoteResponse.java
│   └── error
│       ├── ApiErrorResponse.java
│       └── ValidationErrorResponse.java
│
├── mapper
│   └── NoteMapper.java
│
├── exception
│   └── NoteNotFoundException.java
│
├── handler
│   └── GlobalExceptionHandler.java
│
└── NotesApiApplication.java
```

---

## 🧱 Modelo de dominio

### 🗒️ Note (Entity)

Representa una nota persistida en base de datos.

- `id`
- `title`
- `content`
- `createdAt`
- `updatedAt`

Características:
- Uso de `@PrePersist` y `@PreUpdate` para auditoría automática
- No se exponen setters para campos sensibles (`id`, `createdAt`, `updatedAt`)

---

## 🔁 Endpoints disponibles

### ➕ Crear nota
`POST /api/notes`

### 📄 Obtener todas las notas
`GET /api/notes`

### 🔍 Obtener nota por ID
`GET /api/notes/{id}`

### ✏️ Actualizar nota
`PUT /api/notes/{id}`

### 🗑️ Eliminar nota
`DELETE /api/notes/{id}`

- DELETE exitoso devuelve **204 No Content**

---

## 📦 DTOs

### NoteRequest
Usado para crear y actualizar notas.

Validaciones:
- `title`: obligatorio, máximo 100 caracteres
- `content`: obligatorio

### NoteResponse
Usado para exponer datos al cliente sin filtrar la entidad.

---

## 🔄 Mapper

El mapper se encarga de:
- Convertir `NoteRequest` → `Note`
- Convertir `Note` → `NoteResponse`

Esto evita:
- Exponer entidades directamente
- Acoplamiento innecesario entre capas

---

## ❌ Manejo de excepciones

### Excepciones personalizadas
- `NoteNotFoundException` → cuando una nota no existe

### GlobalExceptionHandler
Centraliza el manejo de errores usando `@RestControllerAdvice`.

Maneja:
- `NoteNotFoundException` → **404 Not Found**
- `MethodArgumentNotValidException` → **400 Bad Request**

Ejemplo de respuesta de validación:

```json
{
  "errors": [
    {
      "field": "title",
      "message": "Title must not be blank"
    }
  ]
}
```

---

## ✅ Validaciones

- Uso de `@Valid` en los controllers
- Validaciones declarativas con Bean Validation
- Respuestas claras y consistentes

---

## 🧪 Testing

### Tests de controlador

- `@WebMvcTest`
- `MockMvc`
- `MockitoBean`
- Tests por endpoint

Se validan:
- Status HTTP
- Respuesta JSON
- Manejo de errores
- Contrato REST

Los tests **no dependen de base de datos**.

---

## 🧠 Buenas prácticas aplicadas

- Controllers delgados
- Service por interfaz
- DTOs para entrada y salida
- Mapper dedicado
- Manejo centralizado de excepciones
- Tests de contrato HTTP
- Uso correcto de códigos de estado REST

---

## 🚀 Ejecución del proyecto

1. Crear base de datos MySQL:
```sql
CREATE DATABASE notes_db;
```

2. Configurar `application.properties`

3. Ejecutar:
```bash
mvn spring-boot:run
```

---

## 📈 Estado del proyecto

✔ CRUD completo  
✔ Arquitectura limpia  
✔ Validaciones  
✔ Manejo de excepciones  
✔ Tests  
✔ Listo para producción básica  

---

## 📌 Próximos pasos sugeridos

- Swagger / OpenAPI
- ResponseEntity
- Paginación y sorting
- Seguridad (JWT)

---

## 👨‍💻 Autor
Camilo Villa Agudelo

Proyecto desarrollado como práctica avanzada de Spring Boot y arquitectura backend.
