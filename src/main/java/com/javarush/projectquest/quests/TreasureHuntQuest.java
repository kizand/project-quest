package com.javarush.projectquest.quests;

import java.util.*;

public class TreasureHuntQuest extends BaseLevel implements Quest {

    private static final long serialVersionUID = 1L;
    private final Map<Integer, Question> questions = new HashMap<>();
    private final ResourceBundle bundle = ResourceBundle.getBundle("treasure_messages");

    public TreasureHuntQuest() {
        initializeQuestions();
    }

    private void initializeQuestions() {

        questions.put(1, new Question.Builder()
                .id(1)
                .text(bundle.getString("q1.text"))
                .option1(bundle.getString("q1.answer1"))
                .option2(bundle.getString("q1.answer2"))
                .nextIdOption1(2)
                .nextIdOption2(3)
                .build());

        questions.put(2, new Question.Builder()
                .id(2)
                .text(bundle.getString("q2.text"))
                .option1(bundle.getString("q2.answer1"))
                .option2(bundle.getString("q2.answer2"))
                .nextIdOption1(4)
                .nextIdOption2(5)
                .build());

        questions.put(3, new Question.Builder()
                .id(3)
                .text(bundle.getString("q3.text"))
                .option1(bundle.getString("q3.answer1"))
                .option2(bundle.getString("q3.answer2"))
                .nextIdOption1(6)
                .nextIdOption2(2)
                .build());

        questions.put(4, new Question.Builder()
                .id(4)
                .text(bundle.getString("q4.text"))
                .option1(bundle.getString("q4.answer1"))
                .option2(bundle.getString("q4.answer2"))
                .nextIdOption1(7)
                .nextIdOption2(8)
                .build());

        questions.put(5, new Question.Builder()
                .id(5)
                .text(bundle.getString("q5.text"))
                .option1(bundle.getString("q5.answer1"))
                .option2(bundle.getString("q5.answer2"))
                .nextIdOption1(9)
                .nextIdOption2(10)
                .build());

        questions.put(6, new Question.Builder()
                .id(6)
                .text(bundle.getString("q6.text"))
                .option1(bundle.getString("q6.answer1"))
                .option2(bundle.getString("q6.answer2"))
                .nextIdOption1(11)
                .nextIdOption2(12)
                .build());

        questions.put(7, new Question.Builder()
                .id(7)
                .text(bundle.getString("q7.text"))
                .option1(bundle.getString("q7.answer1"))
                .option2(bundle.getString("q7.answer2"))
                .nextIdOption1(1)
                .nextIdOption2(1)
                .isFinal(true)
                .victoryMessage(bundle.getString("q7.victory"))
                .defeatMessage("")
                .difficulty(Integer.parseInt(bundle.getString("q7.difficulty")))
                .build());

        questions.put(8, new Question.Builder()
                .id(8)
                .text(bundle.getString("q8.text"))
                .option1(bundle.getString("q8.answer1"))
                .option2(bundle.getString("q8.answer2"))
                .nextIdOption1(1)
                .nextIdOption2(1)
                .isFinal(true)
                .victoryMessage(bundle.getString("q8.victory"))
                .defeatMessage("")
                .difficulty(Integer.parseInt(bundle.getString("q8.difficulty")))
                .build());

        questions.put(9, new Question.Builder()
                .id(9)
                .text(bundle.getString("q9.text"))
                .option1(bundle.getString("q9.answer1"))
                .option2(bundle.getString("q9.answer2"))
                .nextIdOption1(1)
                .nextIdOption2(1)
                .isFinal(true)
                .victoryMessage(bundle.getString("q9.victory"))
                .defeatMessage("")
                .difficulty(Integer.parseInt(bundle.getString("q9.difficulty")))
                .build());

        questions.put(10, new Question.Builder()
                .id(10)
                .text(bundle.getString("q10.text"))
                .option1(bundle.getString("q10.answer1"))
                .option2(bundle.getString("q10.answer2"))
                .nextIdOption1(1)
                .nextIdOption2(1)
                .isFinal(true)
                .victoryMessage("")
                .defeatMessage(bundle.getString("q10.defeat"))
                .difficulty(Integer.parseInt(bundle.getString("q10.difficulty")))
                .build());

        questions.put(11, new Question.Builder()
                .id(11)
                .text(bundle.getString("q11.text"))
                .option1(bundle.getString("q11.answer1"))
                .option2(bundle.getString("q11.answer2"))
                .nextIdOption1(1)
                .nextIdOption2(1)
                .isFinal(true)
                .victoryMessage("")
                .defeatMessage(bundle.getString("q11.defeat"))
                .difficulty(Integer.parseInt(bundle.getString("q11.difficulty")))
                .build());

        questions.put(12, new Question.Builder()
                .id(12)
                .text(bundle.getString("q12.text"))
                .option1(bundle.getString("q12.answer1"))
                .option2(bundle.getString("q12.answer2"))
                .nextIdOption1(1)
                .nextIdOption2(1)
                .isFinal(true)
                .victoryMessage(bundle.getString("q12.victory"))
                .defeatMessage("")
                .difficulty(Integer.parseInt(bundle.getString("q12.difficulty")))
                .build());
    }

    @Override
    public String getId() {
        return config.getProperty("treasure.id");
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
        return config.getProperty("treasure.genre");
    }

    @Override
    public int getDifficultyLevel() {
        return getDifficultyFromProps("treasure.difficulty");
    }

    @Override
    public String getBackgroundImage() {
        return config.getProperty("treasure.image");
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
        return super.isVictory(questionId, "treasure.victoryIds");
    }

    @Override
    public String getVictoryMessage(int questionId) {
        return Optional.ofNullable(questions.get(questionId))
                .map(Question::getVictoryMessage)
                .orElse("Поздравляем! Вы победили!");
    }

    @Override
    public String getDefeatMessage(int questionId) {
        return Optional.ofNullable(questions.get(questionId))
                .map(Question::getDefeatMessage)
                .orElse("К сожалению, вы проиграли.");
    }
}
