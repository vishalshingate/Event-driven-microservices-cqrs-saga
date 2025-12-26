package com.eazybytes.accounts.query.handler;

import com.eazybytes.accounts.command.event.AccountCreatedEvent;
import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.query.FindAccountQuery;
import com.eazybytes.accounts.service.IAccountsService;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountQueryHandler {
    private final IAccountsService accountsService;

    @QueryHandler
    public AccountsDto handle(FindAccountQuery findAccountQuery)
    {
       return accountsService.fetchAccount(findAccountQuery.getMobileNumber());
    }
}
