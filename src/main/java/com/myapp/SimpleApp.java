package com.myapp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimpleApp {
    public static void main(String[] args) {
        // Create a formatter for the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        
        System.out.println("----------------------------------------");
        System.out.println("Hello, Jenkins! This Java app was built successfully.");
        System.out.println("Current Server Time: " + timestamp);
        System.out.println("----------------------------------------");
    }
}
