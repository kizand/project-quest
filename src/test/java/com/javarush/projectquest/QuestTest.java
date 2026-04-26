package com.javarush.projectquest;

import com.javarush.projectquest.quests.Quest;
import com.javarush.projectquest.quests.QuestManager;
import com.javarush.projectquest.quests.Question;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class QuestTest {

    private QuestManager questManager;

    @BeforeEach
    void setUp() {
        QuestManager.resetInstance();
        this.questManager = QuestManager.getInstance();
    }

    @AfterEach
    void tearDown() {
        resetQuestStats();
    }

    private void resetQuestStats() {
        try {
            java.lang.reflect.Field statsField = QuestManager.class.getDeclaredField("questStats");
            statsField.setAccessible(true);
            java.util.Map<String, Integer> stats = (java.util.Map<String, Integer>) statsField.get(questManager);
            stats.clear();
        } catch (Exception e) {
            org.junit.jupiter.api.Assertions.fail("Failed to reset statistics via reflection: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("The quest list must not be null")
    public void shouldReturnNonNullQuestList() {
        List<Quest> quests = questManager.getAllQuests();
        assertNotNull(quests, "The quest list must not be null");
    }

    @Test
    @DisplayName("The quest list must not be empty")
    public void shouldReturnNonEmptyQuestList() {
        List<Quest> quests = questManager.getAllQuests();
        assertFalse(quests.isEmpty(), "The quest list must contain elements");
    }

    @Test
    @DisplayName("At least 3 quests must be loaded")
    public void shouldHaveAtLeastThreeQuestsOnStart() {
        List<Quest> quests = questManager.getAllQuests();
        assertTrue(quests.size() >= 3, "At least 3 default quests were expected");
    }

    @Test
    @DisplayName("Search by ID: Quest 'treasure-hunt' must have a valid name")
    public void getQuestById_ShouldReturnCorrectTreasureHuntTitle() {
        Quest quest = questManager.getQuest("treasure-hunt");
        assertNotNull(quest, "The quest 'treasure hunt' was not found");
        assertEquals("Поиск сокровищ в замке", quest.getTitle());
    }

    @Test
    @DisplayName("Search by ID: Quest 'space-adventure' must have the genre Survival")
    public void getQuestById_ShouldReturnCorrectSpaceAdventureGenre() {
        Quest quest = questManager.getQuest("space-adventure");
        assertNotNull(quest, "The quest 'space-adventure' was not found");
        assertEquals("Выживание", quest.getGenre());
    }

    @Test
    @DisplayName("Search by ID: The 'detective-story' quest must have a difficulty of 5")
    public void getQuestById_ShouldReturnCorrectDetectiveDifficulty() {
        Quest quest = questManager.getQuest("detective-story");
        assertNotNull(quest, "The quest 'detective-story' was not found");
        assertEquals(5, quest.getDifficultyLevel());
    }

    @Test
    @DisplayName("Search by ID: Requesting a non-existent quest should return null")
    public void getQuest_ShouldReturnNull_WhenIdDoesNotExist() {
        Quest nonExistent = questManager.getQuest("non-existent");
        assertNull(nonExistent, "Should return null if the quest is not in the database.");
    }

    @Test
    @DisplayName("Quest structure: Checking the correctness of the starting question and the completeness of the list of questions")
    public void treasureHunt_ShouldHaveValidStructureAndQuestions() {
        Quest quest = questManager.getQuest("treasure-hunt");
        assertNotNull(quest, "The quest 'treasure-hunt' must be loaded");

        Question startQuestion = quest.getStartQuestion();
        assertAll("The starting question must be filled out completely",
                () -> assertNotNull(startQuestion, "There is no starting question"),
                () -> assertEquals(1, startQuestion.getId(), "The start question ID is not equal to 1"),
                () -> assertNotNull(startQuestion.getText(), "The text of the question is not specified"),
                () -> assertNotNull(startQuestion.getOption1(), "The first answer option is not empty."),
                () -> assertNotNull(startQuestion.getOption2(), "The second answer option is not empty.")
        );

        Map<Integer, Question> questions = quest.getQuestions();
        assertAll("General structure of quest questions",
                () -> assertNotNull(questions, "The question map is not initialized"),
                () -> assertFalse(questions.isEmpty(), "The list of questions is empty"),
                () -> assertTrue(questions.size() >= 10, "The quest must have at least 10 questions" +
                        " (now: " + questions.size() + ")")
        );
    }

    @Test
    @DisplayName("Space Adventure: Checking the Quest and Plot Synopsis")
    public void testSpaceAdventureQuestQuestions() {
        Quest quest = questManager.getQuest("space-adventure");

        assertNotNull(quest, "Quest 'space-adventure' not found in manager");

        assertAll("Checking the starting question",
                () -> {
                    Question startQuestion = quest.getStartQuestion();
                    assertNotNull(startQuestion, "There is no starting question");
                    assertEquals("Ваш космический корабль потерпел крушение на неизвестной планете. Что делать?",
                            startQuestion.getText(),
                            "The quest intro text does not match the expected one");
                }
        );
    }

    @Test
    @DisplayName("Detective Story: Checking the Starting Question Topic")
    public void testDetectiveStoryQuestQuestions() {
        Quest quest = questManager.getQuest("detective-story");

        assertNotNull(quest, "Quest 'detective-story' not found in manager");

        assertAll("Checking the starting question",
                () -> {
                    Question startQuestion = quest.getStartQuestion();
                    assertNotNull(startQuestion, "There is no starting question");
                    assertTrue(startQuestion.getText().contains("детектив"),
                            "The text of the opening question must contain a mention of a detective story");
                });
    }

    @Test
    @DisplayName("Treasure Hunt: Checking Victory Conditions and Defeat Conditions")
    public void testTreasureHuntVictoryConditions() {
        Quest quest = questManager.getQuest("treasure-hunt");

        assertNotNull(quest, "The quest 'treasure-hunt' must exist");

        assertAll("Checking the logic of quest completion",
                () -> {
                    int[] victoryIds = {7, 8, 9, 12};
                    for (int id : victoryIds) {
                        assertTrue(quest.isVictory(id), "Question " + id + " must be victorious");
                        String msg = quest.getVictoryMessage(id);
                        assertNotNull(msg, "Winning Message for ID " + id + " should not be null");
                        assertFalse(msg.isBlank(), "Winning Message for ID " + id + " should not be empty");
                    }
                },

                () -> {
                    int[] defeatIds = {10, 11};
                    for (int id : defeatIds) {
                        assertFalse(quest.isVictory(id), "Question " + id + " should not be victorious");
                        String msg = quest.getDefeatMessage(id);
                        assertNotNull(msg, "Defeat message for ID " + id + " should not be null");
                        assertFalse(msg.isBlank(), "Defeat message for ID " + id + " should not be empty");
                    }
                }
        );
    }

    @Test
    @DisplayName("Space Adventure: Checking Victory Conditions, Defeat Conditions, and Ending Message Texts")
    public void testSpaceAdventureVictoryConditions() {
        Quest quest = questManager.getQuest("space-adventure");

        assertNotNull(quest, "The quest 'space-adventure' must exist");

        assertAll("Checking the logic of quest completion",
                () -> {
                    int[] victoryIds = {7, 9};
                    for (int id : victoryIds) {
                        assertTrue(quest.isVictory(id), "Question " + id + " must be victorious");
                        String msg = quest.getVictoryMessage(id);
                        assertNotNull(msg, "Winning Message for ID " + id + " should not be null");
                        assertFalse(msg.isBlank(), "Winning Message for ID " + id + " should not be empty");
                    }
                },

                () -> {
                    int[] defeatIds = {6, 8, 10};
                    for (int id : defeatIds) {
                        assertFalse(quest.isVictory(id), "Question " + id + " should not be victorious");
                        String msg = quest.getDefeatMessage(id);
                        assertNotNull(msg, "Defeat message for ID " + id + " should not be null");
                        assertFalse(msg.isBlank(), "Defeat message for ID " + id + " should not be empty");
                    }
                },

                () -> {
                    assertAll("Checking the exact texts of messages",
                            () -> assertEquals("Вы успешно вернулись на Землю и стали героем!",
                                    quest.getVictoryMessage(7), "Текст победы (ID 7) не совпадает"),
                            () -> assertEquals("Корабль уничтожен. Вы погибли.",
                                    quest.getDefeatMessage(6), "Текст поражения (ID 6) не совпадает")
                    );
                }
        );
    }

    @Test
    @DisplayName("Detective Story: Checking Victory Conditions and Defeat Conditions")
    public void testDetectiveStoryVictoryConditions() {
        Quest quest = questManager.getQuest("detective-story");

        assertNotNull(quest, "The quest 'detective-story' must exist");

        assertAll("Checking the logic of quest completion",
                () -> {
                    int[] victoryIds = {7, 9, 12};
                    for (int id : victoryIds) {
                        assertTrue(quest.isVictory(id), "Question " + id + " must be victorious");
                        String msg = quest.getVictoryMessage(id);
                        assertNotNull(msg, "Winning Message for ID " + id + " should not be null");
                        assertFalse(msg.isBlank(), "Winning Message for ID " + id + " should not be empty");
                    }
                },

                () -> {
                    int[] defeatIds = {8, 10, 11};
                    for (int id : defeatIds) {
                        assertFalse(quest.isVictory(id), "Question " + id + " should not be victorious");
                        String msg = quest.getDefeatMessage(id);
                        assertNotNull(msg, "Defeat message for ID " + id + " should not be null");
                        assertFalse(msg.isBlank(), "Defeat message for ID " + id + " should not be empty");
                    }
                }
        );
    }

    @Test
    @DisplayName("Statistics: Checking the quest launch counter")
    public void testQuestStats() {
        String questId = "detective-story";
        String otherQuestId = "treasure-hunt";

        int initialPlays = questManager.getQuestPlays(questId);
        assertEquals(0, initialPlays, "The initial number of passes should be 0");

        questManager.incrementQuestPlays(questId);
        questManager.incrementQuestPlays(questId);

        assertAll("Checking the correctness of the increment and isolation of counters",
                () -> assertEquals(2, questManager.getQuestPlays(questId),
                        "Counter '" + questId + "' should increase to 2"),

                () -> assertEquals(0, questManager.getQuestPlays(otherQuestId),
                        "Another quest counter ('" + otherQuestId + "') should not change")
        );
    }

    @Test
    @DisplayName("Statistics: Obtain a full walkthrough map for all quests")
    public void testGetAllQuestStats() {
        questManager.incrementQuestPlays("treasure-hunt");
        questManager.incrementQuestPlays("treasure-hunt");
        questManager.incrementQuestPlays("space-adventure");

        Map<String, Integer> allStats = questManager.getAllQuestStats();

        assertAll("Проверка содержания общей статистики",
                () -> assertNotNull(allStats, "The stats map must not be null"),
                () -> assertEquals(2, allStats.getOrDefault("treasure-hunt", -1),
                        "'Treasure Hunt' was expected to take 2 playthroughs"),
                () -> assertEquals(1, allStats.getOrDefault("space-adventure", -1),
                        "'Space Adventure' was expected to have 1 playthrough"),
                () -> assertEquals(0, allStats.getOrDefault("detective-story", -1),
                        "'Detective Story' had 0 playthroughs expected")
        );
    }

}
