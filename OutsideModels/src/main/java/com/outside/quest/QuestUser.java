package com.outside.quest;

import com.outside.user.User;
import io.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class QuestUser extends Model {

    @Id
    @GeneratedValue
    public long id;

    @Column
    @OneToMany(mappedBy = "user")
    private List<StartedQuest> startedQuests;

    @Column
    @ManyToMany
    @JoinTable(name = "user_recommended_quest")
    private List<Quest> recommendedQuests;

    @Column
    @ManyToMany
    @JoinTable(name = "user_finished_quest")
    private List<Quest> finishedQuests;

    @Column
    @OneToOne
    private QuestUserStats stats;

    @Column(nullable = false, unique = true)
    @ManyToOne
    private User user;


    public List<StartedQuest> getStartedQuests() {
        return startedQuests;
    }
    public void setStartedQuests(List<StartedQuest> startedQuests) {
        this.startedQuests = startedQuests;
    }
    public List<Quest> getRecommendedQuests() {
        return recommendedQuests;
    }
    public void setRecommendedQuests(List<Quest> recommendedQuests) {
        this.recommendedQuests = recommendedQuests;
    }
    public List<Quest> getFinishedQuests() {
        return finishedQuests;
    }
    public void setFinishedQuests(List<Quest> finishedQuests) {
        this.finishedQuests = finishedQuests;
    }
    public QuestUserStats getStats() {
        return stats;
    }
    public void setStats(QuestUserStats stats) {
        this.stats = stats;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
