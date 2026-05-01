package com.javarush.projectquest.quests;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public abstract class BaseLevel implements Quest {
    protected static final Properties config = new Properties();

    static {
        try (InputStream input = BaseLevel.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                config.load(new InputStreamReader(input, StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected int getDifficultyFromProps(String key) {
        return Integer.parseInt(config.getProperty(key, "1")); // 1 — значение по умолчанию
    }

    public boolean isVictory(int questionId, String propertyKey) {
        String rawValue = config.getProperty(propertyKey, "");
        if (rawValue.isEmpty()) return false;

        List<Integer> victoryIds = Arrays.stream(rawValue.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        return victoryIds.contains(questionId);
    }
}
