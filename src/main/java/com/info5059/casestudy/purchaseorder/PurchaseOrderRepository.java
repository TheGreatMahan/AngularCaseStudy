package com.info5059.casestudy.purchaseorder;

import java.util.List;

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

    @Query("select p from PurchaseOrder p where p.vendorid = ?1")
    List<PurchaseOrder> findByVendorid(Long vendorid);
}
