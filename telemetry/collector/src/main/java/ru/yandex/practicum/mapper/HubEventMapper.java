package ru.yandex.practicum.mapper;

import java.util.stream.Collectors;

import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.hub.*;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HubEventMapper {

    public HubEventAvro toAvro(HubEvent event) {
        HubEventAvro.Builder builder = HubEventAvro.newBuilder()
            .setHubId(event.hubId())
            .setTimestamp(event.timestamp());

        builder.setPayload(switch (event) {
            case DeviceAddedEvent d -> DeviceAddedEventAvro.newBuilder()
                .setId(d.id())
                .setType(
                    DeviceTypeAvro.valueOf(
                        d.deviceType()
                            .name()))
                .build();
            case DeviceRemovedEvent d -> DeviceRemovedEventAvro.newBuilder()
                .setId(d.id())
                .build();
            case ScenarioAddedEvent s -> ScenarioAddedEventAvro.newBuilder()
                .setName(s.name())
                .setConditions(
                    s.conditions()
                        .stream()
                        .map(
                            c -> ScenarioConditionAvro.newBuilder()
                                .setSensorId(c.sensorId())
                                .setType(
                                    ConditionTypeAvro.valueOf(
                                        c.type()
                                            .name()))
                                .setOperation(
                                    ConditionOperationAvro.valueOf(
                                        c.operation()
                                            .name()))
                                .setValue(c.value() == null ? null : c.value())
                                .build())
                        .collect(Collectors.toList()))
                .setActions(
                    s.actions()
                        .stream()
                        .map(
                            a -> DeviceActionAvro.newBuilder()
                                .setSensorId(a.sensorId())
                                .setType(
                                    ActionTypeAvro.valueOf(
                                        a.type()
                                            .name()))
                                .setValue(a.value())
                                .build())
                        .collect(Collectors.toList()))
                .build();
            case ScenarioRemovedEvent s -> ScenarioRemovedEventAvro.newBuilder()
                .setName(s.name())
                .build();
        });

        return builder.build();
    }
}
