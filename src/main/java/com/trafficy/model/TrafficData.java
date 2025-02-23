package com.trafficy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class TrafficData {

    @Id
    private LocalDateTime timestamp;  // Primary Key

    private double latitude;
    private double longitude;
    private double speed;
    private String congestionLevel;
}
