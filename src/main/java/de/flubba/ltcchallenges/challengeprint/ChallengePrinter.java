package de.flubba.ltcchallenges.challengeprint;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ChallengePrinter {
    private ChallengePrinter() {}

    public static void printChallenge(Difficulty difficulty) {
        log.info("Printing {} challenge", difficulty);
        System.out.println(difficulty); //TODO: implement me
    }
}
