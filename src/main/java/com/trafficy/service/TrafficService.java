package com.trafficy.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TrafficService {

    RestTemplate restTemplate;

    public TrafficService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }



}
