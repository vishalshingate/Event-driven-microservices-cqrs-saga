package com.eazybytes.customer.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * Command object for creating a new Customer aggregate.
 * <p>
 * This immutable DTO carries all the data required to create a Customer on the command side.
 * The constructor (via Lombok builder with final fields) is intentionally used only for create commands,
 * adhering to CQRS best practices.
 * </p>
 *
 * <p>
 * An instance of this command is typically dispatched through Axon's {@code CommandGateway}.
 * The {@link TargetAggregateIdentifier} annotation on {@code customerId} allows Axon to route
 * the command to the correct aggregate instance.
 * </p>
 */
@Builder
@Data
public class CreateCustomerCommand {
    /**
     * Unique identifier of the target aggregate.
     * Annotated with {@link TargetAggregateIdentifier} (similar to {@code @Id} in JPA) so Axon
     * can locate and route the command to the proper aggregate instance.
     */
    @TargetAggregateIdentifier // this is similar to @Id in JPA entity
    private final String customerId;
    private final String name;
    private final String email;
    private final String mobileNumber;
    private final boolean activeSw;

}
