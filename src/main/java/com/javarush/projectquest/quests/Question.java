package com.javarush.projectquest.quests;

import java.io.Serializable;
import java.util.Optional;

public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int id;
    private final String text;
    private final String option1;
    private final String option2;
    private final int nextIdOption1;
    private final int nextIdOption2;
    private final boolean isFinal;
    private final String victoryMessage;
    private final String defeatMessage;
    private final int difficulty;

    private Question(Builder builder) {
        this.id = builder.id;
        this.text = builder.text;
        this.option1 = builder.option1;
        this.option2 = builder.option2;
        this.nextIdOption1 = builder.nextIdOption1;
        this.nextIdOption2 = builder.nextIdOption2;
        this.isFinal = builder.isFinal;
        this.victoryMessage = builder.victoryMessage;
        this.defeatMessage = builder.defeatMessage;
        this.difficulty = builder.difficulty;
    }

    public static class Builder {
        private int id;
        private String text;
        private String option1;
        private String option2;
        private int nextIdOption1;
        private int nextIdOption2;
        private boolean isFinal = false;
        private String victoryMessage;
        private String defeatMessage;
        private int difficulty = 1;

        public Builder id(int id) { this.id = id; return this; }
        public Builder text(String text) { this.text = text; return this; }
        public Builder option1(String option1) { this.option1 = option1; return this; }
        public Builder option2(String option2) { this.option2 = option2; return this; }
        public Builder nextIdOption1(int id) { this.nextIdOption1 = id; return this; }
        public Builder nextIdOption2(int id) { this.nextIdOption2 = id; return this; }
        public Builder isFinal(boolean isFinal) { this.isFinal = isFinal; return this; }
        public Builder victoryMessage(String msg) { this.victoryMessage = msg; return this; }
        public Builder defeatMessage(String msg) { this.defeatMessage = msg; return this; }
        public Builder difficulty(int difficulty) { this.difficulty = difficulty; return this; }

        public Question build() {
            return new Question(this);
        }
    }

    public int getId() { return id; }
    public String getText() { return text; }
    public String getOption1() { return option1; }
    public String getOption2() { return option2; }
    public int getNextIdOption1() { return nextIdOption1; }
    public int getNextIdOption2() { return nextIdOption2; }
    public boolean isFinal() { return isFinal; }
    public String getVictoryMessage() {
        return Optional.ofNullable(victoryMessage)
                .filter(s -> !s.isBlank())
                .orElse(null);
    }
    public String getDefeatMessage() {
        return Optional.ofNullable(defeatMessage)
                .filter(s -> !s.isBlank())
                .orElse(null);
    }

    public int getDifficultyLevel() {
        return this.difficulty;
    }
}
