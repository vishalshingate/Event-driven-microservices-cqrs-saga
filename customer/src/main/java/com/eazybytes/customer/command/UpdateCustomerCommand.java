package com.eazybytes.customer.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * VERB- what action is to be performed
 * NOUN- on which entity the action is to be performed
 * e.g.- UpdateCustomerCommand
 *naming convention for command classes (present tense)
 */
@Data
@Builder
public class UpdateCustomerCommand {
    @TargetAggregateIdentifier // this is similar to @Id in JPA entity
    private final String customerId;
    private final String name;
    private final String email;
    private final String mobileNumber;
    private final boolean activeSw;
}
