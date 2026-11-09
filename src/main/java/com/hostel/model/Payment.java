
package com.hostel.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Payment {

    private String paymentId;
    private String studentId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String description;

    public Payment(String paymentId, String studentId,
                   BigDecimal amount, LocalDate paymentDate,
                   String paymentMethod, String description) {

        this.paymentId = paymentId;
        this.studentId = studentId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.description = description;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Payment Reference: " + paymentId +
               "\nStudent ID: " + studentId +
               "\nAmount Paid: UGX " + amount.toPlainString() +
               "\nPayment Date: " + paymentDate +
               "\nPayment Method: " + paymentMethod +
               "\nDescription: " + description;
    }
}
