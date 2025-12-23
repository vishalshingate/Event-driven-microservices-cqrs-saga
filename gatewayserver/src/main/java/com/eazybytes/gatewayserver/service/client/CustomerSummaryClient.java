package com.eazybytes.gatewayserver.service.client;

import com.eazybytes.gatewayserver.dto.AccountsDto;
import com.eazybytes.gatewayserver.dto.CardsDto;
import com.eazybytes.gatewayserver.dto.CustomerDto;
import com.eazybytes.gatewayserver.dto.LoansDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

public interface CustomerSummaryClient {
    @GetExchange(value = "/eazybank/customer/api/fetch", accept = "application/json")
    Mono<ResponseEntity<CustomerDto>> fetchCustomerDetails(@RequestParam("mobileNumber") String mobileNumber);

    @GetExchange(value = "/eazybank/accounts/api/fetch", accept = "application/json")
    Mono<ResponseEntity<AccountsDto>> fetchAccountsDetails(@RequestParam("mobileNumber") String mobileNumber);

    @GetExchange(value = "/eazybank/cards/api/fetch", accept = "application/json")
    Mono<ResponseEntity<CardsDto>> fetchCardsDetails(@RequestParam("mobileNumber") String mobileNumber);

    @GetExchange(value = "/eazybank/loans/api/fetch", accept = "application/json")
    Mono<ResponseEntity<LoansDto>> fetchLoansDetails(@RequestParam("mobileNumber") String mobileNumber);



    /***
     * In reactive programming we have to use Mono or Flux to handle asynchronous data streams.
     * Mono represents a single value or no value, while Flux represents a stream of multiple values
     * over time. By using Mono<ResponseEntity>, we can handle the response from the API
     * asynchronously, allowing for non-blocking operations and better resource utilization.
     */

    /***
     * We can't use the openFeign client here to call all the microservices parallely because openFeign it does not support reactive programming.
     * It is built on top of synchronous HTTP clients like Apache HttpClient or OkHttp,
     * We Can use WebClient from Spring WebFlux for making asynchronous and non-blocking HTTP requests to other microservices.
     */

}
