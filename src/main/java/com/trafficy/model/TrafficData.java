package com.trafficy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@ToString
public class TrafficData {

    @Id
    private LocalDateTime timestamp;  // Primary Key

    private double latitude;
    private double longitude;
    private double speed;
    private String congestionLevel;
}
