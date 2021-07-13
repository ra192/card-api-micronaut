package com.card.repository;

import com.card.entity.Merchant;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface MerchantRepository extends CrudRepository<Merchant,Long> {
}
