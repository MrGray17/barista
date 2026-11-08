# Barista Learning Log

> A living record of the learning journey and engineering progress behind Barista.
>
> **Last updated:** 2026-11-08

## Why this file exists

This file records what has been taught, attempted, implemented, decided, and left open. It is the project's shared memory: update it at the end of meaningful lessons and milestones so progress does not depend on chat history.

Keep claims precise:
- **Introduced** means the topic has been explained.
- **Practised** means the learner has attempted an exercise or implementation.
- **Demonstrated** means the learner can explain or apply it independently.
- **Implemented** means working code exists in the repository.
- **Verified** means tests or a reproducible experiment support the claim.

Do not mark a topic complete merely because it was discussed.

## Project

**Name:** Barista  
**Description:** A self-hosted distributed job queue and workflow engine, initially backed by PostgreSQL, with a Java 21 / Spring Boot API and separate worker processes.

### Product goals

- Accept jobs through a language-independent HTTP API.
- Persist jobs and their execution history.
- Let multiple workers claim and execute jobs safely.
- Support at-least-once execution with retries and recovery from worker crashes.
- Make execution inspectable and observable.
- Later support scheduling, priorities, rate limiting, workflows, a dashboard, and production deployment.

### Initial constraints

- Start with PostgreSQL as the source of truth.
- Begin as a modular Java application with workers that can run as separate processes.
- Avoid Kafka, Redis, Kubernetes, microservices, and exactly-once claims in the initial version.
- Introduce complexity only when the project needs it and the learner has the foundations to understand it.

## Learner starting point

- Knows basic Java.
- Has no prior practical knowledge of Spring Boot or Java backend frameworks.
- Is new to the project's domain: job queues, workers, and distributed-system reliability.
- The project is also a structured way to learn backend engineering and software engineering.

Teaching must start from first principles. Do not assume knowledge of HTTP, servers, databases, concurrency, or distributed systems without checking it.

## Learning protocol

Use this cycle for each meaningful topic:

1. **Concept:** explain the problem and the underlying idea from first principles.
2. **Visualize:** use examples, diagrams, timelines, and execution traces.
3. **Design:** guide the learner to reason about responsibilities, constraints, and failure cases.
4. **Build:** the learner writes the important code with progressive hints.
5. **Review:** inspect correctness, clarity, maintainability, and trade-offs.
6. **Break:** test edge cases and deliberately exercise failure scenarios.
7. **Retain:** ask the learner to explain the idea or solve a variation independently.

Start with substantial guidance, then gradually reduce it as competence grows. Never confuse copied code or a successful build with understanding.

## Roadmap

The sequence below is a guide, not a rigid deadline. Testing, logging, security awareness, and good engineering practices should appear as soon as they are relevant.

| Stage | Main topics | Intended outcome |
|---|---|---|
| 0. Foundations | Project purpose, HTTP/client-server basics, Java essentials, Git, Maven, Spring Boot fundamentals | Explain the problem and run a basic Java/Spring application |
| 1. Job domain | Classes, objects, methods, enums, interfaces, collections, exceptions, handler abstraction | Model a job and its lifecycle in Java |
| 2. Backend API | HTTP, REST, JSON, Spring controllers/services, dependency injection, validation, DTOs | Submit and inspect jobs through an API |
| 3. Persistence | SQL, PostgreSQL, Docker, schema design, Flyway, JDBC/JPA, transactions, indexes | Persist jobs and retrieve them after restart |
| 4. Workers | Worker loop, handler registry, thread pools, job claiming, database locking, state machine | Execute jobs with multiple competing workers safely |
| 5. Reliability | Leases, heartbeats, crash recovery, retries, backoff, idempotency, timeouts, dead-letter handling | Recover work after failures with at-least-once semantics |
| 6. Scheduling and control | Delayed/recurring jobs, cron, priorities, fairness, rate limiting, backpressure | Control when and how quickly work executes |
| 7. Workflows | Dependencies, DAGs, fan-out/fan-in, cycle detection, workflow recovery | Orchestrate multi-step jobs |
| 8. Testing and resilience | JUnit, Mockito, Testcontainers, integration/concurrency tests, failure experiments | Verify critical guarantees with repeatable tests |
| 9. Operations and security | Actuator, Micrometer, Prometheus, Grafana, logs, authentication, authorization | Operate and diagnose the service safely |
| 10. Dashboard and DX | React, TypeScript, OpenAPI, TanStack Query, SSE, SDK | Let developers and operators use Barista comfortably |
| 11. Production | Docker, CI/CD, deployment, backups, load tests, profiling, documentation | Make Barista reproducible, deployable, measurable, and maintainable |

## Progress record

### 2026-11-08 — Project orientation and background-processing concepts

**Introduced**
- Barista's purpose: accept work separately from executing it.
- A job is a unit of work, typically described by a type and payload, with an identity and lifecycle state.
- A worker is a process/program that retrieves and executes jobs.
- A job queue organizes work waiting for execution.
- HTTP is a request/response protocol commonly used between clients (browser/mobile/other services) and server applications.
- In a photo-upload example, the client sends an HTTP request to the application's server. The server can save the photo and durably record processing work, then respond while a worker processes the image separately.
- A queue does not make background execution possible by itself; alternatives include synchronous execution, in-process threads/executors, scheduled scripts, and direct service calls.
- These alternatives have trade-offs. In-memory background tasks may be lost if the process crashes unless work is persisted elsewhere.

**Important distinctions introduced**
- A response saying a job was accepted does not mean the job has completed.
- A worker executes work; durable storage and recovery require additional mechanisms.
- Not every operation belongs in a queue. Quick operations requiring an immediate result are often synchronous.
- At-least-once execution can repeat a job after a crash. It is not an exactly-once guarantee.

**Understanding status:** Introduced, but not yet independently demonstrated. The learner asked for clarification about which HTTP request was meant; the client/server request flow was then explained using a photo-upload example. Revisit with a short teach-back before advancing too far.

**Not yet implemented**
- No application code or job-processing functionality has been implemented in this repository.
- The repository was confirmed empty before this log was created.

**Next lesson**
1. Re-check the distinction between client, server, HTTP request, and HTTP response.
2. Explain what a server is and how a Java application can listen for requests.
3. Explain what Spring Boot does at a conceptual level before generating a project.
4. Return to the repository setup and create/run the smallest Spring Boot application.

## Engineering decisions and glossary

Record meaningful decisions here as they are made, including the problem, options, decision, and trade-offs. Do not invent decisions before they are discussed.

| Term | Working definition |
|---|---|
| Client | A program that initiates a request to another program, commonly a browser or another service |
| Server | A program that listens for and responds to requests or provides a service |
| HTTP request | A message a client sends to an HTTP server |
| HTTP response | The server's reply to an HTTP request |
| Job | A description of work the system is asked to perform |
| Queue | An ordered/managed collection of work waiting to be processed |
| Worker | A program/process that claims and executes jobs |
| Background processing | Performing work outside the immediate request/response path |

## Update checklist for future sessions

At the end of each meaningful lesson or coding session, update:

- **Topics introduced**
- **Exercises attempted and outcome**
- **Concepts demonstrated independently**
- **Files/classes/features implemented**
- **Tests run and results**
- **Engineering decisions and reasons**
- **Open questions or misunderstandings**
- **Exact next step**

Keep this log concise enough to maintain, but specific enough that a new session can resume without guessing.

### 2026-11-08 — Connecting server fundamentals to prior Node.js experience

**Learner context clarified**
- The learner has previously implemented a server using TypeScript and Node.js.
- Client/server communication, listening servers, and HTTP request/response are therefore familiar concepts from prior practical work; use them as a bridge rather than reteaching them from zero.
- The learner has not yet established equivalent knowledge of Java server development or Spring Boot.

**Teaching adjustment**
- Compare Node.js concepts with Java/Spring Boot equivalents where useful.
- Avoid assuming which Node framework or APIs were used; ask or inspect an example before making framework-specific comparisons.
- Focus on new material: Java runtime/application startup, embedded server integration, Spring application context, dependency injection, controllers, services, and framework lifecycle.

**Understanding status:** Prior hands-on exposure reported by learner; exact implementation details not yet reviewed. Do not mark Spring Boot knowledge as demonstrated.

