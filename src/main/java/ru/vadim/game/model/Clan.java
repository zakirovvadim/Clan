package ru.vadim.game.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table(name = "clan")
public class Clan {
    @Id
    private Long id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer gold;

    public Clan(long id, String name, int gold) {
        this.id = id;
        this.name = name;
        this.gold = gold;
    }

    public Clan(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Clan(Clan clan) {
        this.id = clan.getId();
        this.name = clan.getName();
        this.gold = clan.getGold();
    }
}
