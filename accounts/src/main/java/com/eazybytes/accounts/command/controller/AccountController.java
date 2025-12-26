package com.eazybytes.accounts.command.controller;

import com.eazybytes.accounts.command.CreateAccountCommand;
import com.eazybytes.accounts.command.DeleteAccountCommand;
import com.eazybytes.accounts.command.UpdateAccountCommand;
import com.eazybytes.accounts.command.event.AccountCreatedEvent;
import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.ResponseDto;
import com.eazybytes.accounts.entity.Accounts;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Digits;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RequiredArgsConstructor
public class AccountController {
    private final CommandGateway commandGateway;


    @PostMapping("/create")
    public ResponseEntity<?> createAccount(
            @RequestParam("mobileNumber")
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
            String mobileNumber) {

        // we have to create the command
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        CreateAccountCommand createAccountCommand = CreateAccountCommand.builder()
                .mobileNumber(mobileNumber)
                .accountNumber(randomAccNumber)
                .accountType(AccountsConstants.SAVINGS)
                .branchAddress(AccountsConstants.ADDRESS)
                .activeSw(true)
                .build();


        //  publish event to axon framework

        commandGateway.sendAndWait(createAccountCommand);


        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @PutMapping("/update")
    public ResponseEntity<?> UpdateAccountDetails(@Valid @RequestBody AccountsDto accountsDto)
    {

        UpdateAccountCommand updateAccountCommand = UpdateAccountCommand.builder()
            .mobileNumber(accountsDto.getMobileNumber())
            .accountNumber(accountsDto.getAccountNumber())
            .accountType(accountsDto.getAccountType())
            .branchAddress(accountsDto.getBranchAddress())
            .activeSw(AccountsConstants.ACTIVE_SW).build();

        // send command to the framework
        // blocking call
        commandGateway.sendAndWait(updateAccountCommand);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
    }

    @PatchMapping("/delete")
    public ResponseEntity<?> deleteAccount(
            @RequestParam("accountNumber")
            @Digits(integer = 10, fraction = 0, message = "Account number must be 10 digits")
            Long accountNumber) {

        DeleteAccountCommand deleteAccountCommand = DeleteAccountCommand.builder()
            .accountNumber(accountNumber)
            .activeSw(AccountsConstants.IN_ACTIVE_SW)
            .build();

        // send command to the axon framework

        commandGateway.sendAndWait(deleteAccountCommand);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
    }

    private Accounts createNewAccount(String mobileNumber) {
        Accounts newAccount = new Accounts();
        newAccount.setMobileNumber(mobileNumber);
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setActiveSw(AccountsConstants.ACTIVE_SW);
        return newAccount;
    }
}
