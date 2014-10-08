package com.colibri.social_story.entities;

import lombok.Data;

@Data
public class Suggestion {

    private String word;
    private User user;

    public Suggestion(User user, String word) {
        this.word = word;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getWord() {
        return word;
    }
}