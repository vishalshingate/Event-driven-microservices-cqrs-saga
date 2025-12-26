package com.eazybytes.accounts.command.event;

import lombok.Data;

@Data
public class AccountUpdatedEvent {
    private Long accountNumber;
    private String accountType;
    private String branchAddress;
    private String mobileNumber;
    private boolean activeSw;
}
