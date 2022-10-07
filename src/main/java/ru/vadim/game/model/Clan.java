package ru.vadim.game.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
public class Clan {
    private long id;     // id клана
    private String name; // имя клана
    private int gold;    // текущее количество золота в казне клана

    public Clan(long id, String name, int gold) {
        this.id = id;
        this.name = name;
        this.gold = gold;
    }

    public Clan(Clan clan) {
        this.id = clan.getId();
        this.name = clan.getName();
        this.gold = clan.getGold();
    }
}
