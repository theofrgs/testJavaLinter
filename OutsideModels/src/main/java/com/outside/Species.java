package com.outside;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Model;
import io.ebean.annotation.DbArray;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Species extends Model {

    @Id
    @GeneratedValue
    public long id;

    @Column
    private String name;

    @Column
    private JsonNode description;

    @Column
    private JsonNode locationData;

    @Column
    private JsonNode funFacts;

    @Column
    private JsonNode clues;

    @Column
    @DbArray
    private List<String> medias;


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public JsonNode getDescription() {
        return description;
    }
    public void setDescription(JsonNode description) {
        this.description = description;
    }
    public JsonNode getLocationData() {
        return locationData;
    }
    public void setLocationData(JsonNode locationData) {
        this.locationData = locationData;
    }
    public JsonNode getFunFacts() {
        return funFacts;
    }
    public void setFunFacts(JsonNode funFacts) {
        this.funFacts = funFacts;
    }
    public JsonNode getClues() {
        return clues;
    }
    public void setClues(JsonNode clues) {
        this.clues = clues;
    }
    public List<String> getMedias() {
        return medias;
    }
    public void setMedias(List<String> medias) {
        this.medias = medias;
    }
}
