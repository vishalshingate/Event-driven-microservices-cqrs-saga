package com.eazybytes.customer.command.Agregate;

import com.eazybytes.customer.command.CreateCustomerCommand;
import com.eazybytes.customer.command.DeleteCustomerCommand;
import com.eazybytes.customer.command.UpdateCustomerCommand;
import com.eazybytes.customer.command.event.CustomerCreatedEvent;
import com.eazybytes.customer.command.event.CustomerDeletedEvent;
import com.eazybytes.customer.command.event.CustomerUpdatedEvent;
import com.eazybytes.customer.dto.CustomerDto;
import com.eazybytes.customer.entity.Customer;
import com.eazybytes.customer.exception.CustomerAlreadyExistsException;
import com.eazybytes.customer.repository.CustomerRepository;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@Aggregate// we are telling to action framework this class is an aggregate
/** responsibilities of this class
 *  Process all the commands
 *  store the given data using event sourcing pattern
 *  publish the events to event store// (to sync the data at read side)
 */
public class CustomerAggregate {

    @AggregateIdentifier // choose the field that never changes for an entity,
    // because this AggregateIdentifier will be used to manage the sequence of events for a particular entity
    private String customerId;
    private String name;
    private String email;
    private String mobileNumber;
    private boolean activeSw;

    public CustomerAggregate() {
        // default constructor required by Axon to construct the aggregate
    }

    @CommandHandler // to tell axon framework that this constructor will handle the command
    public CustomerAggregate(CreateCustomerCommand createCustomerCommand, CustomerRepository customerRepository) {
        // handle the create customer command
        Optional<Customer> optionalCustomer =customerRepository.findByMobileNumberAndActiveSw(createCustomerCommand.getMobileNumber(), true);
        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer with mobile number "+createCustomerCommand.getMobileNumber()+" already exists");
        }
        CustomerCreatedEvent customerCreatedEvent = new CustomerCreatedEvent();
        BeanUtils.copyProperties(createCustomerCommand,customerCreatedEvent);

        // publish the event

        AggregateLifecycle.apply(customerCreatedEvent);

    }
    @CommandHandler
    public void updateCustomerAggregate(UpdateCustomerCommand updateCustomerCommand) {
        CustomerUpdatedEvent customerUpdatedEvent = new CustomerUpdatedEvent();

        //copy properties from command to event
        BeanUtils.copyProperties(updateCustomerCommand,customerUpdatedEvent);

        AggregateLifecycle.apply(customerUpdatedEvent);


    }

    @CommandHandler
    public void deleteCustomerAggregate(DeleteCustomerCommand deleteCustomerCommand) {
        CustomerDeletedEvent customerDeletedEvent = new CustomerDeletedEvent();

        BeanUtils.copyProperties(deleteCustomerCommand,customerDeletedEvent);

        AggregateLifecycle.apply(customerDeletedEvent);
    }

    // this will store the data ino event sourcing DB
    @EventSourcingHandler // to tell axon framework that this method will handle the event
    public void on(CustomerCreatedEvent customerCreatedEvent) {
        this.customerId = customerCreatedEvent.getCustomerId();
        this.name = customerCreatedEvent.getName();
        this.email = customerCreatedEvent.getEmail();
        this.mobileNumber = customerCreatedEvent.getMobileNumber();
        this.activeSw = customerCreatedEvent.isActiveSw();
        /**
         * we just have to right this code and behind the scenes axon framework will look fo the
         * storage database and all this event will be stored in an historical or sequential style
         */
    }


    @EventSourcingHandler // to tell axon framework that this method will handle the event
    public void on(CustomerUpdatedEvent customerUpdatedEvent) {
        this.name = customerUpdatedEvent.getName();
        this.email = customerUpdatedEvent.getEmail();
    }

    @EventSourcingHandler
    public void on(CustomerDeletedEvent customerDeletedEvent) {
        this.activeSw = customerDeletedEvent.isActiveSw();
    }



}
