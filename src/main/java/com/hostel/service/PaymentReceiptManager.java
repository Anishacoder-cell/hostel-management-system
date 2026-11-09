
package com.hostel.service;

import com.hostel.model.Payment;
import com.hostel.model.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PaymentReceiptManager {

    public void generateReceipt(Payment payment, Student student)
            throws IOException {

        String receipt =
            "============================================\n" +
            "          HOSTEL MANAGEMENT SYSTEM\n" +
            "              PAYMENT RECEIPT\n" +
            "============================================\n\n" +
            "Receipt Number: " + payment.getPaymentId() + "\n" +
            "Payment Date: " + payment.getPaymentDate() + "\n\n" +
            "STUDENT DETAILS\n" +
            "--------------------------------------------\n" +
            "Student Name: " + student.getFullName() + "\n" +
            "Student ID: " + student.getStudentId() + "\n" +
            "Course: " + student.getCourse() + "\n\n" +
            "PAYMENT DETAILS\n" +
            "--------------------------------------------\n" +
            "Amount Paid: UGX " +
                String.format("%,.2f", payment.getAmount()) + "\n" +
            "Payment Method: " + payment.getPaymentMethod() + "\n" +
            "Description: " + payment.getDescription() + "\n\n" +
            "============================================\n" +
            "          PAYMENT RECEIVED SUCCESSFULLY\n" +
            "          Thank you for your payment!\n" +
            "============================================\n";

        Path receiptDirectory = Path.of("data", "receipts");

        Files.createDirectories(receiptDirectory);

        Path receiptFile = receiptDirectory.resolve(
            payment.getPaymentId() + ".txt"
        );

        Files.writeString(receiptFile, receipt);

        System.out.println("\n" + receipt);
        System.out.println("Receipt saved successfully!");
        System.out.println("Receipt location: " + receiptFile);
    }
}
