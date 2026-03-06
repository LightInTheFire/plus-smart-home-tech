package ru.yandex.practicum.util;

import java.time.Instant;

import com.google.protobuf.Timestamp;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Converter {

    public long timestampToMillis(Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos())
            .toEpochMilli();
    }
}
