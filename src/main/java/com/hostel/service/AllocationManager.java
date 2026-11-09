
package com.hostel.service;

import com.hostel.model.Student;
import com.hostel.model.Room;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AllocationManager {

    private final ArrayList<String[]> allocations = new ArrayList<>();
    private final Path filePath = Path.of("data", "allocations.txt");

    public AllocationManager() throws IOException {
        loadAllocations();
    }

    public void allocateRoom(
            StudentManager studentManager,
            RoomManager roomManager,
            String studentId,
            String roomNumber) throws IOException {

        Student student = studentManager.findStudentById(studentId);

        if (student == null) {
            throw new IllegalArgumentException("Student ID does not exist.");
        }

        Room room = roomManager.findRoomByNumber(roomNumber);

        if (room == null) {
            throw new IllegalArgumentException("Room number does not exist.");
        }

        for (String[] allocation : allocations) {
            if (allocation[0].equalsIgnoreCase(studentId)) {
                throw new IllegalArgumentException(
                    "This student already has a room allocation."
                );
            }
        }

        int occupants = countRoomOccupants(roomNumber);

        if (occupants >= room.getCapacity()) {
            throw new IllegalArgumentException(
                "This room has reached its maximum capacity."
            );
        }

        allocations.add(new String[]{studentId, roomNumber});

        try {
            saveAllocations();

            String newStatus =
                occupants + 1 >= room.getCapacity()
                ? "Occupied"
                : "Available";

            roomManager.updateRoomStatus(roomNumber, newStatus);

        } catch (IOException e) {
            allocations.remove(allocations.size() - 1);

            try {
                saveAllocations();
            } catch (IOException rollbackError) {
                e.addSuppressed(rollbackError);
            }

            throw e;
        }

        System.out.println("\nRoom allocated successfully!");
        System.out.println("Student: " + student.getFullName());
        System.out.println("Student ID: " + student.getStudentId());
        System.out.println("Room Number: " + room.getRoomNumber());
        System.out.println("Room Type: " + room.getRoomType());
        System.out.println("Room Occupants: " + (occupants + 1));
        System.out.println("Room Capacity: " + room.getCapacity());
    }

    public void viewAllocations() {
        if (allocations.isEmpty()) {
            System.out.println("\nNo room allocations have been made.");
            return;
        }

        System.out.println("\n========== ROOM ALLOCATIONS ==========");

        for (String[] allocation : allocations) {
            System.out.println("----------------------------------");
            System.out.println("Student ID: " + allocation[0]);
            System.out.println("Room Number: " + allocation[1]);
        }

        System.out.println("----------------------------------");
        System.out.println("Total Allocations: " + allocations.size());
    }

    private int countRoomOccupants(String roomNumber) {
        int count = 0;

        for (String[] allocation : allocations) {
            if (allocation[1].equalsIgnoreCase(roomNumber)) {
                count++;
            }
        }

        return count;
    }

    private void loadAllocations() throws IOException {
        Files.createDirectories(filePath.getParent());

        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
            return;
        }

        List<String> records = Files.readAllLines(filePath);

        for (String record : records) {
            String[] fields = record.split("\\|", -1);

            if (fields.length == 2) {
                allocations.add(fields);
            }
        }
    }

    private void saveAllocations() throws IOException {
        List<String> records = new ArrayList<>();

        for (String[] allocation : allocations) {
            records.add(allocation[0] + "|" + allocation[1]);
        }

        Files.write(filePath, records);
    }
}
