package ru.vadim.game.repository;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vadim.game.model.Clan;
import ru.vadim.game.model.History;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class HistoryRepositiry {
    private final ConnectionFactory connectionFactory;
    private final DatabaseClient databaseClient;

    public Mono<History> saveData(History historyData) {
        System.out.println(historyData.toString());
        return Mono.from(connectionFactory.create())
                .flatMap(con -> Mono.from(con.beginTransaction())
                        .then(Mono.from(con.createStatement("insert into History(name, mns, pls, count, date) " +
                                        "values($1,$2,$3,$4,$5)")
                                .bind("$1", historyData.getName())
                                .bind("$2", historyData.getMns())
                                .bind("$3", historyData.getPls())
                                .bind("$4", historyData.getCount())
                                .bind("$5", historyData.getDate())
                                .returnGeneratedValues("id")
                                .execute()))
                        .map(result -> result.map((row, meta) ->
                                new History(row.get("id", Long.class), historyData.getName(),false, false, historyData.getGold(), LocalDate.now())))
                        .flatMap(pub -> Mono.from(pub))
                        .delayUntil(r -> con.commitTransaction())
                        .doOnError(throwable -> System.out.println(throwable.getMessage()))
                        .doFinally((st) -> con.close()));
    }

    public Flux<History> findAll() {
        return Mono.from(connectionFactory.create())
                .flatMap((connection) -> Mono.from(connection.createStatement("select * from History")
                                .execute())
                        .doFinally((st) -> close(connection)))
                .flatMapMany(result -> Flux.from(result.map((row, meta) -> {
                    History history = new History(
                            row.get("id", Long.class),
                            row.get("name", String.class),
                            row.get("mns", Boolean.class),
                            row.get("pls", Boolean.class),
                            row.get("count", Integer.class),
                            row.get("date", LocalDate.class)
                    );
                    return history;
                })));
    }

    private <T> Mono<T> close(Connection connection) {
        return Mono.from(connection.close())
                .then(Mono.empty());
    }
}
