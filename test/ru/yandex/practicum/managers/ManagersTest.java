package ru.yandex.practicum.managers;

import org.junit.jupiter.api.*;

class ManagersTest {

    private static TaskManager taskManager;
    private static HistoryManager historyManager;

    @BeforeAll
    static void createManagers() {
        taskManager = Managers.getDefault(historyManager);
        historyManager = Managers.getDefaultHistory();
    }


    @Test
    void taskManagerIsCreatedAndReadyToUse() {
        Assertions.assertNotNull(taskManager, "Object TaskManager didn't create!");
        Assertions.assertInstanceOf(InMemoryTaskManager.class, taskManager, "Created taskManager" +
                "is not an instance of the InMemoryTaskManager class.");
        Assertions.assertEquals(0, historyManager.getHistory().size(), "TaskManager is not " +
                "ready. Method getHistory() was not called.");
    }

    @Test
    void historyManagerIsCreatedAndReadyToUse() {
        Assertions.assertNotNull(historyManager, "Object HistoryManager didn't create!");
        Assertions.assertInstanceOf(InMemoryHistoryManager.class, historyManager, "Created " +
                "historyManager is not an instance of the InMemoryHistoryManager class.");
        Assertions.assertEquals(0, historyManager.getHistory().size(), "HistoryManager is " +
                "not ready. Method getHistory() was not called");
    }
}