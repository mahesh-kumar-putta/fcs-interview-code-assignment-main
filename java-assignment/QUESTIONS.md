# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
```txt
I would refactor the persistence layer only where the inconsistency creates confusion or maintenance cost, not for the sake of uniformity alone.

The main issue I see is that the codebase mixes different persistence styles: some access is entity- and repository-driven, while others look more service-like or adapter-specific. That is acceptable in a small monolith, but it becomes harder to reason about when the same domain concept is handled through multiple patterns.

If I were maintaining this codebase, I would first standardize the boundaries: keep domain logic in use cases and ports, and make repository/database adapters consistent in naming, transaction handling, and return semantics. I would also reduce duplicate mapping logic between domain models and persistence entities, especially when the same transformation is done in multiple places.

The refactor I would prioritize is not a full rewrite, but a consolidation around one clear pattern: domain model -> persistence adapter -> repository, with explicit transaction boundaries and single responsibility. In a mature codebase, this improves testability, reduces accidental drift, and makes future changes much safer.
```
----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
```txt
The OpenAPI-generated approach has a strong advantage for the Warehouse API: it makes the contract explicit, keeps server and client expectations aligned, and reduces the risk of drift between documentation and implementation. When the contract is the source of truth, code generation can speed up development and make changes more predictable.

The downside is that generated code can sometimes become rigid or harder to customize when business rules or edge cases require custom handling. It also can be noisy if the API grows quickly or if the generated objects do not fit the application’s preferred patterns.

For Product and Store endpoints, coding the handlers directly is simpler and more flexible for a small codebase or where the API is not yet fully standardized. It gives more control over custom validation, domain-specific logic, and exceptions. The trade-off is that the contract can drift from the implementation unless the API documentation is maintained carefully.

My choice would be a hybrid approach: use OpenAPI generation for public or shared APIs where contract stability matters, and keep direct implementation only when the endpoint is small, highly custom, or still evolving. That gives the team the consistency of generated contracts without losing the flexibility to handle edge cases cleanly.
```
----
3. Given the need to balance thorough testing with time and resource constraints, how would you prioritize and implement tests for this project? Which types of tests would you focus on, and how would you ensure test coverage remains effective over time?

**Answer:**
```txt
I would prioritize tests by risk and business criticality, not by breadth alone. The highest-value tests in this project are the ones that validate domain rules and API contracts: warehouse creation validation, archive behavior, replacement constraints, and invalid input handling. These are the rules most likely to break business correctness and are therefore the best use of limited test time.

In practice, I would focus on three layers:

1. Unit tests for domain use cases: These are fast, deterministic, and ideal for validating business rules such as duplicate business unit codes, stock/capacity constraints, and archive/replace logic.
2. Integration tests for HTTP endpoints: These confirm that real HTTP requests produce the expected status codes and payloads, especially for validation failures and not-found behavior.
3. A smaller set of end-to-end checks for the most important flows: create, read, archive, and replace warehouse operations with a real database behind them.

To keep coverage effective over time, I would treat tests as a safety net around business rules, not as a backlog item to add after the fact. I would also keep them focused on behavior rather than implementation details, use meaningful test names, and keep fast unit tests as the default while reserving slower integration tests for the critical paths.

This gives high confidence without over-investing in expensive, brittle tests that do not guard the actual business risk.
```

----
4. How would you implement the bonus fulfilment assignment feature and protect its business rules?

**Answer:**
```txt
I would model a fulfilment assignment as a relationship between a Store, a Warehouse, and a Product, then validate the relationship before persisting it. The service should reject an assignment when the same Product would use more than two different Warehouses for one Store, when a Store would use more than three different Warehouses, or when a Warehouse would hold more than five different Product types.

The limits must count distinct relationships, not duplicate requests. For example, assigning the same product to the same warehouse twice should not consume another product or warehouse slot, while assigning a new warehouse for that product and store should count. These rules should be enforced in the domain service and backed by database constraints or transactional locking when multiple requests can be processed concurrently.

The main use cases are assigning a product to a warehouse for a store, rejecting assignments that exceed a limit, and allowing valid assignments at the boundary values. I would add unit tests for each limit, duplicate assignments, boundary conditions, and combinations where one assignment affects more than one rule. At the API boundary I would return a clear client error for a rejected business rule and keep the operation atomic so a failed assignment cannot leave partial state behind.
```