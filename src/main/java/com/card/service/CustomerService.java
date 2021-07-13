package com.card.service;

import com.card.entity.Customer;
import com.card.repository.CustomerRepository;
import com.card.service.exception.CustomerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void create(Customer customer) throws CustomerException {
        if (customerRepository.findByPhoneAndMerchant(customer.getPhone(), customer.getMerchant()).isPresent())
            throw new CustomerException("Customer already exists");
        customerRepository.save(customer);
        logger.info("Customer was created with id: {}", customer.getId());
    }

    public Customer findActiveById(Long id) throws CustomerException {
        return customerRepository.findByIdAndActive(id, true)
                .orElseThrow(()->new CustomerException("Customer does not exist or is not active"));
    }
}
