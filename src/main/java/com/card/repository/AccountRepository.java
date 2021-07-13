package com.card.repository;

import com.card.entity.Account;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account>findByIdAndActive(Long id, Boolean active);
}
