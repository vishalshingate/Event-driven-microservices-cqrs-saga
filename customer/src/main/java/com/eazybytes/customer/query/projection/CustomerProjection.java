package com.eazybytes.customer.query.projection;

import com.eazybytes.customer.command.event.CustomerCreatedEvent;
import com.eazybytes.customer.command.event.CustomerDeletedEvent;
import com.eazybytes.customer.command.event.CustomerUpdatedEvent;
import com.eazybytes.customer.entity.Customer;
import com.eazybytes.customer.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerProjection {

    private final ICustomerService customerService;

    /**
     * We want this method to take responsibility of handling event that is going to consume from the event bus
     * @param customerCreatedEvent
     */
    @EventHandler
    public void on( CustomerCreatedEvent customerCreatedEvent) {
            Customer customerEntity = new Customer();
            BeanUtils.copyProperties(customerCreatedEvent, customerEntity);
            // saving to the database
            log.info("Customer Created Event received for Customer ID: " + customerCreatedEvent.getCustomerId());
            customerService.createCustomer(customerEntity);

    }

    @EventHandler
    public void on( CustomerUpdatedEvent customerCreatedEvent) {
       // Customer customerEntity = new Customer();
        //BeanUtils.copyProperties(customerCreatedEvent, customerEntity);
        // saving to the database
        log.info("Customer Created Event received for Customer ID: " + customerCreatedEvent.getCustomerId());
        customerService.updateCustomer(customerCreatedEvent);

    }

    @EventHandler
    public void on( CustomerDeletedEvent customerDeletedEvent) {
        log.info("Customer Deleted Event received for Customer ID: " + customerDeletedEvent.getCustomerId());
        customerService.deleteCustomer( customerDeletedEvent.getCustomerId());
    }
}
