package com.trafficy.controller;

import com.trafficy.service.DatabaseConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @Autowired
    private DatabaseConnectionService dbService;

    @GetMapping("/db")
    public ResponseEntity<String> checkDBConnection() {
        boolean isConnected = dbService.checkDatabaseConnection();
        return isConnected ? ResponseEntity.ok("Database Connected")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database Connection Failed");
    }
}