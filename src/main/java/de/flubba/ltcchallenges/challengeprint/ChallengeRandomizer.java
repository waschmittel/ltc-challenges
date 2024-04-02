package de.flubba.ltcchallenges.challengeprint;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.String.join;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Slf4j
public final class ChallengeRandomizer {
    private ChallengeRandomizer() {}

    public static void initRandomFiles() {
        try {
            var tasks = readTasks();

            var resource = ChallengeRandomizer.class.getClassLoader().getResource("tasks.txt");
            Objects.requireNonNull(resource, "cannot load tasks.txt");

            for (var entry : tasks.entrySet()) {
                Path path = null;
                path = new File(new URI(resource.toURI().toString().replace("tasks.txt", "randomized_%s.txt".formatted(entry.getKey().name())))).toPath();
                var challenges = entry.getValue();
                writeShuffled(path, challenges);
            }

            var teens = readLines("names.txt");
            var path = new File(new URI(resource.toURI().toString().replace("tasks.txt", "randomized_TEEN.txt"))).toPath();
            writeShuffled(path, teens);
        } catch (URISyntaxException | IOException | RuntimeException e) {
            log.error("could not write random files", e);
            throw new RuntimeException(e);
        }
    }

    private static void writeShuffled(Path path, List<String> challenges) throws IOException {
        if (Files.notExists(path)) {
            log.info("writing {}", path);
            var written = 0;

            while (written < 10000) {
                shuffle(challenges);
                Files.writeString(path, join("\n", challenges) + "\n", CREATE, APPEND);
                written += challenges.size();
            }
            log.info("done writing {}", path);
        }
    }

    private static List<String> readLines(String fileName) {
        try {
            var resource = ChallengeRandomizer.class.getClassLoader().getResource(fileName);
            Objects.requireNonNull(resource, "cannot load " + fileName);
            var uri = resource.toURI();
            return Files.readAllLines(new File(uri).toPath());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLine(String fileName, int lineNo) {
        return readLines(fileName).get(lineNo);
    }

    private static Map<Difficulty, List<String>> readTasks() {
        return readLines("tasks.txt").stream()
                .map(line -> line.split(": +", 2))
                .collect(groupingBy(
                        parts -> Difficulty.valueOf(parts[0]),
                        mapping(strings -> strings[1], toList())
                ));
    }
}
