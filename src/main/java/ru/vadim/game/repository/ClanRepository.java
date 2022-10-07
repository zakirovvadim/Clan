package ru.vadim.game.repository;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vadim.game.model.Clan;

@Service
@RequiredArgsConstructor
public class ClanRepository {

    private final ConnectionFactory connectionFactory;
    private final Repo repo;
    private final DatabaseClient databaseClient;

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

    public Mono<Clan> createNewClan(Clan clan) {
        return Mono.from(connectionFactory.create())
                .flatMap(con -> Mono.from(con.beginTransaction())
                        .then(Mono.from(con.createStatement("insert into Clan(name, gold) values($1,$2)")
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
                        .doFinally((st) -> con.close()));

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

//    public Mono<Clan> updateGoldBalance(long id, Clan clan) {
//        return repo.findById(id)
//                .map(clann -> {
//            clann.setGold(clann.getGold() + clan.getGold());
//            return clann;
//        })
//                .flatMap(clann -> repo.save(clann).map(Clan::new));
//    }

    public Mono<Integer> update(Clan p) {
        System.out.println("Repository p " + p);
        System.out.println(databaseClient);
        return this.databaseClient.sql("UPDATE Clan set gold = :gold WHERE id = :id")
                .bind("gold", p.getGold())
                .bind("id", p.getId())
                .fetch()
                .rowsUpdated()
                .doOnError(throwable -> System.out.println(throwable.getMessage()));
    }

    private <T> Mono<T> close(Connection connection) {
        return Mono.from(connection.close())
                .then(Mono.empty());
    }
}
