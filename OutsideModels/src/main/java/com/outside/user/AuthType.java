package com.outside.user;

import org.springframework.util.StringUtils;

public enum AuthType {

    NATIVE,
    GOOGLE;

    public String getDisplayName() {
        return StringUtils.capitalize(this.toString().toLowerCase().replace('_', ' '));
    }

}
