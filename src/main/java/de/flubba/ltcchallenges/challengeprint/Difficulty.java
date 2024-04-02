package de.flubba.ltcchallenges.challengeprint;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Difficulty {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard"),
    VERY_HARD("very hard");

    public final String title;
}
