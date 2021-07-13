package com.card.repository;

import com.card.entity.Customer;
import com.card.entity.Merchant;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface CustomerRepository extends CrudRepository<Customer,Long> {
    Optional<Customer> findByPhoneAndMerchant(String phone, Merchant merchant);

    Optional<Customer> findByIdAndActive(Long id, boolean active);
}
