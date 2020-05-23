package com.raiink.inv.hmily.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.raiink.inv.hmily.domain.CancelLog;

@Component
public interface CancelRepository extends CrudRepository<CancelLog, String> {
  Boolean existsByTxNo(String txNo);
}
