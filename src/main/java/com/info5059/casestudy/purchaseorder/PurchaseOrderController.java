package com.info5059.casestudy.purchaseorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class PurchaseOrderController {
    @Autowired
    private PurchaseOrderDAO purchaseOrderDAO;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @PostMapping("/api/purchaseorders")
    public ResponseEntity<PurchaseOrder> addOne(@RequestBody PurchaseOrder clientrep) { // use RequestBody here
        return new ResponseEntity<PurchaseOrder>(purchaseOrderDAO.create(clientrep), HttpStatus.OK);
    }

    @GetMapping("/api/purchaseorders/{id}")
    public ResponseEntity<Iterable<PurchaseOrder>> findByEmployee(@PathVariable Long id) {
        ResponseEntity<Iterable<PurchaseOrder>> temp = new ResponseEntity<Iterable<PurchaseOrder>>(purchaseOrderRepository.findByVendorid(id), HttpStatus.OK);
        return temp;
    }
}
