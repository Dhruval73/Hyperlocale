package com.trafficy.service;

import com.trafficy.repository.TrafficDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseConnectionService {

    @Autowired
    private TrafficDataRepository repository;

    public boolean checkDatabaseConnection() {
        try {
            repository.count();  // Simple query to check connection
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}