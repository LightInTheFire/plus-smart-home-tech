package ru.yandex.practicum.controller;

import jakarta.validation.Valid;

import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.model.sensor.SensorEvent;
import ru.yandex.practicum.service.EventService;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Controller
@ResponseBody
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/sensors")
    public void saveSensorEvent(@Valid @RequestBody SensorEvent sensorEvent) {
        log.info("Saving sensor event {}", sensorEvent);
        eventService.publishSensorEvent(sensorEvent);
    }

    @PostMapping("/hubs")
    public void saveHubEvent(@Valid @RequestBody HubEvent hubEvent) {
        log.info("Saving hub event {}", hubEvent.toString());
        eventService.publishHubEvent(hubEvent);
    }
}
