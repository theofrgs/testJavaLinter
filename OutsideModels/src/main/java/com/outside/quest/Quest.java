package com.outside.quest;

import io.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Quest extends Model {

    @Id
    @GeneratedValue
    public long id;

    @Column
    @OneToMany
    private List<QuestStep> steps;

    @Column
    @OneToOne
    private QuestStats stats;


    public List<QuestStep> getSteps() {
        return steps;
    }
    public void setSteps(List<QuestStep> steps) {
        this.steps = steps;
    }
    public QuestStats getStats() {
        return stats;
    }
    public void setStats(QuestStats stats) {
        this.stats = stats;
    }
}
