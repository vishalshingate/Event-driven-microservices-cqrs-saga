package com.eazybytes.customer.command.event;

import lombok.Data;

/**
 * NOUN+VERB(past tense)+EVENT
 *
 */
@Data
public class CustomerCreatedEvent {
    private String customerId;
    private String name;
    private String email;
    private String mobileNumber;
    private boolean activeSw;
}
