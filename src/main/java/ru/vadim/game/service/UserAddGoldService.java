package ru.vadim.game.service;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Result;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.vadim.game.model.Clan;

@Service
@RequiredArgsConstructor
public class UserAddGoldService {

    private final ClanService clans;

//    public Mono<Clan> addGoldToClan(long userId, long clanId, int gold) {
//        Clan clan = clans.getClan(clanId).blockOptional().orElseThrow(() -> new RuntimeException(String.format("Clan with %s isnt exist", clanId)));
//        clan.setGold(clan.getGold() + gold);
//        return clans.save(clan);
//    }
//
//    public static Publisher<Clan> queryR2dbc(Connection connection, Clan clan) {
//        return Mono.from(connection.createStatement(
//                "INSERT INTO Clan(id, name, gold) VALUES ($1, $2, $2)")
//                .bind("$1", clan.getId())
//                .bind("$2", clan.getName())
//                .bind("$2", clan.getGold())
//                .execute())
//                .flatMap(el -> el.map())
//                ;
//    }
}
