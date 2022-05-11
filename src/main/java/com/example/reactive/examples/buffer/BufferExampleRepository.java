package com.example.reactive.examples.buffer;

import com.example.reactive.common.mapper.ComparisonMapper;
import com.example.reactive.common.model.Comparison;
import io.r2dbc.spi.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

@Repository
class BufferExampleRepository {

    private final ComparisonMapper comparisonMapper;
    private final DatabaseClient databaseClient;

    @Autowired
    BufferExampleRepository(
        final ComparisonMapper comparisonMapper,
        final DatabaseClient databaseClient
    ) {
        this.comparisonMapper = comparisonMapper;
        this.databaseClient = databaseClient;
    }

    public Flux<Comparison> getComparisonsBySsoUserId(final Long ssoUserId) {
        return getComparisonIdsByGroupingOnVehicleType(ssoUserId)
            .flatMap(this::getComparisonsByIds);
    }

    private Flux<List<Long>> getComparisonIdsByGroupingOnVehicleType(final Long ssoUserId) {
        final String sql = """
            SELECT MAX(id) AS MAXID
              FROM comparison
             WHERE ssoUserId = :ssoUserId
             GROUP BY vehicleType
             ORDER BY MAX(id) DESC
             FETCH NEXT 999 ROWS ONLY;
            """;
        return databaseClient.sql(sql)
            .bind("ssoUserId", ssoUserId)
            .map(MAXID_MAPPER::apply)
            .all()
            .filter(Objects::nonNull)
            .buffer();
    }

    private Flux<Comparison> getComparisonsByIds(final List<Long> ids) {
        final String sql = """
            SELECT *
              FROM comparison
             WHERE id IN (:ids);
            """;
        return databaseClient.sql(sql)
            .bind("ids", ids)
            .map(comparisonMapper::apply)
            .all();
    }

    private static final BiFunction<Row, Object, Long> MAXID_MAPPER = (row, o) -> row.get("MAXID", Long.class);
}
