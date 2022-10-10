package ru.vadim.game.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;
import ru.vadim.game.model.Clan;
import ru.vadim.game.model.History;
import ru.vadim.game.repository.HistoryRepositiry;

@Controller
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryRepositiry historyRepositiry;

    @GetMapping("/hist")
    public Flux<History> getAllAccounts() {
        return historyRepositiry.findAll();
    }
}
