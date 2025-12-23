package com.eazybytes.customer.query.controller;

import com.eazybytes.customer.dto.CustomerDto;
import com.eazybytes.customer.query.FindCustomerQuery;
import com.eazybytes.customer.service.ICustomerService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RequiredArgsConstructor
public class customerQueryController {
    /**
     * This we can do but we should not do this way when we are using CQRS pattern.
     * We Should go with the flow recommended by the framework.
     * We should use QueryGateway to fetch the data from the query side.
     */
   /* private final ICustomerService customerService;
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchCustomerDetails(@RequestParam("mobileNumber")
                                                            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                            String mobileNumber) {
        CustomerDto fetchedCustomer = customerService.fetchCustomer(mobileNumber);
        return ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(fetchedCustomer);
    }
  */

    private final QueryGateway queryGateway;
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchCustomerDetails(@RequestParam("mobileNumber")
                                                            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                            String mobileNumber) {
        FindCustomerQuery findCustomerQuery = new FindCustomerQuery(mobileNumber);

        CustomerDto fetchedCustomer= queryGateway.query(findCustomerQuery, ResponseTypes.instanceOf(CustomerDto.class)).join();
        // with this logic we are publishing the query to find customer query to the framework
        // but we dont have any logic how to handle this query.
        // So we need to create a handler for this query in the query side.
        return ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(fetchedCustomer);
    }

}
