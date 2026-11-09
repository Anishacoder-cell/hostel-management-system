
package com.hostel.service;

import com.hostel.model.Room;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RoomManager {
    private final ArrayList<Room> rooms = new ArrayList<>();
    private final Path filePath = Path.of("data", "rooms.txt");

    public RoomManager() throws IOException {
        loadRooms();
    }

    public void addRoom(String roomNumber, String roomType,
                        int capacity, double pricePerMonth) throws IOException {

        for (Room room : rooms) {
            if (room.getRoomNumber().equalsIgnoreCase(roomNumber)) {
                throw new IllegalArgumentException(
                    "A room with this number already exists."
                );
            }
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                "Room capacity must be greater than zero."
            );
        }

        if (pricePerMonth <= 0) {
            throw new IllegalArgumentException(
                "Monthly price must be greater than zero."
            );
        }

        Room room = new Room(
            clean(roomNumber),
            clean(roomType),
            capacity,
            pricePerMonth,
            "Available"
        );

        rooms.add(room);

        try {
            saveRooms();
        } catch (IOException e) {
            rooms.remove(room);
            throw e;
        }

        System.out.println("\nRoom added successfully!");
        System.out.println(room);
    }

    public void viewAllRooms() {
        if (rooms.isEmpty()) {
            System.out.println("\nNo rooms have been registered yet.");
            return;
        }

        System.out.println("\n========== ALL HOSTEL ROOMS ==========");

        for (Room room : rooms) {
            System.out.println("----------------------------------");
            System.out.println(room);
        }

        System.out.println("----------------------------------");
        System.out.println("Total Rooms: " + rooms.size());
    }

    public void viewAvailableRooms() {
        int availableCount = 0;

        System.out.println("\n========== AVAILABLE ROOMS ==========");

        for (Room room : rooms) {
            if (room.getStatus().equalsIgnoreCase("Available")) {
                System.out.println("----------------------------------");
                System.out.println(room);
                availableCount++;
            }
        }

        if (availableCount == 0) {
            System.out.println("No available rooms at the moment.");
        } else {
            System.out.println("----------------------------------");
            System.out.println("Available Rooms: " + availableCount);
        }
    }
public Room findRoomByNumber(String roomNumber) {
    for (Room room : rooms) {
        if (room.getRoomNumber().equalsIgnoreCase(roomNumber)) {
            return room;
        }
    }
    return null;
}

public void updateRoomStatus(String roomNumber, String status)
        throws IOException {

    Room room = findRoomByNumber(roomNumber);

    if (room == null) {
        throw new IllegalArgumentException("Room not found.");
    }

    String previousStatus = room.getStatus();
    room.setStatus(status);

    try {
        saveRooms();
    } catch (IOException e) {
        room.setStatus(previousStatus);
        throw e;
    }
}

    private void loadRooms() throws IOException {
        Files.createDirectories(filePath.getParent());

        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
            return;
        }

        List<String> records = Files.readAllLines(filePath);

        for (String record : records) {
            String[] fields = record.split("\\|", -1);

            if (fields.length != 5) {
                continue;
            }

            try {
                Room room = new Room(
                    fields[0],
                    fields[1],
                    Integer.parseInt(fields[2]),
                    Double.parseDouble(fields[3]),
                    fields[4]
                );

                rooms.add(room);
            } catch (NumberFormatException e) {
                System.out.println("Warning: An invalid room record was skipped.");
            }
        }
    }

    private void saveRooms() throws IOException {
        List<String> records = new ArrayList<>();

        for (Room room : rooms) {
            String record = room.getRoomNumber() + "|" +
                room.getRoomType() + "|" +
                room.getCapacity() + "|" +
                room.getPricePerMonth() + "|" +
                room.getStatus();

            records.add(record);
        }

        Files.write(filePath, records);
    }

    private String clean(String value) {
        return value.replace("|", "/")
                    .replace("\n", " ")
                    .replace("\r", " ");
    }
}
