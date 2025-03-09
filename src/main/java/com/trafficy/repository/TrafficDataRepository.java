package com.trafficy.repository;

import com.trafficy.model.TrafficData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Repository
public interface TrafficDataRepository extends JpaRepository<TrafficData, LocalDateTime> {

    Logger logger = LoggerFactory.getLogger(TrafficDataRepository.class);

}
