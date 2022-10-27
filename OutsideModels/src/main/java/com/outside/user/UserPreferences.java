package com.outside.user;

import io.ebean.Model;

import javax.persistence.*;

@Entity
@Table(name = "users_preferences")
public class UserPreferences extends Model {

    @Id
    @GeneratedValue
    public long id;

    @Column(nullable = false, unique = true)
    @OneToOne(mappedBy = "preferences")
    private User user;

    @Column
    private String language;


    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String lang) {
        this.language = lang;
    }
}
