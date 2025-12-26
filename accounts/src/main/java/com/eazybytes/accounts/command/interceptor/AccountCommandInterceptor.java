package com.eazybytes.accounts.command.interceptor;

import com.eazybytes.accounts.repository.AccountsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountCommandInterceptor {

    private final AccountsRepository accountsRepository;


}
