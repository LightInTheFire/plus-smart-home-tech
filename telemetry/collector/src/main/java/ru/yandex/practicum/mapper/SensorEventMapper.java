package ru.yandex.practicum.mapper;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SensorEventMapper {

    ClimateSensorAvro map(ClimateSensorProto proto);

    LightSensorAvro map(LightSensorProto proto);

    MotionSensorAvro map(MotionSensorProto proto);

    SwitchSensorAvro map(SwitchSensorProto proto);

    @Mapping(target = "timestamp", expression = "java(mapTimestamp(proto))")
    TemperatureSensorAvro map(SensorEventProto proto, TemperatureSensorProto sensor);

    @Mapping(target = "timestamp", expression = "java(mapTimestamp(proto))")
    @Mapping(target = "payload", expression = "java(mapPayload(proto))")
    SensorEventAvro map(SensorEventProto proto);

    default Instant mapTimestamp(SensorEventProto proto) {
        return Instant.ofEpochSecond(
            proto.getTimestamp()
                .getSeconds(),
            proto.getTimestamp()
                .getNanos());
    }

    default Object mapPayload(SensorEventProto proto) {
        Map<SensorEventProto.PayloadCase, Supplier<Object>> handlers = Map.of(
            SensorEventProto.PayloadCase.CLIMATE_SENSOR,
            () -> map(proto.getClimateSensor()),
            SensorEventProto.PayloadCase.LIGHT_SENSOR,
            () -> map(proto.getLightSensor()),
            SensorEventProto.PayloadCase.MOTION_SENSOR,
            () -> map(proto.getMotionSensor()),
            SensorEventProto.PayloadCase.SWITCH_SENSOR,
            () -> map(proto.getSwitchSensor()),
            SensorEventProto.PayloadCase.TEMPERATURE_SENSOR,
            () -> map(proto, proto.getTemperatureSensor()));
        return Optional.ofNullable(handlers.get(proto.getPayloadCase()))
            .map(Supplier::get)
            .orElseThrow(() -> new IllegalArgumentException("Unknown payload type: " + proto.getPayloadCase()));
    }
}
