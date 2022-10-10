package ru.vadim.game.repository;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.util.Logger;
import reactor.util.Loggers;
import ru.vadim.game.model.Clan;
import ru.vadim.game.model.History;

import java.rmi.server.UID;
import java.time.LocalDate;
import java.util.UUID;
import java.util.logging.Level;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClanRepository {

    private final ConnectionFactory connectionFactory;
    private final DatabaseClient databaseClient;
    private final HistoryRepositiry historyRepositiry;

    public Mono<Clan> findById(long id) {
        return Mono.from(connectionFactory.create())
                .flatMap(con -> Mono.from(con.createStatement("SELECT id, name, gold FROM Clan WHERE id = $1")
                                .bind("$1", id)
                                .execute())
                        .doFinally((st) -> close(con)))
                .map(result -> result.map((row, meta) -> new Clan(row.get("id", Long.class),
                        row.get("name", String.class),
                        row.get("gold", Integer.class))))
                .flatMap(p -> Mono.from(p));

    }

    public Flux<Clan> createNewClan(Clan clan) {
        return Flux.from(connectionFactory.create())
                .flatMap(con -> Mono.from(con.beginTransaction())
                        .then(Mono.from(con
                                        .createStatement("insert into Clan(name, gold) values($1,$2)")
                                .bind("$1", clan.getName())
                                .bind("$2", clan.getGold())
                                .returnGeneratedValues("id")
                                .execute()))
                        .map(result -> result.map((row, meta) ->
                                new Clan(row.get("id", Long.class),
                                        clan.getName(),
                                        clan.getGold())))
                        .flatMap(pub -> Mono.from(pub))
                        .delayUntil(r -> con.commitTransaction())
                        .doFinally((st) -> {
                            con.close();
                            History history = new History(clan.getName(), false, false, clan.getGold(), LocalDate.now());
                            historyRepositiry.saveData(history).subscribe();
                        }));

    }

    public Flux<Clan> findAll() {
        return Mono.from(connectionFactory.create())
                .flatMap((connection) -> Mono.from(connection.createStatement("select id, name, gold from Clan")
                                .execute())
                        .doFinally((st) -> close(connection)))
                .flatMapMany(result -> Flux.from(result.map((row, meta) -> {
                    Clan clan = new Clan(
                            row.get("id", Long.class),
                            row.get("name", String.class),
                            row.get("gold", Integer.class)

                    );
                    return clan;
                })));
    }

    public Flux<Clan> plusUpdate(Clan clan) {
        return Flux.from(connectionFactory.create())
                .flatMap(con -> Mono.from(con.beginTransaction())
                        .then(Mono.from(con
                                .createStatement("update clan set gold = gold + $1 where id = $2")
                                .bind("$1", clan.getGold())
                                .bind("$2", clan.getId())
                                .returnGeneratedValues("id")
                                .execute()))
                        .map(result -> result.map((row, meta) ->
                                new Clan(row.get("id", Long.class),
                                        clan.getName())))
                        .flatMap(pub -> Mono.from(pub))

                        .log("CLAN",
                                Level.FINEST,
                                SignalType.ON_SUBSCRIBE, SignalType.ON_NEXT, SignalType.ON_COMPLETE)
                        .delayUntil(r -> con.commitTransaction())
                        .doFinally((st) -> {
                            con.close();
                            History history = new History(clan.getName(), true, false, clan.getGold(), LocalDate.now());
                            historyRepositiry.saveData(history).subscribe();
                        }));
    }

    public Flux<Clan> minusUpdate(Clan clan) {
        return Flux.from(connectionFactory.create())
                .flatMap(con -> Mono.from(con.beginTransaction())
                        .then(Mono.from(con
                                .createStatement("update clan set gold = gold - $1 where id = $2")
                                .bind("$1", clan.getGold())
                                .bind("$2", clan.getId())
                                .returnGeneratedValues("id")
                                .execute()))
                        .map(result -> result.map((row, meta) ->
                                new Clan(row.get("id", Long.class),
                                        clan.getName())))
                        .flatMap(pub -> Mono.from(pub))

                        .log("CLAN",
                                Level.FINEST,
                                SignalType.ON_SUBSCRIBE, SignalType.ON_NEXT, SignalType.ON_COMPLETE)
                        .delayUntil(r -> con.commitTransaction())
                        .doFinally((st) -> {
                            con.close();
                            History history = new History(clan.getName(), true, false, clan.getGold(), LocalDate.now());
                            historyRepositiry.saveData(history).subscribe();
                        }));
    }

    private <T> Mono<T> close(Connection connection) {
        return Mono.from(connection.close())
                .then(Mono.empty());
    }
}
