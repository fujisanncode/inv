package com.raiink.inv.service.lock;

import com.raiink.inv.service.lock.model.GetLock;
import com.raiink.inv.service.lock.model.GetLockMonomer;
import com.raiink.inv.service.lock.model.ReleaseLock;
import com.raiink.inv.service.lock.model.ReleaseLockMonomer;

/**
 * @description:
 * @author: hulei
 * @create: 2020-05-22 21:59:36
 */
public class LockFactoryMonomer implements LockFactory {

  @Override
  public GetLock newGetLock() {
    return new GetLockMonomer();
  }

  @Override
  public ReleaseLock newReleaseLock() {
    return new ReleaseLockMonomer();
  }
}
