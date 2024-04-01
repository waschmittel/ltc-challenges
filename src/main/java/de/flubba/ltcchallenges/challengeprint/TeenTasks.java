package de.flubba.ltcchallenges.challengeprint;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class TeenTasks {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        var tasks = readTasks();

        readNames().stream()
                .flatMap(name -> tasks.entrySet().stream()
                        .map(entry -> getRandomTaskWithName(entry.getValue(), entry.getKey(), name)))
                .sorted()
                .forEach(System.out::println);
    }

    private static Map<String, List<String>> readTasks() {
        return readLines("tasks.txt").stream()
                .map(line -> line.split("\\. "))
                .collect(groupingBy(
                        strings -> strings[0],
                        mapping(strings -> strings[1], toList())
                ));
    }

    private static List<String> readNames() {
        return readLines("names.csv").stream()
                .map(line -> line.split(","))
                .map(strings -> strings[1] + " " + strings[0])
                .toList();
    }

    private static String getRandomTaskWithName(List<String> tasks, String difficulty, String name) {
        return "[%s] %s".formatted(
                difficulty,
                tasks.get(RANDOM.nextInt(0, tasks.size()))
                        .replaceAll(" [—-]+", " " + name));
    }


    private static List<String> readLines(String fileName) {
        try {
            var resource = TeenTasks.class.getClassLoader().getResource(fileName);
            Objects.requireNonNull(resource, "cannot load " + fileName);
            var uri = resource.toURI();
            return Files.readAllLines(new File(uri).toPath());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
