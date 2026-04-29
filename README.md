# project-quest - это небольшое учебное Java-веб-приложение на основе сервлетов.

# Стек технологий (Tech Stack)
* **Backend:** Java 25, Jakarta Servlet API
* **Frontend:** JSP, JSTL, CSS, JS
* **Инструменты:** Maven, Tomcat 10.1.52
* **Тестирование и логирование:** JUnit 5, SLF4J, Jackson

# Структура проекта
```
src/
├── main/
│   ├── java/com/javarush/projectquest/
│   │   ├── command/                # Реализация паттерна Command для логики игры
│   │   ├── controller/             # Веб-слой: обработка HTTP-запросов (Servlet)
│   │   ├── filter/                 # Фильтрация запросов и обработка статических ресурсов (CSS, JS, изображения)
│   │   ├── listener/               # Управление жизненным циклом сессий и инициализация приложения
│   │   ├── quests/                 # Игровая логика: сценарии, вопросы и ответы
│   └── resources/
│       └── properties/             # Конфигурационные файлы проекта
└── test/                           # Юнит-тестирование бизнес-логики
```
# Endpoints
```
http://localhost:8080/
http://localhost:8080/selectQuest
http://localhost:8080/game
http://localhost:8080/game?result=final
```