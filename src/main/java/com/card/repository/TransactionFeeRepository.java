package com.card.repository;

import com.card.entity.Account;
import com.card.entity.TransactionFee;
import com.card.entity.enums.TransactionType;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface TransactionFeeRepository extends CrudRepository<TransactionFee, Long> {
    Optional<TransactionFee> findByTypeAndAccount(TransactionType type, Account account);
}
