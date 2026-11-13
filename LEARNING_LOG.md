# Barista Learning Log

> A living record of the learning journey and engineering progress behind Barista.
>
> **Last updated:** 2026-11-11

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

### 2026-11-08 — Previous Node.js experience clarified

- Learner once implemented a server with Node.js's native `http` module, but this was some time ago and the implementation is not remembered.
- Treat this as prior exposure, not current demonstrated knowledge. Do not quiz the learner on that old code.
- Learner has now clarified that basic Java and JVM concepts are already familiar.
- Do not restart with elementary Java syntax or a generic JDK/JVM/JRE lesson. Build on the learner's stated baseline, and fill gaps when they become relevant in implementation.
- Spring Boot and Java backend framework knowledge remain new and should be taught carefully from first principles.

### Learning checkpoint 00 — Project purpose and client/server foundations

**Prior experience reported, but not currently retained**
- Learner mentioned having implemented a server with Node.js's native `http` module a long time ago.
- Learner does not remember the project details or implementation.
- Do not assume this is usable working knowledge, and do not quiz the learner on that old implementation.
- Teach client/server and HTTP fundamentals from the ground up. Node.js can be mentioned as an optional comparison only when it genuinely helps.

**Topics covered in this learning unit**
- Barista is intended to accept background work separately from executing it.
- HTTP follows a client-request / server-response model.
- A browser or another application can act as an HTTP client; a server application receives and handles requests.
- A server is a program providing a service, not necessarily a dedicated physical machine.
- A port identifies a network endpoint used by an application; `localhost:8080` refers to port 8080 on the same machine.
- Spring Boot can start an embedded web server (commonly Tomcat for a Spring MVC application) so a Java application can receive HTTP requests.
- A job queue is one way to manage work for later execution. Synchronous calls, in-process background threads/executors, scheduled scripts, and direct service calls are alternatives with different trade-offs.
- A response that says a job was accepted is not a claim that the job has completed.
- Durable job acceptance requires recording the work durably before acknowledging it; a transient in-memory task can be lost when its process stops.

**Checkpoint status**
- **Familiarity reported by learner:** basic Java and JVM concepts. Do not reteach these from zero; validate specific gaps naturally through project work.
- **Prior exposure only:** a Node.js native HTTP server was implemented in the past, but its details are not remembered.
- **Introduced, not yet independently demonstrated in this project:** client/server request flow, HTTP requests/responses, ports, localhost, and the role of Spring Boot.
- **Not yet covered in depth:** TCP sockets, DNS, HTTP message structure, Java web-server internals, Spring ApplicationContext, beans, dependency injection, request routing, and application lifecycle.
- **Implemented in Barista:** only this learning log; no application code exists yet.

**Checkpoint review questions**
1. In general, what is the difference between a client and a server?
2. What is the difference between a client, a server program, and the physical machine hosting that program?
3. When a browser visits `http://localhost:8080`, what does `localhost` identify and what does `8080` identify?
4. Why does an accepted background job need durable storage before the API confirms acceptance?

**Next step**
- Do not spend more time on elementary Java or JVM explanations unless implementation reveals a concrete gap.
- Finish the current introductory networking thread only as needed: client/server, URL host and port, HTTP request routing, and the role of an embedded web server.
- Move next into Spring Boot's application startup, embedded server, application context, beans, and dependency injection, explaining each new framework concept from first principles.

## Checkpoint policy

Create a learning checkpoint after each meaningful concept cluster, design milestone, implementation milestone, or significant debugging session—not after every small message.

Each checkpoint should separate:
- What was explained.
- What the learner attempted.
- What the learner can currently explain or demonstrate independently.
- What was implemented and how it was verified.
- Misconceptions, gaps, or open questions.
- The exact next step.

Update this file at those checkpoints. Keep the main progress record concise and factual; never mark learning as mastered solely because an explanation was provided.



### 2026-11-08 — Learning checkpoint 01: Learner's Java baseline clarified

**Learner clarification**
- Learner is already familiar with basic Java and JVM concepts.
- The old Node.js native HTTP server project is not remembered, so it should not be used as a recall exercise.

**Teaching adjustment**
- Treat basic Java/JVM as existing familiarity, not as a topic to reteach wholesale.
- Begin with the Spring Boot/backend concepts that are new, and revisit Java details selectively when actual code requires them.
- Continue to explain unfamiliar framework and networking concepts from first principles, without assuming that basic Java familiarity implies knowledge of Spring, HTTP internals, or distributed systems.

**Current project state**
- GitHub repository: `MrGray17/barista`.
- `LEARNING_LOG.md` exists and is maintained in the repository.
- No Barista application code has been implemented yet.
- Lesson 03 introduced URL components, localhost, ports, TCP at a high level, sockets, and the high-level Spring Boot startup flow. The learner has not completed the checkpoint exercise about identifying the port in a URL.

**Next step**
- Avoid a generic JVM/JDK lesson.
- Continue with the Spring Boot application lifecycle and how an embedded HTTP server is configured and started.
- Before generating the application, confirm the local repository is synchronized and review the current local setup; then create the smallest Spring Boot project and inspect every generated file relevant to startup.

### 2026-11-08 — Lesson 04: Frameworks, Spring Framework, and dependency injection (introductory)

**Topics introduced**
- A framework provides application structure, conventions, reusable infrastructure, and extension points.
- Library vs framework: application code typically calls a library; a framework often controls part of the lifecycle and invokes application code through extension points.
- Inversion of Control (IoC): control over some object creation and application flow is delegated to a framework/container.
- Spring Framework provides infrastructure for Java applications, including an IoC container / application context, dependency injection, web MVC, transaction management, and integrations.
- Dependency Injection (DI): a class receives its dependencies from outside rather than constructing them itself.
- Barista example: `JobService` depends on `JobRepository`; manual constructor injection is ordinary Java and Spring can later manage supplying the dependency.

**Understanding status**
- These concepts have been explained with an initial conceptual example.
- No Spring code has been generated or run.
- No independent teach-back or exercise has been completed yet.
- Do not mark IoC or DI as demonstrated/mastered.

**Next topics**
- Clarify Spring Framework vs Spring Boot.
- Explain starters, conditional auto-configuration, embedded servers, and the simplified application startup lifecycle.
- Explain why the Barista API process will use a web server while a worker process may be a non-web application.
- Then check understanding with a small design exercise before generating the Spring Boot project.

### 2026-11-08 — Learning adjustment: Spring vs Spring Boot needs a simpler explanation

**Learner feedback**
- Learner reported losing the thread during the Spring lesson and reasonably assumed Spring Boot and Spring were the same thing.
- The previous explanation introduced too many terms (framework, library, IoC, DI, application context, beans, auto-configuration, starters, embedded server) in one lesson.

**Teaching correction**
- Explain only the essential distinction first:
  - Spring Framework is the underlying framework providing core capabilities such as dependency injection and Spring MVC.
  - Spring Boot builds on Spring Framework and makes Spring applications easier to set up, configure, and run (for example through starters, auto-configuration, and embedded servers).
  - They are not alternatives; a typical Spring Boot application uses Spring Framework underneath.
- Acknowledge that people casually say "Spring" to refer to the broader Spring ecosystem or a Spring Boot application, which can make the names sound interchangeable.
- Do not introduce more Spring internals until this distinction is understood.
- Use one concrete analogy and one Barista-specific example, then ask a single short check question.
- Reduce lesson density and avoid advancing to Maven/project generation until the learner is comfortable.

**Mastery status**
- Spring vs Spring Boot: distinction needs to be retaught simply; not yet demonstrated.
- DI, IoC, application context, beans, auto-configuration: previously mentioned, but should be treated as not yet understood/mastered and revisited later in smaller steps.

### 2026-11-08 — Clarification: web applications are not JavaScript-only

**Concept clarified**
- "Web application" is a broad term; it does not mean "an application written in JavaScript".
- A web system commonly has a browser-facing frontend and a backend server, and these can be written in different languages.
- The frontend commonly uses HTML, CSS, and JavaScript/TypeScript in the browser.
- The backend can be written in Java with Spring Boot, JavaScript/TypeScript with Node.js, Python, C#, Go, etc.
- The browser communicates with the backend through HTTP; the browser does not need to know which programming language the server uses.
- Barista's Spring Boot component is the backend HTTP API. A separate React + TypeScript dashboard may be added later as the frontend/operator interface.
- More precisely, Barista itself is initially a backend service/API, not a browser UI.

**Teaching note**
- Avoid using "web app" ambiguously. Say "backend web service/API" for Spring Boot and "browser frontend/dashboard" for React.

### 2026-11-08 — Checkpoint 03: Backend service vs browser frontend

**Clarified**
- "Web application" does not mean JavaScript-only.
- A browser frontend is commonly built with HTML/CSS/JavaScript or TypeScript.
- A backend HTTP service can be built with Java/Spring Boot, Node.js, Python, C#, Go, and other technologies.
- Barista's initial Spring Boot component is a backend API/service. A React + TypeScript operator dashboard is a separate frontend that may be added later.
- HTTP provides a language-independent communication contract between the browser/client and backend.

**Learner response**
- Learner confirmed that the frontend/backend distinction now makes sense.
- Treat as an initial clarification, not yet a detailed independent teach-back.

**Next step**
- Continue Spring fundamentals one concept at a time. Revisit dependency injection with a small plain-Java example before discussing Spring annotations or the application context.

### 2026-11-09 — Architecture lesson: Modular monolith vs microservices

**Topics introduced**
- A modular monolith organizes one logical application into cohesive modules with explicit boundaries.
- A traditional modular monolith is generally built and deployed as one application unit; its modules commonly interact through in-process interfaces/method calls.
- Microservices are independently deployable services organized around focused capabilities, typically communicating through network APIs or messaging.
- Microservices require additional distributed-systems work: network failures, timeouts, retries, observability across services, data consistency, deployment coordination, and service-to-service security.
- Service-owned data is an important microservices principle: other services should normally access a service's data through its contracts rather than directly modifying its tables.
- A separate process does not automatically make a component a microservice. Deployment autonomy, ownership boundaries, data ownership, and communication contracts matter.
- Barista may have a shared Java codebase and modular domain while running API and worker roles as separate process types. Describe this precisely as a modular application with separate runtime roles; do not call every process a microservice.

**Current recommendation**
- Start Barista as a modular monolith/shared codebase with explicit module boundaries.
- Use PostgreSQL as the initial source of truth.
- Run worker processes separately when necessary for execution and scaling, without prematurely splitting the product into independently owned microservices.
- Reconsider microservices only when actual requirements justify independent deployment, scaling, ownership, or fault isolation.

**Understanding status**
- Difference between modular monolith and microservices has been explained using Barista examples.
- Learner has not yet completed a teach-back or design exercise; do not mark as mastered.

**Next step**
- Ask learner to explain the main distinction in their own words or apply it to an API/worker deployment scenario.
- Then continue the current Spring learning path in smaller concept units; do not jump ahead into service decomposition.

### 2026-11-09 — Architecture proposal: initial Barista structure

**Proposed architecture (not yet marked as learner-approved)**
- One repository and initially one Maven project, organized by feature/module.
- A pragmatic layered flow inside features: inbound adapters/controllers → application use cases/services → domain rules.
- Use ports/interfaces and infrastructure adapters at meaningful boundaries, especially persistence. Keep the dependency direction inward.
- API and worker are separate runtime roles/processes that share code where appropriate; multiple worker processes can scale independently.
- PostgreSQL is the initial durable source of truth and coordination mechanism.
- job_events is initially an append-only audit history, not an event bus or event-sourced architecture.

**Approaches deliberately deferred**
- Microservices: operational and network complexity is not justified yet.
- Full Clean/Hexagonal ceremony: adopt dependency boundaries, but avoid unnecessary abstraction.
- Event-driven coordination for everything: use direct calls within modules and PostgreSQL coordination initially; add events when there is a real use case.
- Full CQRS with separate read/write stores: ordinary PostgreSQL queries are sufficient initially.
- Kafka/Redis/RabbitMQ: evaluate only after a measured need.

**Important nuance**
- Separate API and worker processes do not automatically make the system microservices. The important questions are independent ownership, deployment boundaries, data ownership, and communication contracts.
- A modular codebase with separate runtime roles is the intended starting point.

**Architecture decision record**
- Proposed details: docs/adr/0001-initial-architecture.md
- Status: Proposed; learner should understand and challenge the trade-offs before treating it as an accepted project decision.

**Next learning step**
- Review the proposed architecture in plain language, confirm understanding, and refine it if needed.
- Then return to Spring fundamentals at a slower pace before generating the first Spring Boot application.

### 2026-11-09 — Maven checkpoint: dependencies vs build plugins

**Learner response**
- Correctly selected `<dependencies>` for the PostgreSQL JDBC driver.
- Reason given: it provides functionality used by application code, rather than being a build plugin.

**Clarification**
- A dependency is a library/framework artifact made available on the project's compile and/or runtime classpath.
- A plugin is tooling Maven invokes as part of the build lifecycle or another Maven goal (for example, compiler, test, or Spring Boot Maven plugins).
- Avoid defining the distinction as "dependencies are not for compilation": many dependencies are required to compile source code, while others (such as the PostgreSQL JDBC driver in a typical Spring Boot setup) are needed at runtime.
- The key distinction is application functionality/classpath vs build-process functionality.

**Understanding status**
- Basic purpose of Maven and why imports alone do not download external libraries: explained.
- Dependencies vs plugins: learner selected the correct section and gave the core distinction; the compile/runtime nuance has now been clarified.
- Maven's real generated POM has not yet been inspected.

**Next step**
- Generate the initial Spring Boot Maven project for Barista, inspect the generated POM, and explain the parent, properties, dependencies, and plugins in context.

### 2026-11-09 — Learning adjustment: keep POM lessons incremental

**Learner feedback**
- The detailed walkthrough of the generated pom.xml was too information-dense.
- Learner prefers to defer detailed POM study until the relevant pieces are needed.

**Teaching adjustment**
- Do not explain every generated file or POM section in one lesson.
- Introduce only the minimum concept needed for the next practical action, then return to details when they become relevant.
- Current takeaway only: pom.xml tells Maven about the project, its dependencies, and build configuration.
- Do not treat the POM lesson as mastered. Revisit parent, properties, dependencies, scopes, and plugins incrementally during actual project work.

**Next step**
- Stop the dependency-tree exploration for now.
- Run a simple Maven build using the project wrapper, inspect the result, then start the generated Spring Boot application.
- Explain only what each command does as it is needed.



### 2026-11-09 — Learning checkpoint: first Spring Boot build

**Project setup**
- Learner generated the initial Spring Boot project locally using Spring Initializr.
- Project configuration: Maven, Java 21, Spring Boot 4.1.2, JAR packaging, package/group `com.barista`, and Spring MVC web dependency.
- The generated project is in the intended local `barista/` project root (not nested in another `barista/` directory).

**Concepts introduced**
- Maven Wrapper downloads/uses the project's configured Maven distribution when needed.
- On the first build, Maven may download project dependencies, transitive dependencies, and build plugins; artifacts are cached in the local Maven repository (normally `~/.m2/repository`) for reuse.
- `clean` removes generated build output, and `package` runs the preceding lifecycle phases needed to compile, test, and package the project.
- Java compilation produces `.class` bytecode. Packaging does not merge all classes into one bytecode file: a JAR is an archive containing application classes, resources, and (for an executable Spring Boot JAR) packaged dependency JARs.
- The JVM still executes bytecode. With `java -jar`, the executable Spring Boot JAR's launcher helps load the application and its packaged dependencies. The class loader loads classes as needed; it does not necessarily load every class all at once.

**Attempt and verification**
- Learner ran the Maven Wrapper build command (`clean package`) from the project root.
- Learner reported the build completed successfully with `BUILD SUCCESS` and a `target/` directory was present.
- The first build downloaded dependencies and build tooling.
- This verifies that the generated project can be built locally. It does not yet verify that the application starts or handles HTTP requests.

**Understanding status**
- Learner connected JAR packaging to the bytecode/JVM model and described the JAR as grouping class files for the JVM.
- Refined model: the JAR is a container/archive, not a funnel that sends every class to the JVM simultaneously. The JVM's class-loading mechanism loads required classes as the application needs them.
- Treat JAR packaging as introduced and initially understood; reinforce through actually running the JAR rather than marking it fully mastered.

**Next step**
- Run the generated executable JAR with the Java launcher.
- Confirm the Spring Boot application starts, identify the embedded server startup log, and understand what is listening on the configured HTTP port.
- No Barista job-queue functionality has been implemented yet.


### 2026-11-09 — Runtime checkpoint: first successful Spring Boot startup

**Attempt and verification**
- Learner executed the generated executable JAR using `java -jar target/barista-0.0.1-SNAPSHOT.jar`.
- Learner reported that the application started and the terminal indicates it is listening.
- This is the first successful runtime startup of the generated Barista Spring Boot application.
- The process remains attached to the terminal while the server runs; this is expected.

**Concepts introduced**
- Building/package creation and application execution are distinct operations: Maven's `clean package` lifecycle creates the JAR; `java -jar` launches the packaged application in the JVM.
- Spring Boot starts the embedded Tomcat server for this Spring MVC web application.
- A server listening on port 8080 is ready to receive HTTP requests. This does not imply that application routes/endpoints have been implemented.
- Requesting `http://localhost:8080/` may return 404 because no controller route has been created yet; this would not, by itself, mean the server failed to start.

**Understanding status**
- Learner asked for clarification about the distinction between building and running. It was explained with a two-step flow, but no independent teach-back was recorded.
- Successful startup verifies the runtime path, not yet the learner's independent understanding of Maven/JAR/JVM responsibilities.
- Do not mark Spring Boot internals, HTTP routing, or dependency injection as mastered.

**Next step**
- Send an HTTP request to `http://localhost:8080/` in a browser and inspect the response.
- Use the result to distinguish "server is running and received the request" from "an application endpoint exists and handled the request."
- Then introduce the smallest controller/endpoint so Barista can return a deliberate response.


### 2026-11-11 — Learning checkpoint: Java packages, Spring annotations, and editor diagnosis

**Package concepts introduced**
- Java package declarations identify a class's logical namespace.
- In the conventional Java source layout, the package name mirrors the directory path under the source root. For example, `src/main/java/com/barista/controller/JobController.java` declares `package com.barista.controller;`.
- `src/main/java` is the source root and is not part of the package name.
- Packages provide organization, qualified class names/namespaces, and package-level access boundaries; they are more than a simple folder-tracking mechanism.
- VS Code commonly generates a package declaration based on the file's path.

**Spring annotations introduced**
- An annotation, written with `@`, is structured metadata attached to a Java element. It is not an ordinary comment and is not itself a method call.
- Comments are primarily for human readers; a framework/tool can inspect and process annotations.
- `@RestController` tells Spring to treat the annotated class as a REST web controller. Spring performs the framework behavior; the annotation is the declaration Spring recognizes.
- Annotations are not the only possible way to configure a framework. An explicit route-registration API could map an HTTP method and path to a handler method. Spring annotations provide a declarative alternative.
- `@GetMapping("/hello")` was introduced conceptually as a way to associate an HTTP GET path with a controller method. The learner's question about what happens when this annotation is removed remains an open teach-back point.

**Editor issue and diagnosis**
- After adding the `RestController` import/annotation to `HelloController.java`, VS Code showed unresolved import/type diagnostics.
- Learner ran `./mvnw compile`; Maven returned `BUILD SUCCESS`, demonstrating that Maven could resolve Spring's class and compile the controller source at that point.
- The likely issue was stale VS Code Java language-server/Maven project state, rather than a missing Spring dependency. A Java project reload was attempted, but it was reported as unusually slow; whether the diagnostics cleared was not confirmed.
- Do not overstate the diagnosis as certain until editor state is rechecked.

**Implementation and verification status**
- Learner created `HelloController.java` in `src/main/java/com/barista/`.
- The attempted class uses package `com.barista`, imports `org.springframework.web.bind.annotation.RestController`, and is being annotated with `@RestController`.
- Maven compilation succeeded after that code was added.
- No `@GetMapping` endpoint has yet been implemented or tested.
- Important runtime/build nuance: the executable JAR that was started was packaged before `HelloController` was added. Running `./mvnw compile` later updates compiled classes under `target/classes`, but does not rebuild the executable JAR. To test the new controller using `java -jar`, run `./mvnw package` (or `./mvnw clean package`), stop the old running process, and start the newly packaged JAR.

**Open questions / next step**
- Confirm whether VS Code finished reloading and whether the red diagnostics disappeared. Maven compilation is currently the stronger verification of source/dependency correctness.
- Complete the mental model: if `@GetMapping` is removed, how would Spring know which URL should invoke the method? Explain explicit route registration versus annotation-based mapping.
- Then add the first deliberate endpoint and rebuild/restart the JAR before testing it in the browser.
- Keep the lesson incremental: explain one annotation at a time and require the learner to predict its effect before advancing.


### 2026-11-11 — Assessment: first web-runtime concepts and next implementation target

**Additional concepts introduced**
- Spring Web MVC is the web framework that handles servlet-based HTTP request routing and response handling. MVC expands to Model-View-Controller; Barista's REST API will primarily return data (for example JSON), not server-rendered HTML views.
- Spring MVC's `DispatcherServlet` acts as a front controller: it receives web requests and coordinates mapping them to handler methods.
- Spring Boot builds on Spring Framework and, with the Spring MVC starter on the classpath, auto-configures a servlet web application and an embedded Tomcat server by default.
- Tomcat accepts HTTP connections; Spring MVC dispatches/routes requests; Spring Boot configures and starts the infrastructure. These are related but distinct responsibilities.
- The default embedded server port is 8080 and can be changed with `server.port` in `application.properties`.
- `SNAPSHOT` identifies a development version of a Maven artifact; it is not the Spring Boot framework version. Maven artifact coordinates are `groupId:artifactId:version`.
- The `groupId`/package prefix `com.barista` follows the reverse-domain naming convention. Owning a website/domain is not required for this project; the prefix is a namespace convention.
- A package's source-folder layout follows the package name beneath the source root. Spring Boot's default component scan begins at the package of the main application class and includes its subpackages, so placing controllers under `com.barista` makes them discoverable by default.
- Java import statements let source code refer to a type by its simple name instead of repeatedly writing its fully qualified name. The IDE can insert imports via autocomplete/quick fixes; memorizing every library import path is unnecessary.

**Understanding status**
- Learner now accepts that `com.barista` is a conventional Java namespace, not a requirement to publish a website or own a domain.
- Packages, Maven coordinates/artifacts, JAR files, imports, Spring MVC, DispatcherServlet, auto-configuration, embedded Tomcat, and component scanning have been explained. These are introductions, not yet independently demonstrated mastery.
- The learner correctly questioned why Spring annotations are needed and understood that annotation-based route declaration is an alternative to explicit route registration. The precise consequence of removing `@GetMapping` remains an open check.
- No HTTP controller endpoint has been implemented or verified yet.

**Current milestone assessment**
- Local project setup and Maven compilation have been verified by the learner.
- Spring Boot's embedded server was successfully started locally and responded to a browser request with HTTP 404 at `/`, confirming a reachable server with no matching application route.
- `HelloController.java` exists locally and its `@RestController` annotation compiled successfully with Maven.
- The initial executable JAR was built before `HelloController` was added; the later `mvn compile` did not rebuild that JAR.
- A check of the GitHub default branch found the learning log and architecture ADR, but did not find `pom.xml` or the Java source paths. Treat the local codebase and remote repository contents as potentially out of sync; this is not a blocker for the next local exercise, and Git workflow is handled separately.

**Next implementation milestone: first HTTP endpoint**
1. Add `@GetMapping("/hello")` to a method in `HelloController` that returns a simple string.
2. Explain what HTTP GET and route-to-method mapping mean before running it.
3. Rebuild the executable JAR (for example, `./mvnw clean package`), stop the older running process, and run the newly built JAR.
4. Request `http://localhost:8080/hello` and verify the deliberate response (expected HTTP 200 and the returned text).
5. Use the trace to reinforce Tomcat → DispatcherServlet → mapped controller method → HTTP response.

**Teaching constraint**
- Keep the next lesson to one concept and one small implementation at a time. Do not introduce database, job domain, persistence, or worker architecture until the first endpoint works and the request/response path is understood.


### 2026-11-11 — Bytecode inspection: compiled endpoint verified

**Learner action**
- After seeing FernFlower display `@GetMapping({"path"})`, learner checked the original `HelloController.java`, which contained `@GetMapping("/hello")`.
- Learner ran `javap -v -p -classpath target/classes com.barista.HelloController` from the project root.

**Verified evidence**
- `javap` inspected `target/classes/com/barista/HelloController.class`.
- The `RuntimeVisibleAnnotations` section reports `GetMapping(value=["/hello"])`.
- The method bytecode loads the constant `"Barista is alive"` and returns it (`ldc`, `areturn`).
- The class metadata identifies `HelloController.java` as the source filename.
- This proves the compiled class under `target/classes` has the intended route and response. The earlier FernFlower display showing `"path"` was stale or came from a different class/view; it did not reflect this compiled class.

**Learning demonstrated**
- Learner used the JDK's `javap` tool to inspect a compiled class and provide direct evidence about its annotation value.
- Reinforce that decompilers reconstruct readable Java-like source, while `javap -v` exposes class-file metadata and bytecode information.

**Next step**
- Since `clean package` was run, stop any older Barista process and launch the newly packaged JAR.
- Request `http://localhost:8080/hello` and verify the response body is `Barista is alive` (expected successful HTTP response).
- Do not mark the endpoint implemented-and-verified until the live request succeeds.


### 2026-11-11 — Executable JAR inspection: route and response verified

**Investigation**
- Learner listed the executable Spring Boot JAR contents with `jar tf target/barista-0.0.1-SNAPSHOT.jar | grep HelloController`.
- Confirmed the class is stored at `BOOT-INF/classes/com/barista/HelloController.class`, which explains why `javap -classpath <boot-jar> com.barista.HelloController` did not find it: the class is nested under Spring Boot's executable-JAR layout.
- Learner extracted the class with `jar xf` and inspected the extracted class using `javap -v -p`.

**Verified evidence from the packaged artifact**
- `RuntimeVisibleAnnotations` reports `GetMapping(value=["/hello"])`.
- Bytecode loads the string constant `"Barista is alive"` and returns it.
- `RestController` is also present as a runtime-visible annotation.
- This confirms the executable JAR contains the intended route and response. The FernFlower output showing `"path"` was stale or from a different class/view and is not authoritative for this artifact.

**Learning demonstrated**
- Learner successfully used `jar tf` to inspect a JAR's entries, `jar xf` to extract a nested class, and `javap -v` to inspect class-file metadata and bytecode.
- Learned that Spring Boot executable JARs place application classes under `BOOT-INF/classes` and dependencies under `BOOT-INF/lib`; they are not laid out like a plain JAR at the archive root.
- Learned to distinguish source files, compiled class-directory output, and the packaged executable JAR.

**Next step**
- Learner's terminal is currently in `target/jar-inspection`. Return to the project root using `cd ../..`.
- Ensure any old Barista process is stopped, then launch the verified JAR with `java -jar target/barista-0.0.1-SNAPSHOT.jar`.
- Test `http://localhost:8080/hello` and verify the response body is `Barista is alive`.
- The endpoint is compiled and packaged correctly, but end-to-end HTTP behavior is not yet verified.


### 2026-11-11 — Milestone: first HTTP endpoint works end-to-end

**Implementation**
- `HelloController` is annotated with `@RestController`.
- Its `hello()` method is mapped with `@GetMapping("/hello")` and returns `"Barista is alive"`.
- The executable JAR was rebuilt after the controller was added.

**Verification**
- Learner launched the rebuilt executable Spring Boot JAR.
- Learner visited `http://localhost:8080/hello` and confirmed that it works and returns the intended text.
- This verifies the complete basic request path: browser/client → embedded Tomcat → Spring MVC request mapping → controller method → HTTP response.
- This is the first end-to-end HTTP endpoint verified in Barista.

**Concepts now demonstrated through implementation**
- Spring Boot can launch the embedded web server.
- Spring MVC can route a GET request to a controller method through `@GetMapping`.
- `@RestController` registers the controller for web handling and a returned string is written as response content.
- A successful Maven package is not sufficient by itself; the packaged application must also be run and its behavior tested.

**Next step**
- Consolidate HTTP endpoint fundamentals: request method, path, status code, response body, and the distinction between route matching and method execution.
- Then introduce request mapping for a small second endpoint or a path variable, with a learner prediction before coding.
- After the learner can explain and implement a simple route independently, move toward the first real Barista use case: defining what a job is and designing the job submission API.


### 2026-11-11 — Teaching preference: interviewer-style understanding challenges

**Learner preference**
- Learner explicitly enjoys tricky, interviewer-like questions that challenge whether they genuinely understand a concept rather than merely recognize syntax or repeat an explanation.
- Incorporate regular conceptual questions, scenario-based questions, debugging hypotheses, trade-off questions, and "what would happen if..." variations throughout Barista lessons.
- Do not make every question a multiple-choice question. Prefer questions that require reasoning and explanation, with one focused question at a time.
- Let the learner attempt first; when they do not know, explain the reasoning clearly and then give a closely related follow-up to check retention.
- Keep questions fair and grounded in concepts already introduced. Increase difficulty progressively.
- The goal is independent engineering judgment, not tricking or embarrassing the learner.

**Example distinction reinforced**
- A successful Maven build verifies that configured build phases completed: compilation, configured tests, and packaging. It does not alone prove that the application can start or that an HTTP route works.
- A successful request to `/hello` verifies one runtime path through application startup, embedded Tomcat, Spring MVC route mapping, controller invocation, and HTTP response generation.
- A single successful endpoint request is a focused end-to-end integration check, not proof that the entire application is correct.

**Teaching rule going forward**
- End meaningful concept/implementation units with an interviewer-style understanding challenge.
- If the learner says they do not know, teach the answer without judgment, then check understanding with a nearby variation before advancing.


### 2026-11-11 — Teaching calibration: scaffold difficulty more carefully

**Learner feedback**
- Interviewer-style questions are motivating, but recent questions about Spring testing and application-context behavior felt too difficult and assumed senior-level reasoning.
- The learner is new to Spring and backend framework concepts; do not confuse ambition with prior expertise.

**Teaching adjustment**
- Preserve the learner's preference for challenging, interviewer-like questions, but scaffold them progressively:
  1. Basic recognition/recall.
  2. Explain a concept in their own words.
  3. Apply it to a concrete Barista scenario.
  4. Introduce a tricky edge case or interview-style twist only after the prerequisite concepts are secure.
- Ask one focused question at a time.
- When the learner says they do not know or signals overload, explain the answer plainly, reduce abstraction, and use a simpler nearby check before increasing difficulty.
- Avoid stacking several new Spring internals in one explanation. Clearly distinguish what must be understood now from what can wait.
- Maintain high standards without making the learner feel expected to perform like a senior engineer.

**Immediate clarification**
- For the previous `contextLoads()` question: changing `@GetMapping("/hello")` to another valid path such as `/wrong-path` would normally NOT make the context-load test fail. Spring can initialize successfully while registering the endpoint under the wrong path. A dedicated HTTP/API test is needed to detect that the expected `/hello` route is missing.


### 2026-11-12 — Progress reassessment and next milestone

**Verified implementation**
- The local Spring Boot 4 / Java 21 project has been generated and built with Maven.
- The executable JAR starts the application and embedded Tomcat on port 8080.
- `HelloController` exposes `GET /hello` and returns `Barista is alive`.
- Learner confirmed the endpoint works in the browser.
- Learner inspected both `target/classes/com/barista/HelloController.class` and the class extracted from `BOOT-INF/classes` in the executable JAR using `javap`; both show `GetMapping(value=["/hello"])` and the expected response string.
- This confirms one working end-to-end HTTP path, not full application correctness.

**Current conceptual status**
- Maven build/package, JAR execution, packages/imports, annotations, Spring MVC, embedded Tomcat, routes and HTTP methods have been introduced.
- Java packages/imports and the source → bytecode → JAR → JVM flow have good initial understanding.
- Spring application context and the generated `contextLoads()` test have been explained, but the learner has not yet inspected the exact generated test file or demonstrated independent understanding.
- Controller/service/repository separation has been introduced through a job-submission example, but no such layers have been implemented.
- Continue to distinguish explained concepts from independently demonstrated knowledge.

**Not implemented**
- No Job model, job lifecycle, job-submission API, database integration, worker loop, or queue coordination exists yet.
- The initial architecture ADR remains Proposed.

**Next milestone: automated test for GET /hello**
1. Inspect the actual generated test source under `src/test/java`; do not assume its contents.
2. Explain the minimum JUnit concepts needed: a test method, arranging a request, performing it, and asserting the expected status/body.
3. Add a focused automated HTTP/controller test for `GET /hello`.
4. Run it with Maven and inspect what a passing and failing assertion mean.
5. Explain the difference between the context-load test and a test that exercises the HTTP route.
6. After this is understood, introduce a small realistic Barista use case and design the Job model before adding persistence.

**Teaching approach**
- Keep explanations short and sequential.
- Continue interviewer-style challenges, but scaffold them from basic understanding to application and only then to tricky edge cases.
- Ask one question at a time; when the learner is stuck, teach the concept plainly and follow with a nearby, easier check.

**Repository synchronization note**
- The latest check of the GitHub default branch found the learning log and architecture ADR, but not `pom.xml` or the local Java source paths. The locally working project is the current implementation state; remote source synchronization is separate from this learning milestone and Git instruction remains outside this track.


### 2026-11-12 — Teaching agreement: patient, simple, analogy-led mastery

**Learner request**
- Learner studies this material with ChatGPT as their primary learning resource and wants to master Java and Spring.
- Explicitly requests patience, simple explanations, and analogies throughout the learning journey.

**Teaching commitments**
- Explain every new concept from first principles; do not assume familiarity with framework vocabulary.
- Prefer plain language and concrete analogies, then connect the analogy back to the exact Java/Spring mechanism.
- Introduce one main concept at a time. Define new terms before using them.
- Explain code line by line when it is introduced, including why it exists and what would happen if it were removed or changed.
- Use small diagrams and runnable Barista examples when useful.
- Distinguish what the learner must understand now from details that can wait.
- Be patient when the learner asks the same question in a different way; treat that as evidence the explanation needs adjustment, not as a problem with the learner.
- Maintain rigor and active learning: ask one question at a time, let the learner attempt, give hints before answers when appropriate, and use progressively harder interviewer-style challenges only after the foundation is clear.
- When the learner says "I don't know" or feels overwhelmed, explain the answer calmly and check understanding with a simpler adjacent question before advancing.
- Do not rush toward advanced Spring, architecture, or distributed-systems topics merely to cover more material. The goal is durable understanding and independent engineering skill.

**Immediate learning state**
- The current topic is Spring Boot's test setup and the generated `contextLoads()` test.
- The learner has been taught that `@SpringBootTest` asks Spring's test infrastructure to load the application context, and that initialization failures (such as an unsatisfied required dependency) can fail the test before the empty test method runs.
- Continue with a small understanding check, then inspect or create a real HTTP endpoint test. Avoid adding more testing internals until the learner understands this flow.


### 2026-11-12 — Long-term goal: become a thriving software engineer in the AI era

**Learner's goal**
- Barista is not only a portfolio project or a way to learn Spring. The learner wants the overall journey to prepare them to thrive as a software engineer in a rapidly changing, AI-enabled industry.
- The target is durable engineering capability, adaptability, sound judgment, and the ability to deliver useful software—not merely collecting framework knowledge or producing code with AI.

**Engineering capabilities to cultivate alongside Barista**
1. **Strong fundamentals:** Java and the JVM, data structures and algorithms, HTTP and networking, databases and SQL, operating systems, concurrency, and core software design. Learn the underlying mechanisms rather than memorizing framework recipes.
2. **End-to-end delivery:** turn a problem into a working, tested, documented, observable, secure, deployable feature. Understand the entire path from user/API contract through application logic, persistence, runtime, and operations.
3. **Engineering judgment:** clarify requirements, identify constraints, compare options, reason about trade-offs, avoid unnecessary complexity, and explain why a design is appropriate.
4. **Reliability and systems thinking:** understand failure modes, concurrency, data consistency, performance, security, observability, recovery, and operational cost. Use Barista's queue semantics as a practical laboratory for these skills.
5. **Code quality and maintainability:** write readable code, modularize around real responsibilities, test behavior, review changes, refactor safely, and document important decisions.
6. **AI fluency without dependency:** use AI as an accelerator for exploration, explanations, test ideas, code review, debugging hypotheses, and repetitive work. Verify outputs, inspect generated code, consult primary documentation when needed, and retain the ability to reason, implement, debug, and explain independently. Never treat generated code or confident explanations as proof of correctness.
7. **Product and communication skills:** understand the user problem, define useful scope, communicate clearly, write concise technical documentation, discuss trade-offs, and collaborate through review and feedback.
8. **Adaptability:** build transferable mental models and learn new tools/frameworks deliberately. Do not chase every trend; evaluate technologies by the problems they solve and evidence from real requirements.

**Working method**
- Every meaningful Barista feature should ideally produce both a software outcome and a learning outcome.
- For each feature, practice: understand the requirement → identify constraints → design → implement → test → inspect failure cases → explain the decisions → document the result.
- Use AI to scaffold learning, not replace the learner's thinking. The learner should attempt, predict, or state a hypothesis before receiving a complete solution when appropriate.
- Gradually reduce hints as competence grows.
- Maintain a living skills picture: what is understood, what has been applied, what has been independently demonstrated, and what remains weak.
- Prefer a few deeply understood, completed features over many impressive-looking but fragile features.
- Keep the project focused on a credible, reliable PostgreSQL-backed job queue before adding advanced infrastructure or trendy technologies.

**How progress will be assessed**
- Can the learner explain the problem and the design without relying on memorized wording?
- Can they implement a small variation without copying the previous example?
- Can they write tests that verify observable behavior?
- Can they debug using evidence and distinguish symptoms from causes?
- Can they explain trade-offs and identify what the system does NOT guarantee?
- Can they use AI-generated suggestions critically and verify them independently?
- Can they take a feature from requirement through implementation, tests, documentation, and operation?

**Teaching commitment**
- Coach for long-term engineering capability, not just completion of Barista.
- Connect project lessons to transferable SWE principles and current AI-assisted engineering practices when relevant, without turning every lesson into a career lecture.
- Be honest about skill gaps and the limits of any assessment. Celebrate verified progress, not activity alone.
- Revisit this development plan periodically and adjust it based on demonstrated work, changing industry needs, and the learner's interests.

**Immediate focus**
- Finish the first automated HTTP test for `GET /hello` using a clear, beginner-friendly explanation.
- Then begin Barista's first real domain feature: define a job, its data, and its valid lifecycle before building the job-submission API.


### 2026-11-12 — Milestone: first automated HTTP endpoint test passes

**Implementation**
- Added `@AutoConfigureMockMvc` alongside `@SpringBootTest`.
- Injected a configured `MockMvc` instance with `@Autowired`.
- Added a JUnit test that performs `GET /hello` and asserts HTTP 200 plus the exact response body `Barista is alive`.
- Added the necessary static imports for MockMvc's `get`, `status`, and `content` request/matcher helpers.

**Debugging and verification**
- Initial test run failed because the final chained assertion statement was missing a semicolon.
- Learner corrected the syntax and ran `./mvnw clean test`.
- Learner reported that the tests now pass.
- The two test methods are the context-load smoke test and the endpoint response test.
- `clean test` compiles application and test code and runs tests; it creates compiled classes and Surefire reports under `target/`, but does not run the package phase to create a new executable JAR.
- `clean package` proceeds through the test phase and then packages the executable JAR.

**Concepts introduced / reinforced**
- MockMvc simulates HTTP requests through Spring MVC without a real network connection or starting Tomcat.
- JUnit `@Test` marks a test method for execution.
- Assertions express expected behavior; a failing assertion means observed behavior differs from the expectation.
- Static imports allow static methods such as `get()`, `status()`, and `content()` to be called without qualifying them with their declaring class.
- Distinguish test failures (an assertion is false) from test errors (unexpected execution/setup problems).
- A successful build/test run only proves the checks that were actually configured and executed.

**Learning demonstrated**
- Learner recognized the conceptual similarity between MockMvc assertions and Node.js `assert.equal`.
- Learner fixed a missing statement-terminating semicolon after reading the compiler error.
- Learner successfully ran the test suite after the correction.
- Do not yet claim independent mastery of JUnit, MockMvc, static imports, or the Maven lifecycle; revisit through additional tests.

**Next milestone**
- Begin Barista's first real domain feature: define what a `Job` is and what information the system must store about it.
- Start with the problem and a concrete example, not code or database schema.
- Distinguish job data from job behavior/state transitions.
- Ask the learner to propose the minimum information Barista needs to accept, identify, execute, and track a job.
- Only after agreeing on the domain model, implement a simple Java `Job` representation and tests. Introduce persistence later, when the in-memory model is understood.

**Teaching approach**
- Continue simple, patient, analogy-led explanations with one central concept per lesson.
- Progressively use interviewer-style challenges, but scaffold them from basic understanding to applied reasoning and then harder edge cases.


### 2026-11-12 — Proposed job identifier strategy: UUIDv4

**Learner preference and recommendation**
- Learner prefers UUID-style identifiers for Barista jobs.
- Recommended starting choice: UUIDv4 generated in Java 21 with `java.util.UUID.randomUUID()`, stored as PostgreSQL's native `uuid` type and constrained as the primary key.
- This is a proposed design choice for the first implementation; revisit it if requirements or measured database performance justify UUIDv7 or another identifier strategy.

**Rationale**
- Multiple API instances can generate IDs independently without coordinating through a central sequence allocator.
- Java 21's standard `UUID.randomUUID()` produces a type-4 pseudorandom UUID and requires no extra dependency.
- PostgreSQL supports a native UUID column type and a primary-key uniqueness constraint.
- UUIDv4 uses 128 bits (16 bytes), so it is larger than a BIGINT (8 bytes); random key insertion may have less favorable B-tree index locality than sequential or time-ordered identifiers.
- UUIDv7 is time-ordered and may offer better index locality, but its timestamp component reveals approximate generation time. Java 21's standard UUID API does not provide a UUIDv7 factory, so it would require another implementation/library or database-side generation.
- Avoid premature optimization: start with UUIDv4 and revisit UUIDv7 only with a concrete requirement or benchmark.

**Intended data model**
- Use one canonical job identifier, e.g. a Java `UUID` and PostgreSQL `uuid` primary key. Do not add a second numeric internal ID unless a real requirement emerges.
- Related rows such as `job_events` should reference the job using a UUID foreign key (e.g. `job_id`).
- UUID uniqueness is not the same as idempotency. Two identical submissions should normally create two distinct jobs unless a separate idempotency mechanism/key is deliberately implemented later.
- UUIDs are not an authorization mechanism; API access to a job must still be authorized.

**Understanding status**
- Learner discussed sequential database IDs versus UUIDs, central sequence allocation, and the relevance of prior Redis Lua atomicity experience.
- UUIDv4 vs UUIDv7 trade-offs have now been introduced. Do not mark the choice or concepts as independently demonstrated until reinforced during Job implementation.

**Next step**
- Begin the actual `Job` domain model. First decide the minimum job data and lifecycle needed by Barista; then implement a simple Java representation using a UUID identifier. Persistence and PostgreSQL schema come later.


### 2026-11-12 — Clarification: API replicas, worker processes, and UUID version choice

**API instances**
- One API instance means one running copy/process of the Barista API application.
- A single Spring Boot/Tomcat process can handle multiple concurrent requests using its request-handling thread pool and can use multiple CPU cores; it is not limited to one request at a time.
- More API instances are an optional horizontal-scaling/redundancy strategy when one instance or one machine becomes a bottleneck or availability target requires replicas.
- Running multiple instances on the same physical machine does not add hardware capacity; they share CPU, memory, network and other resources. Replicas are most useful across machines/containers, although multiple processes on one host may sometimes serve isolation or deployment purposes.
- Do not assume Barista needs multiple API instances initially. Start with one API instance.
- Keep API instances distinct from worker processes: API instances handle incoming HTTP requests; worker processes claim and execute background jobs. Barista may run one API instance and one or more worker processes independently.

**UUID version clarification**
- No UUID implementation has been added to the code yet.
- The current recorded proposal is UUIDv4, generated with Java 21's `UUID.randomUUID()`; this is not UUIDv7.
- Java 21's standard `java.util.UUID` API documents `randomUUID()` as generating a type-4 UUID and does not provide a built-in UUIDv7 factory.
- UUIDv7 contains a Unix-millisecond timestamp in its most significant bits and random/optional monotonicity data in the remaining bits, making values approximately time-ordered according to RFC 9562. It may improve insertion locality for B-tree indexes relative to random UUIDv4, depending on the workload and implementation.
- UUIDv7 also exposes approximate generation time and requires an additional library or custom, carefully tested implementation when targeting Java 21.
- Do not select UUIDv7 solely because Barista could someday have multiple API instances. Choose based on the job identifier's database/index usage, operational requirements, privacy considerations, and dependency cost.
- Current provisional recommendation remains UUIDv4 for the initial learning implementation because Java 21 supports it directly and no measured index-performance requirement exists. Revisit the choice when designing the schema; changing generation strategy later does not require changing PostgreSQL's `uuid` column type.

**Teaching clarification**
- When explaining horizontal scaling, distinguish adding concurrent threads/resources to one process, running additional processes on the same host, and distributing replicas across multiple machines.
- Do not describe UUIDs as necessary for multiple API instances or imply multiple instances are required for concurrency.


### 2026-11-12 — Next product slice: public job-submission contract

**Design insight**
- Learner identified that Barista needs a language-independent, standardized public API contract so applications written in different languages can submit and inspect jobs without knowing Barista's Java/Spring internals.
- Keep the distinction clear between the public HTTP/JSON contract and internal Java domain classes/interfaces.
- Public contract should specify method, path, request schema, server-owned fields, success status/response, validation, and error response behavior. OpenAPI can document this contract later.

**Proposed first use case (not yet implemented or formally accepted)**
- Operation: submit a job.
- HTTP method and path: `POST /api/v1/jobs`.
- Request contains client-provided job data such as `type` and `payload`.
- Barista generates the job identifier (provisionally UUIDv4) and assigns the initial status `PENDING`; clients should not choose these server-managed fields.
- A successful asynchronous acceptance can return HTTP `202 Accepted` with the generated job ID, type, and status. Explain that 202 means accepted for processing, not completed.
- Payload is JSON and may differ by job type; validate it according to the selected type. Keep the initial contract minimal and evolve it deliberately.
- Do not expose internal persistence/entity representations as the public contract.

**Implementation path**
1. Agree on the single submit-job contract and distinguish client-owned from server-owned fields.
2. Design the minimal Job domain model and lifecycle.
3. Implement and unit-test domain rules.
4. Add application use case/service and HTTP DTO/controller mapping.
5. Add PostgreSQL persistence before claiming durable acceptance; then write HTTP integration tests.
6. Add worker execution only after the submit/read/persist flow is understood and verified.

**Teaching note**
- Do not start by designing dozens of endpoints. Build and verify one vertical slice first.
- Explain one concept at a time, with a concrete request/response example.


### 2026-11-12 — Public API ownership decision: server-managed job identity and initial state

**Learner decision**
- For the proposed `POST /api/v1/jobs` contract, the learner correctly identified that Barista, not the client, should generate the job ID and assign the initial status.
- The client supplies the requested work (job type and payload); Barista owns system-managed identity and lifecycle state.
- This prevents clients from choosing duplicate/forged identifiers or claiming arbitrary lifecycle states such as SUCCEEDED before execution.

**Contract principle**
- Client-owned input: job type and payload (plus explicitly supported options later).
- Server-owned fields: job ID, initial status, creation timestamps, attempt counters, and execution metadata.
- On successful asynchronous acceptance, the proposed response uses HTTP 202 Accepted. This confirms that Barista accepted the request for processing, not that the job has completed.
- UUIDv4 remains a proposed initial identifier strategy; no UUID code or database schema has been implemented yet.

**Next step**
- Continue designing the minimal public request/response schemas and minimal Job domain model before writing code.


### 2026-11-12 — Future reliability design: resumable jobs and worker crash recovery

**Design direction recorded for a later stage (not implemented)**

- Initial execution policy: when a worker crashes during a job, Barista will recover and make the job eligible for another attempt. The next attempt starts the handler from the beginning.
- Future extension: support resumable handlers through application-owned checkpoints. A handler that can safely resume must periodically persist enough progress in durable storage for a later worker to continue from a known checkpoint.
- Barista's generic queue should provide the execution/recovery mechanism, but it cannot infer what "60% complete" or "continue at item 61" means for every job type. The handler understands the task's semantics and must define how progress is saved and restored.
- Keep two concepts separate:
  - `attempts`: how many execution attempts have started; used with a retry limit to decide whether another attempt is allowed.
  - **Checkpoint/progress state**: durable task-specific information that a handler can use to resume work.
- Do not promise exactly-once effects or automatic continuation from the crash point. Retrying from the beginning can repeat side effects, so handlers must eventually be designed for idempotency or use checkpoints/other coordination where appropriate.
- This is a future capability, not part of the first Job model or initial implementation. Revisit after basic job execution, persistence, retries, and crash recovery are understood and working.

**Concepts introduced**
- A process crash normally destroys its in-memory execution state: local variables, stack, and unsaved progress are not available to a replacement worker.
- A crash can occur because of an unhandled exception, process termination/restart, machine or container failure, operating-system resource pressure (such as out-of-memory termination), power/network/host failure, or a forced deployment/shutdown.
- Distinguish a job failure from a worker/process crash: a handler can fail normally and report an error while the worker remains alive; a process crash can prevent Barista from receiving a final success/failure report at all.
- Barista will need a way to detect work that was claimed but not completed (later, leases/heartbeats and recovery). Do not design those mechanisms before the basic lifecycle is understood.

**Understanding status**
- The difference between an attempt counter and a checkpoint has been introduced.
- The learner asked whether a worker should continue from the point of a crash. Explained restart-from-beginning as the initial strategy and handler-owned durable checkpoints as a later extension.
- No independent teach-back, implementation, or verification yet.

**Next learning step**
- Understand common causes and observable effects of worker failure.
- Then continue the Job model, including the attempt counter and retry limit, without adding checkpoint fields to the first implementation.


### 2026-11-12 — First Job domain skeleton compiles

**Implemented locally (learner-reported; not yet pushed to the repository)**
- Added `com.barista.job.JobStatus` enum with `PENDING`, `RUNNING`, `RETRYING`, `SUCCEEDED`, and `DEAD`.
- Added `com.barista.job.Job` with:
  - `UUID id`, generated in the constructor with `UUID.randomUUID()`.
  - `JobStatus status`, initialized to `PENDING`.
  - `String type`, supplied through the constructor.
- Learner ran `./mvnw compile` and reported that the build succeeds.

**Scope and limitations**
- This is only the initial domain skeleton. It does not yet include payload, attempt counters, retry limits, timestamps, validation, lifecycle methods, or tests.
- Compilation verifies syntax and type consistency for the compiled sources, but does not yet verify domain behavior.
- UUIDv4 remains the proposed identifier strategy.

**Learning status**
- Enum syntax was newly introduced and the learner implemented the five status values.
- Learner implemented the initial Job fields and constructor, with successful compilation.
- Getters/setters and ordinary Java class syntax are already familiar; avoid reteaching them unless a concrete issue arises.

**Next step**
- Design the representation of type-specific JSON job payloads. Start by explaining the generic key/value structure and why the queue cannot assume every job type has identical fields.
- Then extend the Job model in a small step and test behavior before adding lifecycle complexity.


### 2026-11-13 — First Job lifecycle unit test passes

**Implemented locally (learner-reported; application source/test changes have not yet been pushed to the repository)**
- `Job` now has a `start()` operation that:
  - Requires the current status to be `PENDING`.
  - Rejects starts when `attempts >= maxAttempts`.
  - Increments `attempts` only after validation succeeds.
  - Changes the status to `RUNNING`.
- The learner added read accessors for status and attempt count so behavior can be observed in tests.
- Added `JobTest` under `src/test/java/com/barista/job/JobTest.java`.
- The test creates a job with an empty payload, calls `start()`, and asserts `RUNNING` plus one attempt.

**Verification**
- Learner ran `./mvnw clean test`.
- Maven compiled application and test sources and executed 3 tests total: 2 in `BaristaApplicationTests` and 1 in `JobTest`.
- Reported result: 3 tests, 0 failures, 0 errors, 0 skipped; `BUILD SUCCESS`.
- A subsequent `./mvnw compile` also reported `BUILD SUCCESS`.
- VS Code displayed unresolved-symbol underlines despite Maven successfully compiling and executing the test. This points to an IDE Java language-server / Maven project synchronization issue, not a source compilation failure. Suggested reloading Maven/Java projects and cleaning the Java language-server workspace.
- Mockito/JDK dynamic-agent warnings appeared during tests; they did not fail the build and are not the cause of the unresolved-symbol underlines.

**Learning status**
- Learner implemented the first state transition and a happy-path unit test.
- The happy path is verified. Invalid transition and retry-limit behavior still need tests.
- Do not claim the full Job lifecycle is complete or the model is fully verified yet.

**Next step**
- Resolve or confirm the VS Code language-server underlines after project reload.
- Add tests for invalid start states and exhausted attempt limits, verifying that rejected operations leave the object's state unchanged.


### 2026-11-13 — Proposed future design: dependency-aware circuit breaker and recovery probes

**Learner's proposal**
- When a job fails because a shared external service is unavailable, stop repeatedly attempting every dependent job.
- Keep dependent jobs waiting and use a separate lightweight mechanism to check whether the external service has recovered.
- Once the dependency responds successfully, resume eligible jobs.

**Engineering assessment**
- This is a strong direction and corresponds to a combination of the **Circuit Breaker pattern**, dependency health probing, and queue-level deferral/backpressure.
- Prefer one coordinated breaker/probe per dependency (e.g. the email provider), not one disposable probe job per failed business job. Otherwise many jobs can create duplicate probes and amplify load.
- Circuit breaker states:
  - **CLOSED:** ordinary requests are allowed; failures are observed.
  - **OPEN:** ordinary requests are rejected/deferred without contacting the unhealthy dependency; wait for a cooldown.
  - **HALF_OPEN:** allow only a small, bounded number of test requests. Close on successful recovery; reopen on failure.
- Do not open the breaker because of every job failure. Classify failures: connection failures, timeouts, and suitable service-unavailable responses may indicate dependency health; invalid payloads, authentication/configuration errors, and job-specific failures generally do not prove a shared outage. A configurable failure threshold/window helps avoid reacting to one isolated transient failure.
- A health probe must test a meaningful capability and be safe/lightweight. A successful TCP ping or generic health endpoint does not necessarily prove the actual operation (e.g. sending email) is ready.
- When the breaker opens, jobs that depend on that service should be deferred rather than repeatedly claimed and failed. Job-level state and dependency-level availability are distinct concerns; the precise representation (e.g. RETRYING/availableAt versus a dependency gate or a future BLOCKED state) remains undecided.
- When the dependency recovers, release work gradually with concurrency/rate limits or jitter. Releasing a large backlog all at once can create a thundering herd and immediately overload the recovering service.
- Per-job retry/backoff and dependency-level circuit breaking complement one another; the breaker does not replace retries for isolated transient errors.
- In a multi-process/multi-worker Barista deployment, a purely in-memory breaker is not shared. A future implementation must coordinate breaker state and ensure only a bounded number of half-open probes across workers/instances (e.g. shared state or an ownership/lease mechanism). Do not solve this in the initial Job domain model.
- A timeout can occur after the external service performed the side effect but before Barista received the response. Retrying may duplicate that side effect; idempotency/deduplication remains a separate requirement.

**Status**
- Proposed architecture only. No circuit breaker, health-probe job, dependency registry, blocked-job state, or retry scheduling has been implemented or formally accepted.
- Current Barista implementation remains focused on the basic Job lifecycle and its initial tests.

**Next design revisit**
- Revisit this idea during the reliability/resilience stage, after basic retries, worker execution, and database-backed claiming exist.
- Decide how handlers declare dependencies, how failure categories are classified, how breaker state is shared, how jobs are deferred/released, and how recovery traffic is rate-limited.


### 2026-11-13 — Recommended retry scheduling design: database-backed eligibility

**Recommendation for the initial PostgreSQL-backed queue (design only; not implemented)**
- Store a durable `available_at` timestamp on each job. It is a **not-before** time: a worker must not execute the job before this instant.
- When a running attempt fails:
  - If attempts remain, set status to `RETRYING` and compute `available_at = failure_time + retry_delay`.
  - If the attempt limit is exhausted, set status to `DEAD`.
- Keep a retrying job in `RETRYING` while it waits. Once `available_at` has arrived, it becomes eligible for a worker to claim. The claim can transition it directly from `RETRYING` to `RUNNING` and begin the next attempt.
- Likewise, an immediately runnable `PENDING` job is eligible when its `available_at` has arrived.
- **Avoid a separate scheduler process solely to promote due jobs from `RETRYING` to `PENDING`** in the first implementation. Workers can poll PostgreSQL for eligible jobs and atomically claim them. This removes an unnecessary moving part.
- The eventual claim operation must use a database transaction and concurrency-safe row locking (planned: PostgreSQL `SELECT ... FOR UPDATE SKIP LOCKED`) so multiple workers cannot claim the same job. The SQL sketch discussed in the lesson is illustrative, not production SQL.
- Use database time for the eligibility comparison where practical, avoiding dependence on potentially skewed worker clocks.
- Retry policy (including exponential backoff, a maximum delay, jitter, and optional server-provided `Retry-After`) computes the next `available_at`. The first implementation should begin with a simple bounded policy and evolve deliberately.
- `available_at` is a not-before constraint, not an exact execution guarantee: polling interval, queue load, and worker capacity can make actual execution later.
- The proposed dependency-level circuit breaker is a separate mechanism. When a dependency breaker is OPEN, dependent work should be deferred without repeatedly contacting the dependency; a probe/half-open policy controls recovery. A job that is deferred without actually invoking its handler should not consume a normal execution attempt. The exact integration with claiming and dependency state remains a future design task.
- On recovery, release work gradually using concurrency/rate controls and jitter to avoid a thundering herd.

**Important design correction**
- Earlier lessons described a separate scheduler moving `RETRYING` jobs back to `PENDING`. The recommended initial design does not require that extra transition or process: a due `RETRYING` job can be claimed directly by a worker and transition to `RUNNING`.
- Retain `RETRYING` as an observable state meaning that an attempt failed and the job is waiting for another attempt.

**Status**
- Recommended architecture, not yet formally accepted or implemented.
- The current local `Job` class has only a preliminary `availableAt` field proposal; do not claim database scheduling, polling, locking, backoff, or circuit-breaking code exists.
- Revisit exact status transitions, timestamp ownership, database schema/indexes, polling cadence, and atomic claim implementation during persistence/worker design.
