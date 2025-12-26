# CreateCustomerCommand

A command object used by Axon Framework to request creation of a new Customer aggregate. Constructors (and this immutable structure) are intentionally used only for create commands, following CQRS best practices.

## Package
`com.eazybytes.customer.command`

## Purpose
- Carries the data required to create a Customer in the command side of the system.
- Identifies the target aggregate via `@TargetAggregateIdentifier`.
- Immutable by design using Lombok's `@Builder` and `@Data` with `final` fields.

## Fields
- `customerId` (String): Unique identifier of the target aggregate. Marked with `@TargetAggregateIdentifier` so Axon can route the command to the correct aggregate instance.
- `name` (String): Customer's name.
- `email` (String): Customer's email address.
- `mobileNumber` (String): Customer's mobile number.
- `activeSw` (boolean): Flag indicating active status of the customer.

## Usage
Construct using Lombok builder (example):

- Create the command with required fields using the builder pattern.
- Dispatch the command via Axon's CommandGateway from your service layer.

## Notes
- Constructor is reserved for create commands only, aligning with "Constructor should be used for only create Command" guidance.
- Since fields are `final`, the instance is immutable; use the builder to create a new instance rather than mutating.
- `@Data` generates getters, `equals`, `hashCode`, and `toString`. If you prefer not to expose setters on immutable objects, you can replace `@Data` with `@Getter`.
