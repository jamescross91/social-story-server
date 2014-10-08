package com.colibri.social_story.entities;

import lombok.Data;
import lombok.NonNull;

@Data
@SuppressWarnings("PMD.UnusedPrivateField")
public class ScoredWord {

    @NonNull
    private User user;
    @NonNull
    private String word;
    @NonNull
    private int score;

    public void incrementScore() {
        score++;
    }
}
