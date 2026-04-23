package com.javarush.projectquest.quests;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class DetectiveStoryQuest implements Quest {

    private static final long serialVersionUID = 1L;
    private Map<Integer, Question> questions = new HashMap<>();

    private final ResourceBundle bundle = ResourceBundle.getBundle("detective_messages");

    public DetectiveStoryQuest() {
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
                4, 5));

        questions.put(3, new Question(3,
                bundle.getString("q3.text"),
                bundle.getString("q3.answer1"),
                bundle.getString("q3.answer2"),
                6, 5));

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
                11, 12));

        questions.put(7, new Question(7,
                bundle.getString("q7.text"),
                bundle.getString("q7.answer1"),
                bundle.getString("q7.answer2"),
                1, 1, true,
                bundle.getString("q7.victory"),
                null));

        questions.put(8, new Question(8,
                bundle.getString("q8.text"),
                bundle.getString("q8.answer1"),
                bundle.getString("q8.answer2"),
                1, 1, true,
                null,
                bundle.getString("q8.defeat")));

        questions.put(9, new Question(9,
                bundle.getString("q9.text"),
                bundle.getString("q9.answer1"),
                bundle.getString("q9.answer2"),
                1, 1, true,
                bundle.getString("q9.victory"),
                null));

        questions.put(10, new Question(10,
                bundle.getString("q10.text"),
                bundle.getString("q10.answer1"),
                bundle.getString("q10.answer2"),
                1, 1, true,
                null,
                bundle.getString("q10.defeat")));

        questions.put(11, new Question(11,
                bundle.getString("q11.text"),
                bundle.getString("q11.answer1"),
                bundle.getString("q11.answer2"),
                1, 1, true,
                null,
                bundle.getString("q11.defeat")));

        questions.put(12, new Question(12,
                bundle.getString("q12.text"),
                bundle.getString("q12.answer1"),
                bundle.getString("q12.answer2"),
                1, 1, true,
                bundle.getString("q12.victory"),
                null));
    }

    @Override
    public String getId() {
        return "detective-story";
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
        return "Детектив/Мистика";
    }

    @Override
    public int getDifficultyLevel() {
        return 5;
    }

    @Override
    public String getBackgroundImage() {
        return "/images/mansion.jpg";
    }

    @Override
    public Map<Integer, Question> getQuestions() {
        return questions;
    }

    @Override
    public Question getStartQuestion() {
        return questions.get(1);
    }

    @Override
    public boolean isVictory(int questionId) {
        return questionId == 7 || questionId == 9 || questionId == 12;
    }

    @Override
    public String getVictoryMessage(int questionId) {
        return Optional.ofNullable(questions.get(questionId))
                .map(Question::getVictoryMessage)
                .orElse("Дело раскрыто!");
    }

    @Override
    public String getDefeatMessage(int questionId) {
        return Optional.ofNullable(questions.get(questionId))
                .map(Question::getDefeatMessage)
                .orElse("Дело провалено.");
    }
}
