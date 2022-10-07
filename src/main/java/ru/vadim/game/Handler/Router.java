package ru.vadim.game.Handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import ru.vadim.game.model.Clan;


@Configuration
public class Router {

    @Bean
    public RouterFunction<ServerResponse> route(GHandler handler) {
        return RouterFunctions.route(RequestPredicates.GET("/test")
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::hello);
    }
}
