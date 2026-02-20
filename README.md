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

* DTOs are used to separate concerns and prevent exposing JPA entities directly.
* `AppointmentReportDTO` and `FullAppointmentReportDTO` are read-only projections optimized for reporting.
* Caching keys are based on appointment ID and report identifiers to minimize database hits for repeated queries.
* Specification or dynamic queries are not used for appointments since filtering is limited to status.
