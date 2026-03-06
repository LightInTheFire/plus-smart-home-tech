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
import org.mapstruct.ValueMapping;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HubEventMapper {

    DeviceAddedEventAvro map(DeviceAddedEventProto proto);

    DeviceRemovedEventAvro map(DeviceRemovedEventProto proto);

    @Mapping(target = "conditions", source = "conditionList")
    @Mapping(target = "actions", source = "actionList")
    ScenarioAddedEventAvro map(ScenarioAddedEventProto proto);

    @Mapping(target = "value", expression = "java(mapConditionValue(proto))")
    ScenarioConditionAvro map(ScenarioConditionProto proto);

    @Mapping(target = "value", expression = "java(proto.hasValue() ? proto.getValue() : null)")
    DeviceActionAvro map(DeviceActionProto proto);

    @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.THROW_EXCEPTION)
    DeviceTypeAvro map(DeviceTypeProto proto);

    @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.THROW_EXCEPTION)
    ConditionTypeAvro map(ConditionTypeProto proto);

    @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.THROW_EXCEPTION)
    ConditionOperationAvro map(ConditionOperationProto proto);

    @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.THROW_EXCEPTION)
    ActionTypeAvro map(ActionTypeProto proto);

    ScenarioRemovedEventAvro map(ScenarioRemovedEventProto proto);

    @Mapping(target = "hubId", source = "hubId")
    @Mapping(target = "timestamp", expression = "java(mapTimestamp(proto))")
    @Mapping(target = "payload", expression = "java(mapPayload(proto))")
    HubEventAvro map(HubEventProto proto);

    default Instant mapTimestamp(HubEventProto proto) {
        return Instant.ofEpochSecond(
            proto.getTimestamp()
                .getSeconds(),
            proto.getTimestamp()
                .getNanos());
    }

    default Object mapPayload(HubEventProto proto) {
        Map<HubEventProto.PayloadCase, Supplier<Object>> handlers = Map.of(
            HubEventProto.PayloadCase.DEVICE_ADDED,
            () -> map(proto.getDeviceAdded()),
            HubEventProto.PayloadCase.DEVICE_REMOVED,
            () -> map(proto.getDeviceRemoved()),
            HubEventProto.PayloadCase.SCENARIO_ADDED,
            () -> map(proto.getScenarioAdded()),
            HubEventProto.PayloadCase.SCENARIO_REMOVED,
            () -> map(proto.getScenarioRemoved()));

        return Optional.ofNullable(handlers.get(proto.getPayloadCase()))
            .map(Supplier::get)
            .orElseThrow(() -> new IllegalArgumentException("Unknown payload type: " + proto.getPayloadCase()));
    }

    default Object mapConditionValue(ScenarioConditionProto proto) {
        return switch (proto.getValueCase()) {
            case BOOL_VALUE -> proto.getBoolValue();
            case INT_VALUE -> proto.getIntValue();
            default -> null;
        };
    }
}
