package de.flubba.ltcchallenges.challengeprint;

import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.Objects;

@Slf4j
public enum RandomChallengePrinter {
    INSTANCE;

    private final URL resource;

    RandomChallengePrinter() {
        resource = RandomChallengePrinter.class.getClassLoader().getResource("tasks.txt");
        Objects.requireNonNull(resource, "cannot load tasks.txt");
    }

    public static void printChallenge(Difficulty difficulty) {
        log.info("Printing {} challenge", difficulty);

        String currentTeen = ChallengeRandomizer.getLine("randomized_TEEN.txt", Cursor.getAndIncrementTeen());
        String currentTask = ChallengeRandomizer.getLine("randomized_%s.txt".formatted(difficulty.name()), Cursor.getAndIncrement(difficulty));

        log.info("teen: {} | task: {}", currentTeen, currentTask);

        var task = currentTask.replace("$TEEN", currentTeen);

        ActualChallengePrinter.print(difficulty, task);
    }
}
