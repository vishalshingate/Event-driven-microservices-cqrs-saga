package com.eazybytes.accounts.query.projection;

import com.eazybytes.accounts.command.event.AccountCreatedEvent;
import com.eazybytes.accounts.command.event.AccountDeletedEvent;
import com.eazybytes.accounts.command.event.AccountUpdatedEvent;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.service.IAccountsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountProjection {
    private final IAccountsService accountsService;

    @EventHandler
    public void on(AccountCreatedEvent accountCreatedEvent){
        Accounts accountEntity = new Accounts();

        BeanUtils.copyProperties(accountCreatedEvent, accountEntity);

        log.info("Account Created Event received for Customer ID: " + accountCreatedEvent.getAccountNumber());
        accountsService.createAccount(accountEntity);

    }

    @EventHandler
    public void on(AccountUpdatedEvent accountUpdatedEvent){
        Accounts accountEntity = new Accounts();
        BeanUtils.copyProperties(accountUpdatedEvent, accountEntity);

        log.info("Account Updated Event received for Customer ID: " + accountUpdatedEvent.getAccountNumber());

        accountsService.updateAccount(accountEntity);

    }

    @EventHandler
    public void on(AccountDeletedEvent accountDeletedEvent){
       log.info("Account Deleted Event received for Customer ID: " + accountDeletedEvent.getAccountNumber());
       accountsService.deleteAccount(accountDeletedEvent.getAccountNumber());

    }
}
