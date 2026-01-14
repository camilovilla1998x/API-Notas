# Notes API

## 🏷 Proyecto
**Notes API** – API REST en Spring Boot para manejar notas con CRUD completo, validaciones, manejo de excepciones y arquitectura desacoplada.

---

## ⚡ Tecnologías
- Java 17+  
- Spring Boot 3.x  
- Spring Data JPA  
- MySQL  
- Lombok  
- Validation (`jakarta.validation`)  
- Maven  

---

## 📦 Arquitectura y diseño

### 1️⃣ Capa de persistencia
- **Entity:** `Note`  
  - Campos: `id`, `title`, `content`, `createdAt`, `updatedAt`  
  - `@PrePersist` y `@PreUpdate` para manejo automático de fechas  
  - Setters de `id`, `createdAt` y `updatedAt` protegidos con `AccessLevel.NONE`  

- **Repository:** `NoteRepository`  
  - Extiende `JpaRepository<Note, Long>`  

---

### 2️⃣ DTOs (Data Transfer Objects)
- **Entrada:** `NoteRequest`  
  - Campos: `title`, `content`  
  - Validaciones con `@NotBlank` y `@Size`  
- **Salida:** `NoteResponse`  
  - Campos: `id`, `title`, `content`, `createdAt`, `updatedAt`  

> Los DTOs aseguran que la API no esté acoplada a la base de datos ni a la Entity.

---

### 3️⃣ Mapper
- **`NoteMapper`**  
  - Métodos estáticos para convertir:
    - `NoteRequest → Note`  
    - `Note → NoteResponse`  
  - Responsable **solo de mover datos**, sin lógica de negocio ni acceso a BD.  

---

### 4️⃣ Service
- **Interface:** `NoteService`  
- **Implementación:** `NoteServiceImpl`  
- Funciona como **traductor entre DTOs y Entities**  
- Métodos:
  - `create(NoteRequest)` → `NoteResponse`  
  - `getAll()` → `List<NoteResponse>`  
  - `getById(Long)` → `NoteResponse`  
  - `update(Long, NoteRequest)` → `NoteResponse`  
  - `deleteById(Long)` → `void`  

---

### 5️⃣ Controller
- **`NoteController`** con endpoints CRUD
- Maneja **DTOs directamente** y activa validaciones con `@Valid`
- Endpoints:
| Método | URL | Request | Response |
|--------|-----|---------|---------|
| GET | `/api/notes` | - | `List<NoteResponse>` |
| GET | `/api/notes/{id}` | - | `NoteResponse` |
| POST | `/api/notes` | `NoteRequest` | `NoteResponse` |
| PUT | `/api/notes/{id}` | `NoteRequest` | `NoteResponse` |
| DELETE | `/api/notes/{id}` | - | void |

---

### 6️⃣ Manejo de excepciones
- `NoteNotFoundException` → 404  
- `MethodArgumentNotValidException` → 400 (validaciones)  
- `GlobalExceptionHandler` centraliza las respuestas y devuelve JSON limpio:
```json
{
  "errors": [
    {
      "field": "title",
      "message": "Title must not be blank"
    }
  ]
}
