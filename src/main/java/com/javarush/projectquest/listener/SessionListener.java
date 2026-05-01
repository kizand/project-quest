package com.javarush.projectquest.listener;

import com.javarush.projectquest.command.CommandFactory;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

@WebListener
public class SessionListener implements HttpSessionListener, ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(SessionListener.class);

    private static AtomicInteger activeSessions = new AtomicInteger();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        CommandFactory commandFactory = CommandFactory.getInstance();
        sce.getServletContext().setAttribute("commandFactory", commandFactory);
        logger.info("Application context initialized. CommandFactory placed in ServletContext.");
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        activeSessions.incrementAndGet();
        logger.info("Session created. Active sessions: {}", activeSessions);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        activeSessions.decrementAndGet();
        logger.info("Session destroyed. Active sessions: {}", activeSessions);
    }

    public int getActiveSessions() {
        return activeSessions.get();
    }
}
