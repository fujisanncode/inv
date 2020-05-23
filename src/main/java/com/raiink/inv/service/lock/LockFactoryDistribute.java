package com.raiink.inv.service.lock;

import com.raiink.inv.service.lock.model.GetLock;
import com.raiink.inv.service.lock.model.GetLockDistribute;
import com.raiink.inv.service.lock.model.ReleaseLock;
import com.raiink.inv.service.lock.model.ReleaseLockDistribute;

/**
 * @description:
 * @author: hulei
 * @create: 2020-05-22 22:09:44
 */
public class LockFactoryDistribute implements LockFactory {

  @Override
  public GetLock newGetLock() {
    return new GetLockDistribute();
  }

  @Override
  public ReleaseLock newReleaseLock() {
    return new ReleaseLockDistribute();
  }
}
