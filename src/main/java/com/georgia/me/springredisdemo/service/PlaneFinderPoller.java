package com.georgia.me.springredisdemo.service;

import com.georgia.me.springredisdemo.dto.Aircraft;
import com.georgia.me.springredisdemo.repository.AircraftRepository;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@EnableScheduling
@Component
public class PlaneFinderPoller {
    private WebClient webClient = WebClient.create("http://localhost:7636/aircraft");

    private final RedisConnectionFactory connectionFactory;
    private final AircraftRepository aircraftRepository;

    public PlaneFinderPoller(RedisConnectionFactory connectionFactory, AircraftRepository aircraftRepository) {
        this.connectionFactory = connectionFactory;
        this.aircraftRepository = aircraftRepository;
    }

    @Scheduled(fixedRate = 1000)
    private void pollPlanes() {
        connectionFactory.getConnection().serverCommands().flushDb();

        webClient
                .get()
                .retrieve()
                .bodyToFlux(Aircraft.class)
                .filter(plane -> !plane.getReg().isEmpty())
                .toStream()
                .forEach(aircraftRepository::save);

        aircraftRepository.findAll().forEach(System.out::println);
    }
}
