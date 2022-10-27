package com.outside.quest;

import io.ebean.Model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class StartedQuest extends Model {

    @Id
    @GeneratedValue
    public long id;

    @Column
    @ManyToOne
    private Quest quest;

    @Column
    private short progress;

    @Column
    private LocalDateTime startDate;

    @Column
    @ManyToOne(optional = false)
    private QuestUser user;


    public Quest getQuest() {
        return quest;
    }
    public void setQuest(Quest quest) {
        this.quest = quest;
    }
    public short getProgress() {
        return progress;
    }
    public void setProgress(short progress) {
        this.progress = progress;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    public QuestUser getUser() {
        return user;
    }
    public void setUser(QuestUser user) {
        this.user = user;
    }
}
