package ru.vadim.game.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vadim.game.model.Clan;
import ru.vadim.game.repository.ClanRepository;


@Service
@RequiredArgsConstructor
public class ClanService {

    private final ClanRepository clanRepository;

    public Mono<Clan> findById(long id) {
        return clanRepository.findById(id);
    }

    public Flux<Clan> createNewClan(Clan clan) {
       return clanRepository.createNewClan(clan);
    }

    public Flux<Clan> findAll() {
        return clanRepository.findAll();
    }

    public Flux<Clan> plusGoldBalance(Clan clan) {
        return clanRepository.plusUpdate(clan);
    }

    public Flux<Clan> minusGoldBalance(Clan clan) {
        return clanRepository.minusUpdate(clan);
    }
}
