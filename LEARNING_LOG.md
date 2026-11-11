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
