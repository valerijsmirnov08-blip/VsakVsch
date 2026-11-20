package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.example.demo.dto.FinanceSummaryDto;
import com.example.demo.entity.FinanceStatus;
import com.example.demo.entity.FinanceType;
import com.example.demo.entity.Finances;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.VendorProfile;
import com.example.demo.repository.FinancesRepository;
import com.example.demo.repository.VendorProfileRepository;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Service 
public class FinancesService
{
    @Autowired 
    private FinancesRepository financesRepository;
    @Autowired 
    private VendorProfileRepository vendorProfileRepository;

    public FinanceSummaryDto getVendorBalanceSummary(Long vendorId)
    {
        BigDecimal frozen = financesRepository.sumNetAmountByVendorAndTypeAndStatus(vendorId, FinanceType.SALE, FinanceStatus.FROZEN);
        
        BigDecimal availableSales = financesRepository.sumNetAmountByVendorAndTypeAndStatus(vendorId, FinanceType.SALE, FinanceStatus.AVAILABLE);
        
        BigDecimal totalPaid = financesRepository.sumNetAmountByVendorAndTypeAndStatus(vendorId, FinanceType.PAYOUT, FinanceStatus.PAID);
    
        BigDecimal pendingPayouts = financesRepository.sumNetAmountByVendorAndTypeAndStatus(vendorId, FinanceType.PAYOUT, FinanceStatus.PENDING);
    
        BigDecimal availableBalance = availableSales.subtract(totalPaid).subtract(pendingPayouts);

        return new FinanceSummaryDto(availableBalance, frozen, totalPaid);
    }
    public List<Finances> getVendorPayouts(Long vendorId)
    {
        return financesRepository.findByVendorVendorIdAndTypeOrderByCreatedAtDesc(vendorId, FinanceType.PAYOUT);
    }
    @Transactional
    public Finances requestPayout(Long vendorId)
    {
        FinanceSummaryDto summary = getVendorBalanceSummary(vendorId);
        BigDecimal amount = summary.availableBalance();
        if(amount.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new RuntimeException("Нет доступных средств для вывода");
        }

        VendorProfile vendor = vendorProfileRepository.findByVendorId(vendorId).orElseThrow(() ->
        new RuntimeException("Продавец не найден"));

        Finances payout = new Finances();
        payout.setVendor(vendor);
        payout.setType(FinanceType.PAYOUT);
        payout.setStatus(FinanceStatus.PENDING);
        payout.setNetAmount(amount);
        payout.setInvoiceNumber("INV-" + (System.currentTimeMillis() % 100000));
        payout.setPlannedDate(LocalDateTime.now().plusDays(3));

        return  financesRepository.save(payout);
    }
    @Transactional
    public void recordSale(OrderItem item)
    {
        BigDecimal grossAmount = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        BigDecimal commission = grossAmount.multiply(new BigDecimal("0.10"));
        BigDecimal netAmount = grossAmount.subtract(commission);

        VendorProfile vendor = vendorProfileRepository.findByVendorId(item.getVendorId())
            .orElseGet(() -> vendorProfileRepository.save(
                new VendorProfile(item.getProduct().getVendor(), "Магазин #" + item.getVendorId(), "0000000000", "Не указан")
            ));

        Finances sale = new Finances();
        sale.setVendor(vendor);
        sale.setOrderItem(item);
        sale.setType(FinanceType.SALE);
        sale.setStatus(FinanceStatus.FROZEN);
        sale.setGrossAmount(grossAmount);
        sale.setCommissionAmount(commission);
        sale.setNetAmount(netAmount);

    financesRepository.save(sale);
    
    }
    @Transactional
    public void updateSaleStatusByOrderItem(Long orderItemId, String newOrderStatus)
    {
        String cleanStatus = newOrderStatus.replace("\"", "").trim();
        financesRepository.findByOrderItemId(orderItemId).ifPresent(sale ->
    {
        if("Доставлен".equals(cleanStatus))
        {
            sale.setStatus(FinanceStatus.AVAILABLE);
            financesRepository.save(sale);
        }
        else if("Отменен".equals(cleanStatus) || "Отменён".equals(cleanStatus))
            {
                sale.setType(FinanceType.REFUND);
                financesRepository.save(sale);
            }
        });
    }
}
