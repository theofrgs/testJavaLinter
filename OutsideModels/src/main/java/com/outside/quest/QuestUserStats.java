package com.outside.quest;

import io.ebean.Model;

import javax.persistence.*;

@Entity
public class QuestUserStats extends Model {

    @Id
    @GeneratedValue
    public long id;

    @Column(nullable = false, unique = true)
    @OneToOne(mappedBy = "stats")
    private QuestUser user;


    public QuestUser getUser() {
        return user;
    }
    public void setUser(QuestUser user) {
        this.user = user;
    }
}
