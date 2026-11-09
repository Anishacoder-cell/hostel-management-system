
package com.hostel.model;

public class Room {
    private String roomNumber;
    private String roomType;
    private int capacity;
    private double pricePerMonth;
    private String status;

    public Room(String roomNumber, String roomType,
                int capacity, double pricePerMonth, String status) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.pricePerMonth = pricePerMonth;
        this.status = status;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getPricePerMonth() {
        return pricePerMonth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Room Number: " + roomNumber +
               "\nRoom Type: " + roomType +
               "\nCapacity: " + capacity +
               "\nMonthly Price: UGX " + String.format("%,.2f", pricePerMonth) +
               "\nStatus: " + status;
    }
}
