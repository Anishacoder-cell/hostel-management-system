
package com.hostel;

import com.hostel.model.Student;
import com.hostel.service.StudentManager;
import com.hostel.service.RoomManager;
import com.hostel.service.AllocationManager;
import com.hostel.service.PaymentManager;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

public class Main {

    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            StudentManager studentManager = new StudentManager();
            RoomManager roomManager = new RoomManager();
            AllocationManager allocationManager = new AllocationManager();
            PaymentManager paymentManager = new PaymentManager();

            while (true) {

                System.out.println("\n======================================");
                System.out.println("       HOSTEL MANAGEMENT SYSTEM");
                System.out.println("======================================");
                System.out.println("1. Register Student");
                System.out.println("2. View Students");
                System.out.println("3. Manage Rooms");
                System.out.println("4. Allocate Room");
                System.out.println("5. Manage Payments");
                System.out.println("6. Exit");

                System.out.print("\nEnter your choice: ");

                String choice = input.nextLine().trim();

                switch (choice) {

                    case "1":
                        registerStudent(studentManager);
                        break;

                    case "2":
                        studentManager.viewStudents();
                        break;

                    case "3":
                        manageRooms(roomManager);
                        break;

                    case "4":
                        manageAllocations(
                            allocationManager,
                            studentManager,
                            roomManager
                        );
                        break;

                    case "5":
                        managePayments(paymentManager, studentManager);
                        break;

                    case "6":
                        System.out.println(
                            "Thank you for using Hostel Management System!"
                        );
                        return;

                    default:
                        System.out.println("Invalid choice. Enter 1 to 6.");
                }
            }

        } catch (IOException e) {
            System.out.println("System error: " + e.getMessage());
        } finally {
            input.close();
        }
    }

    // STUDENT REGISTRATION

    private static void registerStudent(StudentManager studentManager) {

        System.out.println("\n========== STUDENT REGISTRATION ==========");

        String fullName = readRequired("Enter full name: ");

        String email;

        while (true) {
            email = readRequired("Enter email address: ");

            if (email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                break;
            }

            System.out.println("Please enter a valid email address.");
        }

        String phone = readRequired("Enter phone number: ");
        String course = readRequired("Enter course/programme: ");

        try {
            Student student = studentManager.registerStudent(
                fullName, email, phone, course
            );

            System.out.println("\nStudent registered successfully!");
            System.out.println("----------------------------------");
            System.out.println(student);
            System.out.println("----------------------------------");

        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());

        } catch (IOException e) {
            System.out.println("Could not save student: " + e.getMessage());
        }
    }

    // ROOM MANAGEMENT MENU

    private static void manageRooms(RoomManager roomManager) {

        while (true) {

            System.out.println("\n======================================");
            System.out.println("          ROOM MANAGEMENT");
            System.out.println("======================================");
            System.out.println("1. Add New Room");
            System.out.println("2. View All Rooms");
            System.out.println("3. View Available Rooms");
            System.out.println("4. Back to Main Menu");

            System.out.print("\nEnter your choice: ");

            String choice = input.nextLine().trim();

            switch (choice) {

                case "1":
                    addRoom(roomManager);
                    break;

                case "2":
                    roomManager.viewAllRooms();
                    break;

                case "3":
                    roomManager.viewAvailableRooms();
                    break;

                case "4":
                    return;

                default:
                    System.out.println("Invalid choice. Enter 1 to 4.");
            }
        }
    }

    // ADD NEW ROOM

    private static void addRoom(RoomManager roomManager) {

        System.out.println("\n========== ADD NEW ROOM ==========");

        String roomNumber = readRequired("Enter room number: ");

        System.out.println("\nRoom Types:");
        System.out.println("1. Single");
        System.out.println("2. Double");
        System.out.println("3. Shared");

        System.out.print("Choose room type: ");

        String typeChoice = input.nextLine().trim();

        String roomType;

        switch (typeChoice) {
            case "1":
                roomType = "Single";
                break;
            case "2":
                roomType = "Double";
                break;
            case "3":
                roomType = "Shared";
                break;
            default:
                System.out.println("Invalid room type.");
                return;
        }

        int capacity = readPositiveInteger("Enter room capacity: ");
        double price = readPositiveDouble("Enter monthly price (UGX): ");

        try {
            roomManager.addRoom(roomNumber, roomType, capacity, price);

        } catch (IllegalArgumentException e) {
            System.out.println("Could not add room: " + e.getMessage());

        } catch (IOException e) {
            System.out.println("Could not save room: " + e.getMessage());
        }
    }

    // ROOM ALLOCATION MENU

    private static void manageAllocations(
            AllocationManager allocationManager,
            StudentManager studentManager,
            RoomManager roomManager) {

        while (true) {

            System.out.println("\n======================================");
            System.out.println("          ROOM ALLOCATION");
            System.out.println("======================================");
            System.out.println("1. Allocate Room to Student");
            System.out.println("2. View All Allocations");
            System.out.println("3. Back to Main Menu");

            System.out.print("\nEnter your choice: ");

            String choice = input.nextLine().trim();

            switch (choice) {

                case "1":
                    allocateRoom(
                        allocationManager,
                        studentManager,
                        roomManager
                    );
                    break;

                case "2":
                    allocationManager.viewAllocations();
                    break;

                case "3":
                    return;

                default:
                    System.out.println("Invalid choice. Enter 1 to 3.");
            }
        }
    }

    // ALLOCATE ROOM TO STUDENT

    private static void allocateRoom(
            AllocationManager allocationManager,
            StudentManager studentManager,
            RoomManager roomManager) {

        System.out.println("\n========== ALLOCATE ROOM ==========");

        String studentId = readRequired("Enter student ID: ");
        String roomNumber = readRequired("Enter room number: ");

        try {
            allocationManager.allocateRoom(
                studentManager,
                roomManager,
                studentId,
                roomNumber
            );

        } catch (IllegalArgumentException e) {
            System.out.println("Allocation failed: " + e.getMessage());

        } catch (IOException e) {
            System.out.println("Could not save allocation: " + e.getMessage());
        }
    }

    // PAYMENT MANAGEMENT MENU

    private static void managePayments(
            PaymentManager paymentManager,
            StudentManager studentManager) {

        while (true) {

            System.out.println("\n======================================");
            System.out.println("         PAYMENT MANAGEMENT");
            System.out.println("======================================");
            System.out.println("1. Record Student Payment");
            System.out.println("2. View All Payments");
            System.out.println("3. Search Payments by Student ID");
            System.out.println("4. View Total Payments Received");
            System.out.println("5. Back to Main Menu");

            System.out.print("\nEnter your choice: ");

            String choice = input.nextLine().trim();

            switch (choice) {

                case "1":
                    recordPayment(paymentManager, studentManager);
                    break;

                case "2":
                    paymentManager.viewAllPayments();
                    break;

                case "3":
                    String studentId = readRequired("Enter student ID: ");
                    paymentManager.viewPaymentsByStudent(studentId);
                    break;

                case "4":
                    System.out.println("\n========== PAYMENT SUMMARY ==========");
                    System.out.println("Total Payments Received: UGX " +
                        paymentManager.getTotalPayments().toPlainString());
                    break;

                case "5":
                    return;

                default:
                    System.out.println("Invalid choice. Enter 1 to 5.");
            }
        }
    }

    // RECORD STUDENT PAYMENT

    private static void recordPayment(
            PaymentManager paymentManager,
            StudentManager studentManager) {

        System.out.println("\n========== RECORD STUDENT PAYMENT ==========");

        String studentId = readRequired("Enter student ID: ");

        BigDecimal amount;

        while (true) {

            System.out.print("Enter amount paid (UGX): ");

            try {
                amount = new BigDecimal(input.nextLine().trim());

                if (amount.compareTo(BigDecimal.ZERO) > 0) {
                    break;
                }

                System.out.println("Amount must be greater than zero.");

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid payment amount.");
            }
        }

        String paymentMethod;

        while (true) {

            System.out.println("\nPayment Methods:");
            System.out.println("1. Cash");
            System.out.println("2. Mobile Money");
            System.out.println("3. Bank Transfer");
            System.out.println("4. Card");

            System.out.print("Choose payment method: ");

            String methodChoice = input.nextLine().trim();

            switch (methodChoice) {
                case "1":
                    paymentMethod = "Cash";
                    break;
                case "2":
                    paymentMethod = "Mobile Money";
                    break;
                case "3":
                    paymentMethod = "Bank Transfer";
                    break;
                case "4":
                    paymentMethod = "Card";
                    break;
                default:
                    System.out.println("Invalid choice. Select 1 to 4.");
                    continue;
            }

            break;
        }

        String description = readRequired(
            "Enter payment description: "
        );

        try {
            paymentManager.recordPayment(
                studentManager,
                studentId,
                amount,
                paymentMethod,
                description
            );

        } catch (IllegalArgumentException e) {
            System.out.println("Payment failed: " + e.getMessage());

        } catch (IOException e) {
            System.out.println("Could not save payment: " + e.getMessage());
        }
    }

    // INPUT VALIDATION

    private static String readRequired(String message) {

        while (true) {

            System.out.print(message);

            String value = input.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("This field cannot be empty.");
        }
    }

    private static int readPositiveInteger(String message) {

        while (true) {

            System.out.print(message);

            try {
                int value = Integer.parseInt(input.nextLine().trim());

                if (value > 0) {
                    return value;
                }

                System.out.println("Enter a number greater than zero.");

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static double readPositiveDouble(String message) {

        while (true) {

            System.out.print(message);

            try {
                double value = Double.parseDouble(input.nextLine().trim());

                if (Double.isFinite(value) && value > 0) {
                    return value;
                }

                System.out.println("Enter a valid price greater than zero.");

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid price.");
            }
        }
    }
}
