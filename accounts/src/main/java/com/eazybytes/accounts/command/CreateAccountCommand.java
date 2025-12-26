package com.eazybytes.accounts.command;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
@Data // it will give us getter and setters
public class CreateAccountCommand {
    @TargetAggregateIdentifier
    private final Long accountNumber;
    private final String accountType;
    private final String branchAddress;
    private final String mobileNumber;
    private final boolean activeSw;
}
