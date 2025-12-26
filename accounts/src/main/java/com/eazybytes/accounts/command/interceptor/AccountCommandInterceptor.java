package com.eazybytes.accounts.command.interceptor;

import com.eazybytes.accounts.command.CreateAccountCommand;
import com.eazybytes.accounts.command.DeleteAccountCommand;
import com.eazybytes.accounts.command.UpdateAccountCommand;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.exception.AccountAlreadyExistsException;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.repository.AccountsRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class AccountCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
    private final AccountsRepository accountsRepository;

    @Override
    public @NonNull BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@NonNull List<? extends CommandMessage<?>> messages) {
        return(index, command) -> {
            // validation for Create Customer
            if(CreateAccountCommand.class.equals(command.getPayloadType())) {
                CreateAccountCommand createAccountCommand = (CreateAccountCommand) command.getPayload();
                Optional<Accounts> optionalAccounts =  accountsRepository.findByMobileNumberAndActiveSw(createAccountCommand.getMobileNumber(),
                    createAccountCommand.isActiveSw());

                if(optionalAccounts.isPresent()) {
                    throw new AccountAlreadyExistsException("Account with mobile number "+createAccountCommand.getMobileNumber()+" already exists");
                }
            }

            //validation for update Customer

            if(UpdateAccountCommand.class.equals(command.getPayloadType())) {
                UpdateAccountCommand updateAccountCommand = (UpdateAccountCommand) command.getPayload();
                Optional<Accounts> optionalAccounts =  accountsRepository.findByMobileNumberAndActiveSw(updateAccountCommand.getMobileNumber(),
                    updateAccountCommand.isActiveSw());

                if(optionalAccounts.isEmpty()) {
                    throw new ResourceNotFoundException("Account","mobile number",updateAccountCommand.getMobileNumber()+" is not found");
                }
            }

            //validation for update Customer

            if(DeleteAccountCommand.class.equals(command.getPayloadType())) {
                DeleteAccountCommand deleteAccountCommand = (DeleteAccountCommand) command.getPayload();
                Optional<Accounts> optionalAccounts =  accountsRepository.findByAccountNumberAndActiveSw(deleteAccountCommand.getAccountNumber(),
                    true
                    );

                if(optionalAccounts.isEmpty()) {
                    throw new ResourceNotFoundException("Account", "AccountNo", deleteAccountCommand.getAccountNumber()+" is not found");
                }
            }


            return command;
        };
    }

    //private final AccountsRepository accountsRepository;


}
