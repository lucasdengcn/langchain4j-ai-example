package com.example.demo.tool;

import com.example.demo.model.Customer;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomerTools {


    @Tool(name = "getCustomer", value = "Retrieve customer information via name")
    public Customer getCustomer(String name) {
        log.info("Getting customer information via name for: {}", name);
        return new Customer(name, "1234567890@example.com");
    }

    @Tool(name = "getCustomerByEmail", value = "Retrieve customer information via email")
    public Customer getCustomerByEmail(String email) {
        log.info("Getting customer information via email for: {}", email);
        return new Customer("Deom", email);
    }

}
