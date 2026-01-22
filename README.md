# Cloud Storage Backend (Spring Boot)

This is the backend service for a cloud storage application built using **Spring Boot**.  
It provides APIs for user authentication, file & folder management, and sharing features.

---

## 🚀 Features

### Authentication
- User signup and login
- JWT based authentication
- Password hashing using BCrypt

### Folder Management
- Create, update, delete folders
- Nested folder support
- Get folder list and folder details

### File Management
- Upload files (supports progress tracking via frontend)
- Rename, move, delete files
- Download files
- Soft delete (Trash) and restore

### Sharing
- Share files/folders via shareable links
- Set permission (view/edit)

---

## 🧱 Tech Stack

| Layer | Technology |
|------|------------|
| Backend | Spring Boot |
| Security | Spring Security + JWT |
| Database | PostgreSQL |
| ORM | Spring Data JPA |
| File Storage | Supabase |

---

