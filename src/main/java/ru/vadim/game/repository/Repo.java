package ru.vadim.game.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.vadim.game.model.Clan;

@Repository
public interface Repo extends ReactiveCrudRepository<Clan, Long> {
}
