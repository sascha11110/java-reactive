# Flux Combine Example

- Reactive / functional data structures and operations on the are more complex (add least for *traditional* Java developers)
- in ordinary sequential programming one always has the concrete value available and can combine it very easily with another value (e.g. calling a setter of an object)
- in reactive programming one mostly works with a publisher and not with the concrete value
- Some operations on `Mono` and `Flux`: `map`, `flatMap`, `zipWith`, `concatMap`, ...


## Arbitrary example
tbd


## Concrete example 
1. Get all `PkwFahrzeugTyp` entities from database (`pkwFahrzeugTypRepository.getAll()`) 
2. Create a string representation of the car's name (`getOberbaureihenHerstellerNamen()`)
3. Combine it via `zipWith` with one of the previous results (``)
   - we need to artificially create a publisher for the input variable via `Mono.just(pkwFahrzeugTypResult)`
   - the result is a `Flux` of tuples of `PkwFahrzeugTypResult` and `String`
5. Map it to the desired `PkwFahrzeugTyp` entity

```
public Flux<PkwFahrzeugTyp> getAllWithOberbaureihenWithHersteller() {
    return pkwFahrzeugTypRepository.getAll()
        .flatMap(this::combineWithOberbaureihenHerstellerNamen)
        .map(this::map);
}

private Mono<Tuple2<PkwFahrzeugTypResult, String>> combineWithOberbaureihenHerstellerNamen(final PkwFahrzeugTypResult pkwFahrzeugTypResult) {
    return Mono.just(pkwFahrzeugTypResult)
        .zipWith(getOberbaureihenHerstellerNamen(pkwFahrzeugTypResult.id()));
}

private Mono<String> getOberbaureihenHerstellerNamen(final Long pkwTypId) {
    return pkwOberbaureiheRepository.getAllByPkwTypIdWithHersteller(pkwTypId)
        .distinct(PkwOberbaureiheResult::herstellerId)
        .reduce("", JOINER);
}

private PkwFahrzeugTyp map(final Tuple2<PkwFahrzeugTypResult, String> combination) {
    ...
}
```
