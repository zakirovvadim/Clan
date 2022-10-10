package ru.vadim.game.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class History {
    @Id
    private Long id;
    private String name;
    private Boolean mns;
    private Boolean pls;
    private Integer count;
    private LocalDate date;
    private Integer gold;

    public History(Long id, String name, Boolean mns, Boolean pls, Integer count, LocalDate date) {
        this.id = id;
        this.name = name;
        this.mns = mns;
        this.pls = pls;
        this.count = count;
        this.date = date;
    }

    public History(String name, Boolean mns, Boolean pls, Integer count, LocalDate date) {
        this.name = name;
        this.mns = mns;
        this.pls = pls;
        this.count = count;
        this.date = date;
    }
}
