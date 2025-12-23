# CustomerAggregate

This aggregate models the write-side behavior for `Customer` using Axon Framework. It processes commands, applies events, and maintains the aggregate state using event sourcing.

## Why the constructor is used only for CreateCustomerCommand

- Axon constructs a new aggregate instance when handling a command that creates it (e.g., `CreateCustomerCommand`).
- The aggregate constructor annotated with `@CommandHandler` should be used only for the creation command. This is the canonical place to enforce invariants before the aggregate exists and to apply the creation event (e.g., `CustomerCreatedEvent`).
- For all other commands that modify an existing aggregate, use regular instance methods annotated with `@CommandHandler` (not constructors). Those methods operate on already reconstructed state.

## Structure in the current class

- `@Aggregate`: Marks this class as an Axon Aggregate.
- `@AggregateIdentifier`: The stable unique identifier (`customerId`) used by Axon to route commands and tie together the event stream for a specific aggregate instance.
- `@CommandHandler` (constructor): Handles `CreateCustomerCommand`.
  - Validates business rules (e.g., checks for existing active customer by mobile number).
  - Applies `CustomerCreatedEvent` via `AggregateLifecycle.apply(...)` when valid.
- `@EventSourcingHandler`: Updates in-memory aggregate state when `CustomerCreatedEvent` is replayed or newly applied.

## Recommended pattern for other commands

For any command that updates an existing customer (e.g., `UpdateCustomerCommand`, `DeactivateCustomerCommand`):

1. Add an instance method (not a constructor) annotated with `@CommandHandler`.
2. In the method, validate business invariants using the current state (`this.*`).
3. Apply the appropriate domain event using `AggregateLifecycle.apply(event)`.
4. Add corresponding `@EventSourcingHandler` methods to mutate the aggregate state based on those events.

Example sketch:

```java
@CommandHandler
public void handle(UpdateCustomerCommand cmd) {
    // validate invariants with current state
    if (!this.activeSw) {
        throw new IllegalStateException("Cannot update inactive customer");
    }
    CustomerUpdatedEvent event = new CustomerUpdatedEvent(cmd.getCustomerId(), cmd.getName(), cmd.getEmail());
    AggregateLifecycle.apply(event);
}

@EventSourcingHandler
public void on(CustomerUpdatedEvent event) {
    this.name = event.getName();
    this.email = event.getEmail();
    
}
```

## Event sourcing flow

- When a command is received, Axon loads the aggregate by replaying its event stream (or a snapshot).
- The command handler executes, validates, and applies a new event.
- The event sourcing handler updates the aggregate fields accordingly.

## Validation and invariants

- Validate existence, uniqueness, and state transitions (e.g., "already exists", "already deactivated") in command handlers.
- Prefer meaningful domain-specific exceptions (`CustomerAlreadyExistsException`) to signal violations.

## Tips

- Keep command handlers free of I/O where possible; if needed, consider a policy/saga or use an injected component carefully. Axon allows parameter injection for command handlers, but be mindful of transactional consistency.
- Use snapshots if event streams become long for faster reconstruction.
- Maintain clear separation between write side (commands/events/aggregate) and read side (projections/queries).

## Testing

- Use Axon `AggregateTestFixture` to test command → event expectations and state evolution.
- Verify that the constructor-based command handler only handles `CreateCustomerCommand`, and that other commands are implemented as instance methods.

---

This documentation is colocated with `CustomerAggregate.java` to clarify aggregate responsibilities and best practices around constructor vs method command handlers in Axon.
