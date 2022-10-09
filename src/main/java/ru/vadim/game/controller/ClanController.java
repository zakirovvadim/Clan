package ru.vadim.game.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vadim.game.model.Clan;
import ru.vadim.game.service.ClanService;

@Controller
@RequiredArgsConstructor
public class ClanController {

    private final ClanService clanService;

    @PostMapping("/clan")
    public Mono<ResponseEntity<Clan>> postAccount(@RequestBody Clan clan) {
        return clanService.createNewClan(clan)
                .map(acc -> new ResponseEntity<>(acc, HttpStatus.CREATED))
                .log();
    }

    @GetMapping("/clan/{id}")
    public Mono<ResponseEntity<Clan>> getAccount(@PathVariable("id") Long id) {
        return clanService.findById(id)
                .map(acc -> new ResponseEntity<>(acc, HttpStatus.OK))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(null, HttpStatus.NOT_FOUND)));
    }

    @GetMapping("/clans")
    public Flux<Clan> getAllAccounts() {
        return clanService.findAll();
    }

    @PutMapping("/plusGold")
    public Mono<ResponseEntity<Integer>> plusGoldUpdate(@RequestBody Clan clan){
        return clanService.plusGoldBalance(clan).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/minusGold")
    public Mono<ResponseEntity<Integer>> minusGoldUpdate(@RequestBody Clan clan){
        return clanService.minusGoldBalance(clan).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
