package com.info5059.casestudy.purchaseorder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    @Modifying
    @Transactional
    @Query("delete from PurchaseOrder where id = ?1 ")
    int deleteOne(Long purchaseorderid);
}
