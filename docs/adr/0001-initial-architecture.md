# ADR 0001: Initial Barista application architecture

- **Status:** Proposed
- **Date:** 2026-11-09
- **Decision owners:** Barista project team

## Context

Barista is a learning project and a self-hosted job queue/workflow engine. The initial implementation must be understandable, testable, and reliable before it becomes sophisticated.

The system needs an HTTP API for clients and worker processes that can execute jobs concurrently. Workers must be independently runnable and horizontally scalable. PostgreSQL is the planned initial source of truth for jobs, leases, and execution history.

Starting with many independently deployed services, a message broker, separate read databases, or elaborate architectural layers would introduce infrastructure and distributed-systems complexity before the core queue is understood and verified.

## Proposed decision

### 1. Codebase and runtime shape

- Start with one Git repository and one Maven project.
- Organize code into cohesive feature modules/packages, initially around jobs and workers. Add scheduling and workflows only when those features are implemented.
- Keep API and worker as distinct runtime roles that can run in separate processes.
- Share the domain and application logic between those roles where appropriate.
- Run multiple worker processes against the same PostgreSQL-backed queue.
- Separate processes do not automatically make components microservices. We are not initially creating independently owned services with separate data stores and network contracts.

### 2. Internal organization

Use a pragmatic layered structure with explicit dependency boundaries:

- **Inbound adapters:** HTTP controllers and, later, other entry points.
- **Application layer:** use cases such as submit, cancel, claim, complete, and retry.
- **Domain layer:** job state, transitions, and business invariants.
- **Outbound ports:** interfaces describing capabilities the application needs, such as job persistence.
- **Infrastructure adapters:** PostgreSQL implementations of persistence ports and integrations with external systems.

The dependency direction should point inward: domain logic must not depend on Spring MVC, PostgreSQL, JPA entities, or HTTP request classes.

Do not create an interface or abstraction for every class. Introduce boundaries where they protect an important rule, isolate infrastructure, or enable a meaningful test.

### 3. Persistence and queue coordination

- Use PostgreSQL as the initial durable source of truth.
- Implement job claiming with explicit, tested transaction semantics and row locking (including SELECT FOR UPDATE SKIP LOCKED where appropriate).
- Keep an append-only job event history for auditability.
- Treat the event history as an audit log initially, not as proof that the whole system is event-driven or event-sourced.
- Do not introduce Redis, Kafka, or RabbitMQ until measured requirements justify another component.

### 4. Communication

- Clients communicate with the API over HTTP.
- Worker processes coordinate through PostgreSQL in the initial design.
- Use direct in-process calls between modules inside a process.
- Introduce published events or a transactional outbox only when a concrete consumer or reliable event-delivery requirement justifies them.

## Why this approach

- **Learning value:** exposes the important concepts—module boundaries, persistence, transactions, concurrency, and failure recovery—without multiplying infrastructure too early.
- **Operational simplicity:** one codebase and one primary datastore are easier to run, test, and debug.
- **Worker scalability:** worker processes can still be replicated independently to increase execution capacity.
- **Changeability:** inward dependency boundaries make it possible to test business rules without a real database and to replace infrastructure implementations deliberately.
- **Evidence-led evolution:** architecture can become more sophisticated when benchmarks, failure experiments, or real requirements reveal a need.

## Alternatives considered

### Traditional unstructured monolith

Rejected as a target structure because unrelated responsibilities can become coupled in large packages or classes. We still keep the simplicity of one application codebase, but enforce meaningful module boundaries.

### Microservices from the beginning

Deferred. They would add network failure modes, service-to-service authentication, independent deployments, observability across services, and distributed data-consistency problems before Barista's core execution model is proven.

### Pure horizontal layered architecture only

Not enough by itself. Large global controllers, services, and repositories packages can scatter one feature across the codebase. We prefer feature-oriented modules, with a small layered structure inside each module.

### Full Hexagonal / Clean Architecture everywhere

We adopt the important dependency rule and ports/adapters at infrastructure boundaries, but defer a rigid, highly abstract implementation. Excessive interfaces and pass-through layers would slow learning and add ceremony without demonstrated value.

### Event-driven architecture for all internal coordination

Deferred. The initial queue's core coordination is durable PostgreSQL state plus atomic claims. A job_events audit table is not itself an event bus. Events may later be useful for workflow reactions, notifications, and dashboard updates.

### Full CQRS with separate read/write models or databases

Deferred. Initial reads and writes can use the same PostgreSQL schema. We can separate query handlers in code where useful, then consider specialized read models only if actual query workloads require them.

### External message broker immediately

Deferred. PostgreSQL is sufficient to establish the initial durable queue and provides transactional coordination with job state. A broker can be evaluated later against measured throughput, latency, fan-out, and operational requirements.

## Consequences and trade-offs

- The API and workers share a codebase and may share domain/application modules; changes to shared code require coordinated builds and releases.
- PostgreSQL becomes a central dependency and potential bottleneck. We must measure and tune it rather than assuming it scales indefinitely.
- Workers poll or claim from PostgreSQL, so polling frequency, indexes, transaction duration, and connection-pool usage matter.
- Separate runtime roles require clear configuration and startup behavior.
- The architecture does not promise exactly-once side effects. At-least-once execution means handlers must tolerate retries and possible repeated execution.

## Revisit this decision when

- API and worker code need genuinely independent release cycles or ownership.
- Different components require substantially different scaling or isolation characteristics that cannot be handled by separate process roles.
- PostgreSQL queue contention or throughput becomes a measured bottleneck.
- External integrations require durable event publication or substantial fan-out.
- Read workloads require specialized projections or separate read models.

Any proposed change should be supported by a concrete requirement, benchmark, operational observation, or failure experiment.
