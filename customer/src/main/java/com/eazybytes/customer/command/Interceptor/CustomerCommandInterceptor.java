package com.eazybytes.customer.command.Interceptor;


import com.eazybytes.customer.command.CreateCustomerCommand;
import com.eazybytes.customer.command.DeleteCustomerCommand;
import com.eazybytes.customer.command.UpdateCustomerCommand;
import com.eazybytes.customer.entity.Customer;
import com.eazybytes.customer.exception.CustomerAlreadyExistsException;
import com.eazybytes.customer.exception.ResourceNotFoundException;
import com.eazybytes.customer.repository.CustomerRepository;
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
public class CustomerCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
    private final CustomerRepository customerRepository;

    @Override
    public @NonNull BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@NonNull List<? extends CommandMessage<?>> messages) {
        return (index, command)->{
            //intercept each command message

           // validate CreateCustomerCommand for existing customer with same mobile number
            if(CreateCustomerCommand.class.equals(command.getPayloadType())) {
               CreateCustomerCommand createCustomerCommand = (CreateCustomerCommand)command.getPayload();
               Optional<Customer>
                   optionalCustomer =customerRepository.findByMobileNumberAndActiveSw(createCustomerCommand.getMobileNumber(), true);
               if(optionalCustomer.isPresent()){
                   throw new CustomerAlreadyExistsException("Customer with mobile number "+createCustomerCommand.getMobileNumber()+" already exists");
               }

           }
            // validate UpdateCustomerCommand for non existing customer with same mobile number
            if(UpdateCustomerCommand.class.equals(command.getPayloadType())) {
                UpdateCustomerCommand updateCustomerCommand = (UpdateCustomerCommand)command.getPayload();
                Customer customer
                     =customerRepository.findByMobileNumberAndActiveSw(updateCustomerCommand.getMobileNumber(), true).orElseThrow(()->
                    new ResourceNotFoundException("Customer", "mobileNumber", updateCustomerCommand.getMobileNumber()));
            }

            // validate DeleteCustomerCommand for non existing customer
            if(DeleteCustomerCommand.class.equals(command.getPayloadType())) {
                DeleteCustomerCommand deleteCustomerCommand = (DeleteCustomerCommand) command.getPayload();
                Customer customer
                    =customerRepository.findByCustomerIdAndActiveSw(deleteCustomerCommand.getCustomerId(), true).orElseThrow(()->
                    new ResourceNotFoundException("Customer", "mobileNumber", deleteCustomerCommand.getCustomerId()));
            }



            return command;
    };
}


}
