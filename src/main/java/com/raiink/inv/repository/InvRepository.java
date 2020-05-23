package com.raiink.inv.repository;

import com.raiink.inv.domain.GoodInvDo;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface InvRepository extends CrudRepository<GoodInvDo, String> {
  Optional<GoodInvDo> findGoodInvDoByGoodNo(String goodNo);

  @Modifying
  @Query(
      "update GoodInvDo gv set gv.goodCount = gv.goodCount - :#{#goodInvDo.goodFrozenCount}, "
          + "gv.goodFrozenCount = gv.goodFrozenCount + :#{#goodInvDo.goodFrozenCount} where gv.goodNo = :#{#goodInvDo.goodNo}")
  Integer invToFrozen(GoodInvDo goodInvDo);

  @Modifying
  @Query(
      "update GoodInvDo gv set gv.goodFrozenCount = gv.goodFrozenCount - :#{#goodInvDo.goodFrozenCount} "
          + "where gv.goodNo = :#{#goodInvDo.goodNo}")
  Integer consumerFrozen(GoodInvDo goodInvDo);

  // 对象传参必须#{#}，否则会报错
  @Modifying
  @Query(
      "update GoodInvDo gv set gv.goodCount = gv.goodCount + :#{#goodInvDo.goodFrozenCount}, "
          + "gv.goodFrozenCount = gv.goodFrozenCount - :#{#goodInvDo.goodFrozenCount} where gv.goodNo = :#{#goodInvDo.goodNo}")
  Integer releaseFrozen(GoodInvDo goodInvDo);

  // Modifying queries can only use void or int/Integer as return type!
  // 不能使用boolean返回，没有修改数据，只要找到一条数据就返回1；找不到返回0
  @Modifying
  @Query("update GoodInvDo gv set gv.goodCount = :invCount where gv.goodNo = :goodNo")
  Boolean testUpdateInvBoolean(String goodNo, Integer invCount);

  @Modifying
  @Query("update GoodInvDo gv set gv.goodCount = :invCount where gv.goodNo = :goodNo")
  Integer testUpdateInvInteger(String goodNo, Integer invCount);
}
