package com.trafficy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trafficy.model.TrafficData;
import com.trafficy.repository.TrafficDataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrafficService {

    private static final int MAX_DAILY_REQUEST = 1800;
    private static final double MIN_LAT = 12.85, MAX_LAT = 13.05;
    private static final double MIN_LON = 77.65, MAX_LON = 77.80;
    private static final double GRID_SIZE = 0.025;
    private static final int REQUEST_INTERVAL = (24*60*60*1000)/ MAX_DAILY_REQUEST;

    @Value("api.tom-tom.api.key")
    private static String API_KEY;

    private final TrafficDataRepository repository;

    private final RestTemplate restTemplate;

    private final List<double[]> locations;
    private int currentInd = 0;

    public TrafficService(TrafficDataRepository repository, RestTemplate restTemplate){
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.locations = generateGrid();

    }

    private List<double[]> generateGrid(){
        List<double[]> points = new ArrayList<>();
        for(double lat = MIN_LAT; lat <= MAX_LAT; lat += GRID_SIZE){
            for(double lon = MIN_LON; lon <= MAX_LON; lon += GRID_SIZE){
                points.add(new double[]{lat,lon});
            }
        }

        return points;

    }

    @Scheduled(fixedRate = REQUEST_INTERVAL)
    public void fetchTrafficDataForNextLocation(){
        if(currentInd >= locations.size()){
            currentInd = 0;
        }

        double[] coords = locations.get(currentInd++);
        fetchAndSaveTrafficData(coords[0], coords[1]);
    }

    private void fetchAndSaveTrafficData(double lat, double lon){
        try {
            URL url = new URL("https://api.tomtom.com/traffic/services/4/flowSegmentData/absolute/10/json?key=LQWmkFr0KX4wS8wRojhZNUwHK4laVj9x&point=12.9716,77.5946");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            TrafficData data = parseTrafficData(response.toString(), lat, lon);

            if(data!= null){
                repository.save(data);
            }

            System.out.println("Data saved to DB");

        } catch (Exception e) {
            System.out.println("API call failed: "+ e.getMessage());
        }
    }

    private TrafficData parseTrafficData(String response, double lat, double lon) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode root = mapper.readTree(response);
            JsonNode flowSegmentData = root.get("flowSegmentData");

            if(flowSegmentData != null){
                double currentSpeed = flowSegmentData.get("currentSpeed").asDouble();
                double freeFlowSpeed = flowSegmentData.get("freeFlowSpeed").asDouble();
                double congestionPercent = (1 - (currentSpeed/freeFlowSpeed))*100;

                TrafficData data = new TrafficData();
                data.setTimestamp(LocalDateTime.now());
                data.setLongitude(lon);
                data.setLatitude(lat);
                data.setSpeed(currentSpeed);
                data.setCongestionLevel(String.format("%.2f%%", congestionPercent));
                return data;
            }
        } catch (JsonProcessingException e) {
            System.out.println("Error parsing JSON: " + e.getMessage());
        }

        return null;
    }


}
