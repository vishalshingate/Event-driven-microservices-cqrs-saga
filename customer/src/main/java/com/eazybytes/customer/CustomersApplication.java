package com.eazybytes.customer;

import com.eazybytes.customer.command.Interceptor.CustomerCommandInterceptor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class CustomersApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomersApplication.class, args);
    }

    @Autowired
    public void registerCommandInterceptors(ApplicationContext context, CommandGateway commandGateway) {
        // Method intentionally left blank.
        commandGateway.registerDispatchInterceptor(context.getBean(CustomerCommandInterceptor.class));

    }
}
