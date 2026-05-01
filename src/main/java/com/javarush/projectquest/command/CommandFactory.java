package com.javarush.projectquest.command;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandFactory {

    private static final CommandFactory INSTANCE = new CommandFactory();
    private final Map<String, Command> commands = new HashMap<>();

    private CommandFactory() {
        initCommands();
    }

    public static synchronized CommandFactory getInstance() {
        return INSTANCE;
    }

    private void initCommands() {
        Reflections reflections = new Reflections("com.javarush.projectquest.command");
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(WebCommand.class);
        for (Class<?> clazz : annotatedClasses) {
            try {
                WebCommand annotation = clazz.getAnnotation(WebCommand.class);
                Command command = (Command) clazz.getDeclaredConstructor().newInstance();
                commands.put(annotation.value().toLowerCase(), command);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Command getCommand(String commandName) {
        if (commandName == null || commandName.isEmpty()) {
            return commands.get("start");
        }

        Command command = commands.get(commandName.toLowerCase());
        return (command != null) ? command : new UnknownCommand();
    }
}
