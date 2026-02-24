# 🏥 Hospital Management System

## Overview

A comprehensive hospital management system to manage patients, doctors, departments, and appointments. This system leverages Spring Boot, JPA, caching, validation, and OpenAPI documentation to provide efficient CRUD operations and detailed reports.

---

## Features Implemented

### Patient, Doctor, and Department Management

* CRUD operations via JPA repositories.
* Caching support to reduce database hits.

### Appointment Management

* Create, read, update, and delete appointments.
* Track appointment status: `SCHEDULED`, `COMPLETED`, `CANCELLED`.
* Fetch appointments by patient or doctor.
* Generate detailed appointment reports including patient, doctor, and department info.
* Identify appointments without prescriptions.

### DTO-Based Responses

* `AppointmentInputDTO` and `AppointmentResponseDTO` for basic CRUD operations.
* `AppointmentReportDTO` for patient/doctor-specific reports.
* `FullAppointmentReportDTO` for comprehensive reports.

### Pagination & Sorting

* All report endpoints support `page` and `size` parameters.
* Default sorting by appointment date in descending order.

### Caching

* Frequently accessed appointment data and reports are cached using Spring Cache.
* Reduces repeated DB queries and improves response times.

### Transaction Management

* `@Transactional` applied to all service methods.
* **Rollback Strategy:** Creating a prescription checks inventory levels. If stock is insufficient, a `RuntimeException` triggers rollback of the entire transaction (no partial inventory deductions).

### Validation

* Input DTOs include annotations: `@NotBlank`, `@NotNull`, `@Email`, `@Past`.
* Global exception handler returns standardized validation error responses.

### Swagger/OpenAPI Documentation

* All endpoints are annotated with `@Operation` and `@Parameter`.
* Interactive API documentation accessible at `/swagger-ui.html`.

---

##  Example Endpoints

| Endpoint                         | Method | Description                                                 |
|----------------------------------|--------|-------------------------------------------------------------|
| `/api/appointments/patient/{id}` | GET    | Get paginated appointments for a patient                    |
| `/api/appointments/doctor/{id}`  | GET    | Get paginated appointments for a doctor                     |
| `/api/appointments/reports/full` | GET    | Get full appointment report (patient + doctor + department) |
| `/api/appointments`              | POST   | Create a new appointment                                    |
| `/api/appointments/{id}`         | PUT    | Update an existing appointment                              |
| `/api/appointments/{id}`         | DELETE | Delete an appointment                                       |

---

## Technologies Used

* Spring Boot with Spring Data JPA and Hibernate
* PostgreSQL
* Spring Cache (Caffeine/ConcurrentMap)
* Validation API (`javax.validation`)
* Springdoc OpenAPI / Swagger UI

---

## Notes

---

## 🔒 Security Concepts (Lab 7)

### CORS vs CSRF

#### CORS (Cross-Origin Resource Sharing)
**What is it?** A browser security feature that restricts cross-origin HTTP requests.
-   **Scenario**: Your frontend is at `localhost:3000` and backend at `localhost:8080`.
-   **Why allow it?** To let the frontend fetch data from the backend.
-   **Config**: We allowed `localhost:3000` in `SecurityConfig.java`.

#### CSRF (Cross-Site Request Forgery)
**What is it?** An attack where a user is tricked into executing unwanted actions on a web application in which they are currently authenticated.
-   **Why disable it?** We use **JWT (Stateless)** authentication.
-   **Explanation**: CSRF attacks rely on the browser automatically sending cookies (session IDs). Since we send our token manually in the `Authorization` header, the browser doesn't send it automatically for malicious links, making standard CSRF attacks impossible. Therefore, we disable CSRF for our API.

### 🧪 How to Test

#### 1. OAuth2 Login
1.  Open your browser to `http://localhost:8080/oauth2/authorization/google`.
2.  Login with your Google account.
3.  You will be redirected back (check the URL/logs).
4.  Check the database: `SELECT * FROM users;`. You should see your Google email with role `RECEPTIONIST`.

#### 2. Token Blacklisting (Logout)
1.  **Login** via Postman (`POST /auth/login`) to get a JWT.
2.  **Access** a protected route (e.g., `GET /api/patients`) -> **200 OK**.
3.  **Logout** (`POST /auth/logout`) with the token in Header.
4.  **Access** the protected route again -> **401 Unauthorized**.

