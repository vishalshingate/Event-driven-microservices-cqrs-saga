package com.eazybytes.accounts.command.event;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class AccountCreatedEvent {
    private Long accountNumber;
    private String accountType;
    private String branchAddress;
    private String mobileNumber;
    private boolean activeSw;
}
