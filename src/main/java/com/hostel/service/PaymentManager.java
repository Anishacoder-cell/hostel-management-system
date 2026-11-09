
package com.hostel.service;

import com.hostel.model.Payment;
import com.hostel.model.Student;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentManager {

    private final ArrayList<Payment> payments = new ArrayList<>();
    private final Path filePath = Path.of("data", "payments.txt");
    private int nextId = 1;

    public PaymentManager() throws IOException {
        loadPayments();
    }

    public Payment recordPayment(
            StudentManager studentManager,
            String studentId,
            BigDecimal amount,
            String paymentMethod,
            String description) throws IOException {

        Student student = studentManager.findStudentById(studentId);

        if (student == null) {
            throw new IllegalArgumentException(
                "Student ID does not exist."
            );
        }

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                "Payment amount must be greater than zero."
            );
        }

        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new IllegalArgumentException(
                "Payment method is required."
            );
        }

        String paymentId = String.format("PAY%03d", nextId);

        Payment payment = new Payment(
            paymentId,
            student.getStudentId(),
            amount.setScale(2, java.math.RoundingMode.HALF_UP),
            LocalDate.now(),
            clean(paymentMethod),
            clean(description == null ? "" : description)
        );

        payments.add(payment);

        try {
            savePayments();
        } catch (IOException e) {
            payments.remove(payment);
            throw e;
        }

        nextId++;

        System.out.println("\nPayment recorded successfully!");

        try {
            PaymentReceiptManager receiptManager =
                new PaymentReceiptManager();

            receiptManager.generateReceipt(payment, student);

        } catch (IOException e) {
            System.out.println(
                "Payment was saved, but receipt could not be generated: "
                + e.getMessage()
            );
        }

        return payment;
    }

    public void viewAllPayments() {

        if (payments.isEmpty()) {
            System.out.println("\nNo payment records found.");
            return;
        }

        System.out.println("\n========== ALL PAYMENT RECORDS ==========");

        for (Payment payment : payments) {
            System.out.println("----------------------------------");
            System.out.println(payment);
        }

        System.out.println("----------------------------------");
        System.out.println("Total Payment Records: " + payments.size());
        System.out.println("Total Amount Received: UGX " +
            getTotalPayments().toPlainString());
    }

    public void viewPaymentsByStudent(String studentId) {

        boolean found = false;
        BigDecimal studentTotal = BigDecimal.ZERO;

        System.out.println(
            "\n========== STUDENT PAYMENT HISTORY =========="
        );

        for (Payment payment : payments) {

            if (payment.getStudentId().equalsIgnoreCase(studentId)) {

                System.out.println("----------------------------------");
                System.out.println(payment);

                studentTotal = studentTotal.add(payment.getAmount());
                found = true;
            }
        }

        if (!found) {
            System.out.println(
                "No payments found for student " + studentId
            );
            return;
        }

        System.out.println("----------------------------------");
        System.out.println("Student Total Paid: UGX " +
            studentTotal.toPlainString());
    }

    public BigDecimal getTotalPayments() {

        BigDecimal total = BigDecimal.ZERO;

        for (Payment payment : payments) {
            total = total.add(payment.getAmount());
        }

        return total;
    }

    private void loadPayments() throws IOException {

        Files.createDirectories(filePath.getParent());

        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
            return;
        }

        List<String> records = Files.readAllLines(filePath);

        for (String record : records) {

            String[] fields = record.split("\\|", -1);

            if (fields.length == 6) {

                try {

                    Payment payment = new Payment(
                        fields[0],
                        fields[1],
                        new BigDecimal(fields[2]),
                        LocalDate.parse(fields[3]),
                        fields[4],
                        fields[5]
                    );

                    payments.add(payment);

                    if (fields[0].startsWith("PAY")) {
                        int number = Integer.parseInt(
                            fields[0].substring(3)
                        );

                        nextId = Math.max(nextId, number + 1);
                    }

                } catch (NumberFormatException |
                         java.time.format.DateTimeParseException e) {

                    System.out.println(
                        "Warning: Skipping an invalid payment record."
                    );
                }
            }
        }
    }

    private void savePayments() throws IOException {

        List<String> records = new ArrayList<>();

        for (Payment payment : payments) {

            records.add(
                clean(payment.getPaymentId()) + "|" +
                clean(payment.getStudentId()) + "|" +
                payment.getAmount().toPlainString() + "|" +
                payment.getPaymentDate() + "|" +
                clean(payment.getPaymentMethod()) + "|" +
                clean(payment.getDescription())
            );
        }

        Files.write(filePath, records);
    }

    private String clean(String value) {
        return value.replace("|", "/")
                    .replace("\n", " ")
                    .replace("\r", " ")
                    .trim();
    }
}
