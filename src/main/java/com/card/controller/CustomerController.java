package com.card.controller;

import com.card.dto.CreateCustomerDto;
import com.card.dto.CustomerDto;
import com.card.entity.Customer;
import com.card.service.CustomerService;
import com.card.service.MerchantService;
import com.card.service.TokenService;
import com.card.service.exception.CustomerException;
import com.card.service.exception.MerchantException;
import com.card.service.exception.TokenException;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;

@Controller("/api/customer")
public class CustomerController extends WithAuthMerchantController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    public CustomerController(MerchantService merchantService, TokenService tokenService, CustomerService customerService) {
        super(merchantService, tokenService);
        this.customerService = customerService;
    }

    @Post
    public CustomerDto create(@Header String authorization, @Valid @Body CreateCustomerDto requestObject) throws MerchantException, CustomerException, TokenException, InvocationTargetException, IllegalAccessException {
        logger.info("Create customer method wath called with params:");
        logger.info(requestObject.toString());

        final var merchant = validateToken(authorization);

        final var customer = new Customer();
        BeanUtils.copyProperties(customer, requestObject);
        customer.setActive(true);
        customer.setMerchant(merchant);

        customerService.create(customer);

        final var result = new CustomerDto();
        BeanUtils.copyProperties(result, customer);

        return result;
    }
}
