package com.outside.quest;

import org.springframework.util.StringUtils;

public enum QuestStepType {

    PLACE,
    SPECIES;

    public String getDisplayName() {
        return StringUtils.capitalize(this.toString().toLowerCase().replace('_', ' '));
    }

}
