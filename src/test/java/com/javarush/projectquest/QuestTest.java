package com.javarush.projectquest;

import com.javarush.projectquest.quests.Quest;
import com.javarush.projectquest.quests.QuestManager;
import com.javarush.projectquest.quests.Question;
import com.javarush.projectquest.quests.GameState;
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

    @SuppressWarnings("unchecked")
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
    void shouldReturnNonNullQuestList() {
        List<Quest> quests = questManager.getAllQuests();
        assertNotNull(quests, "The quest list must not be null");
    }

    @Test
    @DisplayName("The quest list must not be empty")
    void shouldReturnNonEmptyQuestList() {
        List<Quest> quests = questManager.getAllQuests();
        assertFalse(quests.isEmpty(), "The quest list must contain elements");
    }

    @Test
    @DisplayName("At least 3 quests must be loaded")
    void shouldHaveAtLeastThreeQuestsOnStart() {
        List<Quest> quests = questManager.getAllQuests();
        assertTrue(quests.size() >= 3, "At least 3 default quests were expected");
    }

    @Test
    @DisplayName("Search by ID: Quest 'treasure-hunt' must have a valid name")
    void getQuestById_ShouldReturnCorrectTreasureHuntTitle() {
        Quest quest = questManager.getQuest("treasure-hunt");
        assertNotNull(quest, "The quest 'treasure hunt' was not found");
        assertEquals("Поиск сокровищ в замке", quest.getTitle());
    }


    @Test
    @DisplayName("Search by ID: Quest 'space-adventure' must have the genre Survival")
    void getQuestById_ShouldReturnCorrectSpaceAdventureGenre() {
        Quest quest = questManager.getQuest("space-adventure");
        assertNotNull(quest, "The quest 'space-adventure' was not found");
        assertEquals("Выживание", quest.getGenre());
    }

    @Test
    @DisplayName("Search by ID: The 'detective-story' quest must have a difficulty of 5")
    void getQuestById_ShouldReturnCorrectDetectiveDifficulty() {
        Quest quest = questManager.getQuest("detective-story");
        assertNotNull(quest, "The quest 'detective-story' was not found");
        assertEquals(5, quest.getDifficultyLevel());
    }

    @Test
    @DisplayName("Search by ID: Requesting a non-existent quest should return null")
    void getQuest_ShouldReturnNull_WhenIdDoesNotExist() {
        Quest nonExistent = questManager.getQuest("non-existent");
        assertNull(nonExistent, "Should return null if the quest is not in the database.");
    }

    @Test
    @DisplayName("Quest structure: Checking the correctness of the starting question and the completeness of the list of questions")
    void treasureHunt_ShouldHaveValidStructureAndQuestions() {
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
    void testSpaceAdventureQuestQuestions() {
        Quest quest = questManager.getQuest("space-adventure");

        assertNotNull(quest, "Quest 'space-adventure' not found in manager");

        assertAll("Checking the starting question", () -> {
            Question startQuestion = quest.getStartQuestion();
            assertNotNull(startQuestion, "There is no starting question");
            assertEquals("Ваш космический корабль потерпел крушение на неизвестной планете. Что делать?",
                    startQuestion.getText(),
                    "The quest intro text does not match the expected one");
        });
    }

    @Test
    @DisplayName("Detective Story: Checking the Starting Question Topic")
    void testDetectiveStoryQuestQuestions() {
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
    void testTreasureHuntVictoryConditions() {
        Quest quest = questManager.getQuest("treasure-hunt");

        assertNotNull(quest, "The quest 'treasure-hunt' must exist");

        assertAll("Checking the logic of quest completion", () -> {
            int[] victoryIds = {7, 8, 9, 12};
            for (int id : victoryIds) {
                assertTrue(quest.isVictory(id), "Question " + id + " must be victorious");
                String msg = quest.getVictoryMessage(id);
                assertNotNull(msg, "Winning Message for ID " + id + " should not be null");
                assertFalse(msg.isBlank(), "Winning Message for ID " + id + " should not be empty");
            }
        }, () -> {
            int[] defeatIds = {10, 11};
            for (int id : defeatIds) {
                assertFalse(quest.isVictory(id), "Question " + id + " should not be victorious");
                String msg = quest.getDefeatMessage(id);
                assertNotNull(msg, "Defeat message for ID " + id + " should not be null");
                assertFalse(msg.isBlank(), "Defeat message for ID " + id + " should not be empty");
            }
        });
    }

    @Test
    @DisplayName("Space Adventure: Checking Victory Conditions, Defeat Conditions, and Ending Message Texts")
    void testSpaceAdventureVictoryConditions() {
        Quest quest = questManager.getQuest("space-adventure");

        assertNotNull(quest, "The quest 'space-adventure' must exist");

        assertAll("Checking the logic of quest completion", () -> {
            int[] victoryIds = {7, 9};
            for (int id : victoryIds) {
                assertTrue(quest.isVictory(id), "Question " + id + " must be victorious");
                String msg = quest.getVictoryMessage(id);
                assertNotNull(msg, "Winning Message for ID " + id + " should not be null");
                assertFalse(msg.isBlank(), "Winning Message for ID " + id + " should not be empty");
            }
        }, () -> {
            int[] defeatIds = {6, 8, 10};
            for (int id : defeatIds) {
                assertFalse(quest.isVictory(id), "Question " + id + " should not be victorious");
                String msg = quest.getDefeatMessage(id);
                assertNotNull(msg, "Defeat message for ID " + id + " should not be null");
                assertFalse(msg.isBlank(), "Defeat message for ID " + id + " should not be empty");
            }
        }, () -> {
            assertAll("Checking the exact texts of messages",
                    () -> assertEquals("Вы успешно вернулись на Землю и стали героем!",
                            quest.getVictoryMessage(7), "Текст победы (ID 7) не совпадает"),
                    () -> assertEquals("Корабль уничтожен. Вы погибли.",
                            quest.getDefeatMessage(6), "Текст поражения (ID 6) не совпадает")
            );
        });
    }

    @Test
    @DisplayName("Detective Story: Checking Victory Conditions and Defeat Conditions")
    void testDetectiveStoryVictoryConditions() {
        Quest quest = questManager.getQuest("detective-story");

        assertNotNull(quest, "The quest 'detective-story' must exist");

        assertAll("Checking the logic of quest completion", () -> {
            int[] victoryIds = {7, 9, 12};
            for (int id : victoryIds) {
                assertTrue(quest.isVictory(id), "Question " + id + " must be victorious");
                String msg = quest.getVictoryMessage(id);
                assertNotNull(msg, "Winning Message for ID " + id + " should not be null");
                assertFalse(msg.isBlank(), "Winning Message for ID " + id + " should not be empty");
            }
        }, () -> {
            int[] defeatIds = {8, 10, 11};
            for (int id : defeatIds) {
                assertFalse(quest.isVictory(id), "Question " + id + " should not be victorious");
                String msg = quest.getDefeatMessage(id);
                assertNotNull(msg, "Defeat message for ID " + id + " should not be null");
                assertFalse(msg.isBlank(), "Defeat message for ID " + id + " should not be empty");
            }
        });
    }

    @Test
    @DisplayName("Statistics: Checking the quest launch counter")
    void testQuestStats() {
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
                        "Another quest counter ('" + otherQuestId + "') should not change"));
    }

    @Test
    @DisplayName("Statistics: Obtain a full walkthrough map for all quests")
    void testGetAllQuestStats() {
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

    @Test
    @DisplayName("Filtering: Search quests by genre and check the correctness of empty results")
    void testFilterQuestsByGenre() {
        assertAll("Testing filtering by genre",
                () -> {
                    List<Quest> horrorQuests = questManager.getQuestsByGenre("Хоррор/Приключения");
                    assertNotNull(horrorQuests, "The list of horror quests should not be null");
                    assertTrue(horrorQuests.stream().anyMatch(q -> "treasure-hunt".equals(q.getId())),
                            "The 'treasure hunt' quest must be in the Horror genre");
                },
                () -> {
                    List<Quest> survivalQuest = questManager.getQuestsByGenre("Выживание");
                    assertNotNull(survivalQuest, "The Survival quest list should not be null");
                    assertFalse(survivalQuest.isEmpty(), "At least one Survival quest must be found");
                },
                () -> {
                    List<Quest> emptyList = questManager.getQuestsByGenre("Non-existent");
                    assertNotNull(emptyList, "For a non-existent genre, an empty list should be returned, not null");
                    assertTrue(emptyList.isEmpty(), "The list for a non-existent genre must be empty");
                }
        );
    }

    @Test
    @DisplayName("Filtering: Search quests by difficulty and check the correctness of empty results")
    void testFilterQuestsByDifficulty() {
        assertAll("Testing filtering by complexity",
                () -> {
                    List<Quest> difficulty3Quests = questManager.getQuestsByDifficulty(3);
                    assertNotNull(difficulty3Quests, "Difficulty 3 quests list should not be null");
                    assertFalse(difficulty3Quests.isEmpty(), "Should find difficulty 3 quests");
                    assertTrue(difficulty3Quests.stream().anyMatch(q -> "treasure-hunt".equals(q.getId())),
                            "Treasure hunt should be difficulty 3");
                },
                () -> {
                    List<Quest> difficulty5Quests = questManager.getQuestsByDifficulty(5);
                    assertNotNull(difficulty5Quests, "Difficulty 5 quests list should not be null");
                    assertFalse(difficulty5Quests.isEmpty(), "Should find difficulty 5 quests");
                },
                () -> {
                    List<Quest> nonExistentDifficulty = questManager.getQuestsByDifficulty(10);
                    assertNotNull(nonExistentDifficulty, "Non-existent difficulty should return empty list");
                    assertTrue(nonExistentDifficulty.isEmpty(), "Non-existent difficulty should return empty list");
                }
        );
    }

    @Test
    @DisplayName("GameState: Checking default initialization and state change logic")
    void testGameState() {
        GameState gameState = new GameState();

        assertAll("Initial state of a new object",
                () -> assertEquals(1, gameState.getCurrentQuestionId(), "The starting question should be 1"),
                () -> assertFalse(gameState.isGameOver(), "The game shouldn't be over"),
                () -> assertFalse(gameState.isVictory(), "Victory flag must be false"),
                () -> assertEquals(0, gameState.getGamesPlayed(), "The game counter should be 0"),
                () -> assertEquals(0, gameState.getWins(), "The number of wins must be 0"),
                () -> assertEquals(0, gameState.getLosses(), "The number of defeats must be 0")
        );

        assertAll("Changing state through setters and methods",
                () -> {
                    gameState.setPlayerName("TestPlayer");
                    assertEquals("TestPlayer", gameState.getPlayerName(),
                            "The player's name was set incorrectly.");
                },
                () -> {
                    gameState.setCurrentQuestId("test-quest");
                    assertEquals("test-quest", gameState.getCurrentQuestId(),
                            "The quest ID was set incorrectly.");
                },
                () -> {
                    gameState.addWin();
                    gameState.addWin();
                    assertEquals(2, gameState.getWins(),
                            "The addWin() method should increase the counter to 2");
                },
                () -> {
                    gameState.addLoss();
                    assertEquals(1, gameState.getLosses(),
                            "The addLoss() method should increase the counter to 1");
                },
                () -> {
                    gameState.incrementGamesPlayed();
                    assertEquals(1, gameState.getGamesPlayed(),
                            "The game counter should be 1");
                },
                () -> {
                    gameState.setGameOver(true);
                    gameState.setVictory(true);
                    assertAll("Final flags",
                            () -> assertTrue(gameState.isGameOver(), "The GameOver flag was not set"),
                            () -> assertTrue(gameState.isVictory(), "The Victory flag was not set"));
                }
        );
    }

    @Test
    @DisplayName("Question Model: Testing the creation of regular and final questions")
    void testQuestionModel() {
        assertAll("Comprehensive Model Validation Question",
                () -> {
                    Question question = new Question(1, "Test question",
                            "Option A", "Option B", 2, 3);

                    assertAll("Basic fields and default values",
                            () -> assertEquals(1, question.getId(), "ID must be 1"),
                            () -> assertEquals("Test question", question.getText(), "The text does not match"),
                            () -> assertEquals("Option A", question.getOption1(), "Option A does not match"),
                            () -> assertEquals("Option B", question.getOption2(), "Option B does not match"),
                            () -> assertEquals(2, question.getNextIdOption1(), "The transition for option 1 is incorrect."),
                            () -> assertEquals(3, question.getNextIdOption2(), "The transition for option 2 is incorrect."),
                            () -> assertFalse(question.isFinal(), "The question should not be final by default"),
                            () -> assertNull(question.getVictoryMessage(), "The winning message must be null"),
                            () -> assertNull(question.getDefeatMessage(), "The defeat message must be null")
                    );
                },
                () -> {
                    Question finalQuestion = new Question(2, "Final", "Restart",
                            "Exit", 1, 1, true,
                            "You win!", "You lose!", 5);

                    assertAll("Specific fields of the final question",
                            () -> assertTrue(finalQuestion.isFinal(),
                                    "The final question flag must be set."),
                            () -> assertEquals("You win!", finalQuestion.getVictoryMessage(),
                                    "The victory text does not match"),
                            () -> assertEquals("You lose!", finalQuestion.getDefeatMessage(),
                                    "The text of the defeat does not match")
                    );
                }
        );
    }

    @Test
    @DisplayName("Treasure Hunt: Checking the successful completion of the quest (Path to Victory)")
    void testTreasureHuntGamePathToVictory() {
        Quest quest = questManager.getQuest("treasure-hunt");
        assertNotNull(quest, "The quest must exist");

        int step1 = quest.getQuestions().get(1).getNextIdOption2();
        int step2 = quest.getQuestions().get(step1).getNextIdOption1();
        int finalStep = quest.getQuestions().get(step2).getNextIdOption2();

        assertAll("Testing the chain of decisions leading to victory",
                () -> assertEquals(3, step1, "The first choice should lead to question 3"),
                () -> assertEquals(6, step2, "The second choice should lead to question 6"),
                () -> assertEquals(12, finalStep, "The third choice should lead to the final 12"),
                () -> {
                    Question finalQ = quest.getQuestions().get(finalStep);
                    assertAll("Checking the final state",
                            () -> assertTrue(finalQ.isFinal(), "Question 12 should be marked as final"),
                            () -> assertTrue(quest.isVictory(finalStep), "Question 12 should be a winning one")
                    );
                }
        );
    }

    @Test
    @DisplayName("Treasure Hunt: Checking the Path to Defeat (Trap)")
    void testTreasureHuntGamePathToDefeat() {
        Quest quest = questManager.getQuest("treasure-hunt");
        assertNotNull(quest, "The quest must be downloaded");

        int step1 = quest.getQuestions().get(1).getNextIdOption1();
        int step2 = quest.getQuestions().get(step1).getNextIdOption2();
        int finalStep = quest.getQuestions().get(step2).getNextIdOption2();

        assertAll("Testing the chain of decisions that leads to failure",
                () -> assertEquals(2, step1, "The first choice should lead to question 2"),
                () -> assertEquals(5, step2, "The second choice should lead to question 5"),
                () -> assertEquals(10, finalStep, "The third choice should lead to the final 10"),
                () -> {
                    Question finalQ = quest.getQuestions().get(finalStep);
                    assertAll("Checking the loss status",
                            () -> assertTrue(finalQ.isFinal(), "Question 10 should be the final one."),
                            () -> assertFalse(quest.isVictory(finalStep), "Question 10 should not be a winning question.")
                    );
                }
        );
    }

    @Test
    @DisplayName("Space Adventure: Checking the key game routes (Victory and Defeat)")
    void testSpaceAdventureGamePaths() {
        Quest quest = questManager.getQuest("space-adventure");
        assertNotNull(quest, "The quest 'space-adventure' must exist");
        var questions = quest.getQuestions();

        assertAll("Checking the completion of the quest",
                () -> {
                    int vId = questions.get(1).getNextIdOption1();
                    vId = questions.get(vId).getNextIdOption1();
                    vId = questions.get(vId).getNextIdOption1();

                    assertTrue(quest.isVictory(vId), "The route (1-opt1, opt1, opt1) must lead to victory");
                },
                () -> {
                    int dId = questions.get(1).getNextIdOption2();
                    dId = questions.get(dId).getNextIdOption2();

                    assertFalse(quest.isVictory(dId), "The route (1-opt2, opt2) must not be winning");
                }
        );
    }
}
