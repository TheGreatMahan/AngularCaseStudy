package com.info5059.casestudy.purchaseorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.info5059.casestudy.product.Product;
import com.info5059.casestudy.product.ProductRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Component
public class PurchaseOrderDAO {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ProductRepository prodRepo;

    @Transactional
    public PurchaseOrder create(PurchaseOrder clientrep) {
        PurchaseOrder realPurchaseOrder = new PurchaseOrder();

        // realPurchaseOrder.setId(clientrep.getId());
        realPurchaseOrder.setVendorid(clientrep.getVendorid());
        realPurchaseOrder.setAmount(clientrep.getAmount());
        realPurchaseOrder.setPodate(LocalDateTime.now());
        realPurchaseOrder.setDatecreated(LocalDateTime.now());
        entityManager.persist(realPurchaseOrder);
        for (PurchaseOrderLineitem item : clientrep.getItems()) {
            PurchaseOrderLineitem realItem = new PurchaseOrderLineitem();
            // realItem.setId(realPurchaseOrder.getId());
            realItem.setProductid(item.getProductid());
            realItem.setQty(item.getQty());
            realItem.setPrice(item.getPrice());
            realItem.setPoid(realPurchaseOrder.getId());
            entityManager.persist(realItem);

            // we also need to update the QOO on the product table
            Product prod = prodRepo.getReferenceById(item.getProductid());
            prod.setQoo(prod.getQoo() + item.getQty());
            prodRepo.saveAndFlush(prod);
        }
        entityManager.refresh(realPurchaseOrder);
        return realPurchaseOrder;
    }
}
