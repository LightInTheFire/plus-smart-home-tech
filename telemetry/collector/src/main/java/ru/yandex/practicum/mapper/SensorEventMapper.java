package ru.yandex.practicum.mapper;

import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.sensor.*;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SensorEventMapper {
    public SensorEventAvro toAvro(SensorEvent event) {
        SensorEventAvro.Builder builder =
                SensorEventAvro.newBuilder()
                        .setId(event.id())
                        .setHubId(event.hubId())
                        .setTimestamp(event.timestamp());

        builder.setPayload(
                switch (event) {
                    case ClimateSensorEvent e ->
                            ClimateSensorAvro.newBuilder()
                                    .setTemperatureC(e.temperatureC())
                                    .setHumidity(e.humidity())
                                    .setCo2Level(e.co2Level())
                                    .build();
                    case LightSensorEvent e ->
                            LightSensorAvro.newBuilder()
                                    .setLinkQuality(e.linkQuality())
                                    .setLuminosity(e.luminosity())
                                    .build();
                    case MotionSensorEvent e ->
                            MotionSensorAvro.newBuilder()
                                    .setLinkQuality(e.linkQuality())
                                    .setMotion(e.motion())
                                    .setVoltage(e.voltage())
                                    .build();
                    case SwitchSensorEvent e ->
                            SwitchSensorAvro.newBuilder().setState(e.state()).build();
                    case TemperatureSensorEvent e ->
                            TemperatureSensorAvro.newBuilder()
                                    .setId(e.id())
                                    .setHubId(e.hubId())
                                    .setTimestamp(e.timestamp())
                                    .setTemperatureC(e.temperatureC())
                                    .setTemperatureF(e.temperatureF())
                                    .build();
                });

        return builder.build();
    }
}
