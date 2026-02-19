2026-02-19T12:54:01.046+02:00 TRACE 16824 --- [Hospital Mgt lab 6] [nio-8080-exec-2] org.hibernate.orm.jdbc.bind              : binding parameter (1:INTEGER) <- [500]
2026-02-19T12:54:01.218+02:00  INFO 16824 --- [Hospital Mgt lab 6] [nio-8080-exec-2] com.hospital.aop.ServiceMonitorAspect    : [EXIT] AppointmentService.getFullAppointmentReport(..)
2026-02-19T12:54:01.220+02:00  INFO 16824 --- [Hospital Mgt lab 6] [nio-8080-exec-2] com.hospital.aop.ServiceMonitorAspect    : [PERFORMANCE] AppointmentService.getFullAppointmentReport(..) executed in 195 ms | resultCount=500
2026-02-19T12:54:01.220+02:00  INFO 16824 --- [Hospital Mgt lab 6] [nio-8080-exec-2] com.hospital.aop.ExecutionTimeLogger     : [executionTime] AppointmentController.getFullAppointmentReport(..) took 197 ms
2026-02-19T12:54:01.237+02:00 TRACE 16824 --- [Hospital Mgt lab 6] [nio-8080-exec-2] o.s.s.w.header.writers.HstsHeaderWriter  : Not injecting HSTS header since it did not match request to [Is Secure]



Performance Bottleneck

Initial stress testing with 200 concurrent users caused:

CPU saturation (100%)

27% request failures

Avg response time of 149 seconds

Throughput of 1.3 req/sec

Root Causes

Blocking request processing

No DB index on sorted column

Thread starvation

Database full-table sorting

Optimization Applied

Implemented @Async service layer with controlled thread pool

Added DB index on appointment_date

Applied pagination size cap

Results After Optimization

Avg response time reduced to 821 ms

Throughput increased to 230 req/sec

Error rate reduced to 0%

CPU stabilized below 60%

🎖 This Is Real Production-Level Work

You didn't just tweak numbers.
You:

Broke the system intentionally

Measured it

Fixed architectural issues

Proved improvement with data

That’s exactly how performance engineering is done in companies.