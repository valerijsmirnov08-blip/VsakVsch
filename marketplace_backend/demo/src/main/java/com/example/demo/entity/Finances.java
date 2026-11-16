package com.example.demo.entity;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;


@Entity 
@Table (name = "finances")
public class Finances
{
    @Id 
    @GeneratedValue  (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne 
    @JoinColumn (name = "vendor_id", nullable = false)
    private VendorProfile vendor;

    @ManyToOne 
    @JoinColumn (name = "order_item_id")
    private OrderItem orderItem;

    @Enumerated(EnumType.STRING)
    @Column (name = "type", nullable = false)
    private FinanceType type;
    
    @Enumerated(EnumType.STRING)
    @Column (name = "status", nullable = false)
    private FinanceStatus status;

    @Column (name = "gross_amount")
    private BigDecimal grossAmount;

    @Column (name = "commission_amount")
    private BigDecimal commissionAmount;

    @Column (name = "net_amount", nullable = false)
    private BigDecimal netAmount;

    @Column (name = "invoice_number")
    private String invoiceNumber;

    @Column (name = "created_at")
    private LocalDateTime createdAt;
    
    @Column (name = "planned_date")
    private LocalDateTime plannedDate;

    @Column (name = "payment_date")
    private LocalDateTime paymentDate;

    @PrePersist
    protected void onCreate()
    {
        if (this.createdAt == null)
        {
            this.createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public VendorProfile getVendor() {return vendor;}
    public void setVendor(VendorProfile vendor) {this.vendor = vendor;}

    public OrderItem getOrderItem() {return orderItem;}
    public void setOrderItem(OrderItem orderItem) {this.orderItem = orderItem;}

    public FinanceType getType() {return type;}
    public void setType(FinanceType type) {this.type = type;}

    public FinanceStatus getStatus() {return status;}
    public void setStatus(FinanceStatus status) {this.status = status;}

    public BigDecimal getGrossAmount() {return grossAmount;}
    public void setGrossAmount(BigDecimal grossAmount) {this.grossAmount = grossAmount;}

    public BigDecimal getCommissionAmount() {return commissionAmount;}
    public void setCommissionAmount(BigDecimal commissionAmount) {this.commissionAmount = commissionAmount;}

    public BigDecimal getNetAmount() {return netAmount;}
    public void setNetAmount(BigDecimal netAmount) {this.netAmount = netAmount;}

    public String getInvoiceNumber() {return invoiceNumber;}
    public void setInvoiceNumber(String invoiceNumber) {this.invoiceNumber = invoiceNumber;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public LocalDateTime getPlannedDate() {return plannedDate;}
    public void setPlannedDate(LocalDateTime plannedDate) {this.plannedDate = plannedDate;}

    public LocalDateTime getPaymentDate() {return paymentDate;}
    public void setPaymentDate(LocalDateTime paymentDate) {this.paymentDate = paymentDate;}
}
