# Flux Buffering Example

- Sometimes you need the **whole** result of one stream as input for another one
- *Native reactive solution:* stream the partial results from first stream into the next one [`map`] (only suitable if the database is capable of streaming and executing the queries separately is faster)
- Blocking calls are not allowed
- ...therefore we need to buffer


## Arbitrary example in `BufferExampleRepository`
Buffering the result of the first query to get the complete result list:
```
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
```

Pass the list as input for the next stream:
```
return getComparisonIdsByGroupingOnVehicleType(ssoUserId)
    .flatMap(this::getComparisonsByIds);
```

*Note:* Subselect is also an option but we had some (performance) issues with that approach on our Oracle database.


### Example Request
```
curl -XGET -H "Content-type: application/json" 'http://localhost:8080/example/buffer/12345'
```


## Concrete example (Activity API)

```
public Flux<PkwVergleich> getAllBySsoUserIdAndVisibility(final Long ssoUserId, final Visibility visibility) {
    return getVergleicheIdsByGroupingOnPkwTyp(ssoUserId, visibility)
        .flatMap(this::getVergleicheForIds);
}

private Flux<List<Long>> getVergleicheIdsByGroupingOnPkwTyp(final Long ssoUserId, final Visibility visibility) {
    final String sql = """
        SELECT MAX(kd.id) AS MAXID
          FROM KFZ_DATEN kd
         WHERE kd.SSOUSER_USERID = :ssoUserId
           AND kd.HIDDEN != 1
           AND kd.VERSION >= :version
           AND kd.VISIBILITYSTATE IN (:visibilityStates)
         GROUP BY kd.PKW_TYPID
         ORDER BY MAX(kd.id) DESC
         FETCH NEXT 999 ROWS ONLY;
        """;
    return databaseClient.sql(sql)
        .bind("ssoUserId", ssoUserId)
        .bind("version", PersistenceUtil.SEVEN_YEARS_AGO)
        .bind("visibilityStates", VisibilityStateUtil.assembleDesiredVisibilityStates(visibility))
        .map(MAXID_MAPPER::apply)
        .all()
        .filter(Objects::nonNull)
        .buffer();
}

private Flux<PkwVergleich> getVergleicheForIds(final List<Long> vergleicheIds) {
    var query = dslContext
        .select(
            PkwVergleichDbo.ID.aliased(),
            PkwVergleichDbo.VERSION.aliased(),
            PkwVergleichDbo.PKW_TYPID.aliased(),
            PkwVergleichDbo.ZAHLWEISE.aliased(),
            PkwVergleichDbo.SSOUSER_USERID.aliased(),
            PkwVergleichDbo.HIDDEN.aliased(),
            PkwVergleichDbo.VISIBILITYSTATE.aliased(),
            PkwVergleichDbo.ANZAHLERGEBNISSE.aliased(),
            PkwVergleichDbo.DEVICEOUTPUT.aliased(),
            PkwVergleichDbo.VERSICHERUNGSBEGINN.aliased(),
            PkwVergleichDbo.CRAWLTIME.aliased(),
            PkwVergleichDbo.KFZ_KASKOTYPID.aliased(),
            PkwVergleichDbo.CSCODE.aliased()
        )
        .from(PkwVergleichDbo.TABLE)
        .where(PkwVergleichDbo.ID.field().in(vergleicheIds));
    return databaseClient
        .sql(query.getSQL(ParamType.INLINED))
        .map(pkwVergleichMapper::apply)
        .all();
}
```
