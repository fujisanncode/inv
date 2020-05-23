package com.raiink.inv.hmily.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.raiink.inv.hmily.domain.ConfirmLog;

@Component
public interface ConfirmRepository extends CrudRepository<ConfirmLog, String> {
  Boolean existsByTxNo(String txNo);
}
