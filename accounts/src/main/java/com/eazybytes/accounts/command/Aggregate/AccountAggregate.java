package com.eazybytes.accounts.command.Aggregate;

import com.eazybytes.accounts.command.CreateAccountCommand;
import com.eazybytes.accounts.command.DeleteAccountCommand;
import com.eazybytes.accounts.command.UpdateAccountCommand;
import com.eazybytes.accounts.command.event.AccountCreatedEvent;
import com.eazybytes.accounts.command.event.AccountDeletedEvent;
import com.eazybytes.accounts.command.event.AccountUpdatedEvent;
import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.exception.AccountAlreadyExistsException;
import com.eazybytes.accounts.repository.AccountsRepository;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@Aggregate
public class AccountAggregate {
    @AggregateIdentifier
    private Long accountNumber;
    private String mobileNumber;
    private String branchAddress;
    private String accountType;
    private boolean activeSw;

    //private final AccountsRepository accountsRepository;
    //  this required by axon framework
    public AccountAggregate () {}

    // handlers, we have to create handlers here > // logic to create event and publish to framework
    @CommandHandler
    public AccountAggregate(CreateAccountCommand createAccountCommand, AccountsRepository accountsRepository) {
        // before updating or publishing even to the framework we should validate if the account is already exist in framework

        Optional<Accounts> optionalAccounts =  accountsRepository.findByMobileNumberAndActiveSw(createAccountCommand.getMobileNumber(),
            createAccountCommand.isActiveSw());

        if(optionalAccounts.isPresent()) {
            throw new AccountAlreadyExistsException("Account with mobile number "+createAccountCommand.getMobileNumber()+" already exists");
        }

        AccountCreatedEvent accountCreatedEvent = new AccountCreatedEvent();

        BeanUtils.copyProperties(createAccountCommand, accountCreatedEvent);

        // sending this evet to axon framework once this event published to the framework
        //  framework will look for the logic to store the data of the event to the event sourcing db or normal db

        AggregateLifecycle.apply(accountCreatedEvent);


    }

    // this is the logic which going to store the event data to db
    // once we write the event data to write db framework is going to take care of publishing the event to the event bus
    @EventSourcingHandler
    public void on(AccountCreatedEvent accountCreatedEvent) {
        this.accountNumber = accountCreatedEvent.getAccountNumber();
        this.mobileNumber = accountCreatedEvent.getMobileNumber();
        this.branchAddress = accountCreatedEvent.getBranchAddress();
        this.accountType = accountCreatedEvent.getAccountType();
        this.activeSw = accountCreatedEvent.isActiveSw();
    }

    @CommandHandler
    public void handle(UpdateAccountCommand updateAccountCommand) {
        AccountUpdatedEvent accountUpdatedEvent = new AccountUpdatedEvent();
        BeanUtils.copyProperties(updateAccountCommand, accountUpdatedEvent);

        AggregateLifecycle.apply(accountUpdatedEvent);
    }

    @EventSourcingHandler
    public void on(AccountUpdatedEvent accountUpdatedEvent) {
        this.accountType = accountUpdatedEvent.getAccountType();
        this.branchAddress = accountUpdatedEvent.getBranchAddress();
    }

    @CommandHandler
    public void handle(DeleteAccountCommand deleteAccountCommand) {
        AccountDeletedEvent accountDeletedEvent = new AccountDeletedEvent();

        BeanUtils.copyProperties(deleteAccountCommand, accountDeletedEvent);

        AggregateLifecycle.apply(accountDeletedEvent);

    }

    @EventSourcingHandler
    public void on(AccountDeletedEvent accountDeletedEvent) {
        this.accountNumber = accountDeletedEvent.getAccountNumber();
        this.activeSw = accountDeletedEvent.isActiveSw();

    }

}
