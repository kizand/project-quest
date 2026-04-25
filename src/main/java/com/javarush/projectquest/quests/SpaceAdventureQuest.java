package com.javarush.projectquest.quests;

import java.util.*;

public class SpaceAdventureQuest extends BaseLevel implements Quest {

    private static final long serialVersionUID = 1L;
    private final Map<Integer, Question> questions = new HashMap<>();

    private final ResourceBundle bundle = ResourceBundle.getBundle("space_messages");

    public SpaceAdventureQuest() {
        initializeQuestions();
    }

    private void initializeQuestions() {

        questions.put(1, new Question(1,
                bundle.getString("q1.text"),
                bundle.getString("q1.answer1"),
                bundle.getString("q1.answer2"),
                2, 3));

        questions.put(2, new Question(2,
                bundle.getString("q2.text"),
                bundle.getString("q2.answer1"),
                bundle.getString("q2.answer2"),
                4, 3));

        questions.put(3, new Question(3,
                bundle.getString("q3.text"),
                bundle.getString("q3.answer1"),
                bundle.getString("q3.answer2"),
                5, 6));

        questions.put(4, new Question(4,
                bundle.getString("q4.text"),
                bundle.getString("q4.answer1"),
                bundle.getString("q4.answer2"),
                7, 8));

        questions.put(5, new Question(5,
                bundle.getString("q5.text"),
                bundle.getString("q5.answer1"),
                bundle.getString("q5.answer2"),
                9, 10));

        questions.put(6, new Question(6,
                bundle.getString("q6.text"),
                bundle.getString("q6.answer1"),
                bundle.getString("q6.answer2"),
                1, 1, true,
                "", bundle.getString("q6.defeat"),
                Integer.parseInt(bundle.getString("q6.difficulty"))));

        questions.put(7, new Question(7,
                bundle.getString("q7.text"),
                bundle.getString("q7.answer1"),
                bundle.getString("q7.answer2"),
                1, 1, true,
                bundle.getString("q7.victory"), "",
                Integer.parseInt(bundle.getString("q7.difficulty"))));

        questions.put(8, new Question(8,
                bundle.getString("q8.text"),
                bundle.getString("q8.answer1"),
                bundle.getString("q8.answer2"),
                1, 1, true,
                "",
                bundle.getString("q8.defeat"),
                Integer.parseInt(bundle.getString("q8.difficulty"))));

        questions.put(9, new Question(9,
                bundle.getString("q9.text"),
                bundle.getString("q9.answer1"),
                bundle.getString("q9.answer2"),
                1, 1, true, "",
                bundle.getString("q10.defeat"),
                Integer.parseInt(bundle.getString("q10.difficulty"))));

        questions.put(10, new Question(10,
                bundle.getString("q10.text"),
                bundle.getString("q10.answer1"),
                bundle.getString("q10.answer2"),
                1, 1, true, "",
                bundle.getString("q10.defeat"),
                Integer.parseInt(bundle.getString("q10.difficulty"))));
    }

    @Override
    public String getId() {
        return config.getProperty("space.id");
    }

    @Override
    public String getTitle() {
        return bundle.getString("quest.title");
    }

    @Override
    public String getDescription() {
        return bundle.getString("quest.description");
    }

    @Override
    public String getGenre() {
        return config.getProperty("space.genre");
    }

    @Override
    public int getDifficultyLevel() {
        return getDifficultyFromProps("space.difficulty");
    }

    @Override
    public String getBackgroundImage() {
        return config.getProperty("space.image");
    }

    @Override
    public Map<Integer, Question> getQuestions() {
        return Collections.unmodifiableMap(questions);
    }

    @Override
    public Question getStartQuestion() {
        return questions.get(1);
    }

    @Override
    public boolean isVictory(int questionId) {
        return super.isVictory(questionId, "space.victoryIds");
    }

    @Override
    public String getVictoryMessage(int questionId) {
        return Optional.ofNullable(questions.get(questionId))
                .map(Question::getVictoryMessage)
                .orElse("Миссия выполнена!");
    }

    @Override
    public String getDefeatMessage(int questionId) {
        return Optional.ofNullable(questions.get(questionId))
                .map(Question::getDefeatMessage)
                .orElse("Миссия провалена.");
    }
}
