package com.outside.quest;

import com.fasterxml.jackson.databind.JsonNode;
import com.outside.Place;
import com.outside.Species;
import io.ebean.Model;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
public class QuestStep extends Model {

    @Id
    @GeneratedValue
    public long id;

    @Column
    private QuestStepType type;

    @Column
    @ManyToOne
    private Place place;

    @Column
    @ManyToOne
    private Species species;

    @Column
    private String instructions;

    @Column
    private JsonNode description;

    @Column
    @ManyToOne
    private Quest quest;


    public QuestStepType getType() {
        return type;
    }
    public void setType(QuestStepType type) {
        this.type = type;
    }
    @Nullable
    public Place getPlace() {
        return place;
    }
    public void setPlace(@Nullable Place place) {
        this.place = place;
    }
    @Nullable
    public Species getSpecies() {
        return species;
    }
    public void setSpecies(@Nullable Species species) {
        this.species = species;
    }
    public String getInstructions() {
        return instructions;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    public JsonNode getDescription() {
        return description;
    }
    public void setDescription(JsonNode description) {
        this.description = description;
    }
    public Quest getQuests() {
        return quest;
    }
    public void setQuests(Quest quest) {
        this.quest = quest;
    }
}
