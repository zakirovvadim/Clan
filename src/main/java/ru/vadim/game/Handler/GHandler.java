package ru.vadim.game.Handler;

import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vadim.game.model.Clan;

@Component
public class GHandler {
    public Mono<ServerResponse> hello(ServerRequest request) {
        Clan clan = new Clan(4,"Vadim", 240);
        Flux<Clan> data = Flux.just(clan);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(data, Clan.class);
    }
}
