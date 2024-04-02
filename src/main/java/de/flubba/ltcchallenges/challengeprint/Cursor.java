package de.flubba.ltcchallenges.challengeprint;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public enum Cursor {
    INSTANCE;

    private final URL resource;

    Cursor() {
        resource = ChallengeRandomizer.class.getClassLoader().getResource("tasks.txt");
        Objects.requireNonNull(resource, "cannot load tasks.txt");
    }

    public static int getAndIncrementTeen() {
        return getAndIncrement("TEEN");
    }

    public static int getAndIncrement(Difficulty difficulty) {
        return getAndIncrement(difficulty.name());
    }

    private static int getAndIncrement(String suffix) {
        try {
            var path = getPath(suffix);
            if (Files.notExists(path)) {
                Files.writeString(path, "1", StandardOpenOption.CREATE);
                return 0;
            }
            var current = Integer.parseInt(Files.readString(path));
            Files.writeString(path, Integer.toString(current + 1), StandardOpenOption.WRITE);
            return current;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path getPath(String suffix) {
        try {
            return new File(new URI(INSTANCE.resource.toURI().toString().replace("tasks.txt", "cursor_%s.txt".formatted(suffix)))).toPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
