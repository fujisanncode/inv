package com.raiink.inv.hmily.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.raiink.inv.hmily.domain.TryLog;

@Component
public interface TryRepository extends CrudRepository<TryLog, String> {
  Boolean existsByTxNo(String txNo);
}
