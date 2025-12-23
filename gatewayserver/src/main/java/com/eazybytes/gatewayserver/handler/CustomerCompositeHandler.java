package com.eazybytes.gatewayserver.handler;

import com.eazybytes.gatewayserver.service.client.CustomerSummaryClient;
import com.eazybytes.gatewayserver.dto.AccountsDto;
import com.eazybytes.gatewayserver.dto.CardsDto;
import com.eazybytes.gatewayserver.dto.CustomerDto;
import com.eazybytes.gatewayserver.dto.CustomerSummaryDto;
import com.eazybytes.gatewayserver.dto.LoansDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerCompositeHandler {

    // Ensure proper injection via Lombok-generated constructor
    private final CustomerSummaryClient customerSummaryClient;

    public Mono<ServerResponse> fetchCustomerSummary(ServerRequest serverRequest) {
        // Safely read query param to avoid NoSuchElementException when missing
        String mobileNumber = serverRequest.queryParam("mobileNumber")
                .orElse(null);
        if (mobileNumber == null || mobileNumber.isBlank()) {
            return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue("Missing required query parameter: mobileNumber"));
        }

        // these 4 calls will be made in parallel, with non-blocking style
        Mono< ResponseEntity<CustomerDto>> customerDetails = customerSummaryClient.fetchCustomerDetails(mobileNumber);
        Mono< ResponseEntity<AccountsDto>> accountsDetails = customerSummaryClient.fetchAccountsDetails(mobileNumber);
        Mono< ResponseEntity<LoansDto>> loanDetails = customerSummaryClient.fetchLoansDetails(mobileNumber);
        Mono< ResponseEntity<CardsDto>> cardsDetails = customerSummaryClient.fetchCardsDetails(mobileNumber);

        // Thread is going to be blocked until all the above 4 calls are completed
       return Mono.zip(customerDetails, accountsDetails, loanDetails, cardsDetails)
            .flatMap(touple-> {
                CustomerDto customerDto = touple.getT1().getBody();
                AccountsDto accountsDto = touple.getT2().getBody();
                LoansDto loansDto = touple.getT3().getBody();
                CardsDto cardsDto = touple.getT4().getBody();
                CustomerSummaryDto customerSummaryDto = new CustomerSummaryDto(customerDto, accountsDto, loansDto, cardsDto);
                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(customerSummaryDto));
            });

    }
}
