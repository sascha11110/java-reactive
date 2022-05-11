package com.example.reactive.common.mapper;

import com.example.reactive.common.model.Comparison;
import io.r2dbc.spi.Row;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Component
public class ComparisonMapper implements BiFunction<Row, Object, Comparison> {

    @Override
    public Comparison apply(final Row row, final Object o) {
        return Comparison.create()
                .id(row.get("ID", Long.class))
                .created(row.get("CREATED", LocalDateTime.class))
                .ssoUserId(row.get("SSOUSERID", Long.class))
                .vehicleType(row.get("VEHICLETYPE", String.class))
                .build();
    }
}
