package com.eazybytes.customer.query;

import lombok.Value;

/**
 * VERB+NOUN+QUERY
 *
 */
@Value
public class FindCustomerQuery {
    private final String mobileNumber;

}
