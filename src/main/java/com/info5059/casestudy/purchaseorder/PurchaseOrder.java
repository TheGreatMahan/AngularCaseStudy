package com.info5059.casestudy.purchaseorder;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;


@Data
@Entity
@RequiredArgsConstructor
public class PurchaseOrder {
    // PurchaseOrder private members
    @Id
    @GeneratedValue
    private Long id;
    private Long vendorid;
    private BigDecimal amount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime podate;
    @OneToMany(mappedBy = "poid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderLineitem> items = new ArrayList<PurchaseOrderLineitem>();
    @JsonFormat(pattern="yyyy-MM-dd@HH:mm:ss")
    private LocalDateTime datecreated; 
}
