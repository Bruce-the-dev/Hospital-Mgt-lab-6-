# 🏥 Hospital Management System

## 📖 Overview


## Features Implemented

Patient, Doctor, Department Management
CRUD operations via JPA repositories with caching support.

Appointment Management

Create, read, update, delete appointments

Track appointment status (SCHEDULED, COMPLETED, CANCELLED)

Fetch appointments by patient or by doctor

Generate full appointment reports including patient, doctor, and department info

Identify appointments without prescriptions

DTO-Based Responses

AppointmentInputDTO and AppointmentResponseDTO for basic CRUD

AppointmentReportDTO for patient/doctor reports

FullAppointmentReportDTO for comprehensive reports

Pagination & Sorting

All report endpoints support page/size parameters

Sort by appointment date descending by default

Caching

Frequently accessed appointment data and reports are cached using Spring Cache

Reduces repeated DB queries and improves response times

Validation

Input DTOs include @NotBlank, @NotNull, @Email, @Past annotations

Global exception handler returns standardized validation errors

Swagger/OpenAPI Documentation

All endpoints are annotated with @Operation and @Parameter for interactive documentation

Accessible via /swagger-ui.html

## Example Endpoints
Endpoint	Method	Description
/api/appointments/patient/{id}	GET	Get paginated appointments for a patient
/api/appointments/doctor/{id}	GET	Get paginated appointments for a doctor
/api/appointments/reports/full	GET	Get full appointment report (patient + doctor + department)
/api/appointments	POST	Create a new appointment
/api/appointments/{id}	PUT	Update an existing appointment
/api/appointments/{id}	DELETE	Delete an appointment

### Technologies Used


Spring Data JPA with Hibernate
PostgreSQL
Spring Cache (Caffeine/ConcurrentMap)
Validation API (javax.validation)
Springdoc OpenAPI / Swagger UI

### Notes

DTOs are used for separation of concerns, avoiding exposing JPA entities directly.

AppointmentReportDTO and FullAppointmentReportDTO are read-only projections optimized for reporting.

Caching uses appointment ID and report keys, ensuring minimal database hits for repeated queries.

Specification / dynamic queries are not used in Appointment since filtering is limited to status.

---
