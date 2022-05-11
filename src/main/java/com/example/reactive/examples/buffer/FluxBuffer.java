package com.example.reactive.examples.buffer;

public class FluxBuffer {




    /*
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
     */
}
