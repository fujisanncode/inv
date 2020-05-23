package com.raiink.inv.service.lock;

import com.raiink.inv.service.lock.model.GetLock;
import com.raiink.inv.service.lock.model.ReleaseLock;

public interface LockFactory {
  GetLock newGetLock();

  ReleaseLock newReleaseLock();
}
