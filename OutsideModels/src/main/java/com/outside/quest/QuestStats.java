package com.outside.quest;

import io.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class QuestStats extends Model {

    @Id
    @GeneratedValue
    public long id;

    @Column(nullable = false, unique = true)
    @OneToOne(mappedBy = "stats")
    private Quest quest;

    @Column
    @ManyToMany(mappedBy = "recommendedQuests")
    @JoinTable(name = "user_recommended_quest")
    private List<QuestUser> recommendedToUsers;

    @Column
    @ManyToMany(mappedBy = "finishedQuests")
    @JoinTable(name = "user_finished_quest")
    private List<QuestUser> finishedByUsers;


    public Quest getQuest() {
        return quest;
    }
    public void setQuest(Quest quest) {
        this.quest = quest;
    }
    public List<QuestUser> getRecommendedToUsers() {
        return recommendedToUsers;
    }
    public void setRecommendedToUsers(List<QuestUser> recommendedToUsers) {
        this.recommendedToUsers = recommendedToUsers;
    }
    public List<QuestUser> getFinishedByUsers() {
        return finishedByUsers;
    }
    public void setFinishedByUsers(List<QuestUser> finishedByUsers) {
        this.finishedByUsers = finishedByUsers;
    }
}
