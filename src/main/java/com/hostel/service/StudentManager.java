
package com.hostel.service;

import com.hostel.model.Student;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class StudentManager {
    private final ArrayList<Student> students = new ArrayList<>();
    private final Path filePath = Path.of("data", "students.txt");
    private int nextId = 1;

    public StudentManager() throws IOException {
        loadStudents();
    }

    public Student registerStudent(String fullName, String email,
                                   String phone, String course) throws IOException {
        for (Student student : students) {
            if (student.getEmail().equalsIgnoreCase(email)) {
                throw new IllegalArgumentException(
                    "A student with this email already exists."
                );
            }
        }

        String studentId = String.format("STU%03d", nextId);
        Student student = new Student(studentId, clean(fullName),
                clean(email), clean(phone), clean(course));

        students.add(student);

        try {
            saveStudents();
        } catch (IOException e) {
            students.remove(student);
            throw e;
        }

        nextId++;
        return student;
    }
public Student findStudentById(String studentId) {
    for (Student student : students) {
        if (student.getStudentId().equalsIgnoreCase(studentId)) {
            return student;
        }
    }
    return null;
}

    public void viewStudents() {
        if (students.isEmpty()) {
            System.out.println("\nNo students have been registered yet.");
            return;
        }

        System.out.println("\n========== REGISTERED STUDENTS ==========");

        for (Student student : students) {
            System.out.println("----------------------------------");
            System.out.println(student);
        }

        System.out.println("----------------------------------");
        System.out.println("Total Students: " + students.size());
    }

    private void loadStudents() throws IOException {
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

            Student student = new Student(
                fields[0], fields[1], fields[2], fields[3], fields[4]
            );

            students.add(student);

            try {
                int id = Integer.parseInt(
                    student.getStudentId().substring(3)
                );
                nextId = Math.max(nextId, id + 1);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.out.println("Warning: Invalid student ID found.");
            }
        }
    }

    private void saveStudents() throws IOException {
        List<String> records = new ArrayList<>();

        for (Student student : students) {
            String record = student.getStudentId() + "|" +
                student.getFullName() + "|" +
                student.getEmail() + "|" +
                student.getPhone() + "|" +
                student.getCourse();

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
