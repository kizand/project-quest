package com.javarush.projectquest.controller;

import com.javarush.projectquest.command.Command;
import com.javarush.projectquest.command.CommandFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/")
public class FrontControllerServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(FrontControllerServlet.class);

    private CommandFactory commandFactory;

    @Override
    public void init() throws ServletException {
        this.commandFactory = (CommandFactory) getServletContext().getAttribute("commandFactory");
        if (this.commandFactory == null) {
            throw new ServletException("CommandFactory not found in ServletContext");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = getCommandPath(request);
        logger.debug("Processing request for path: {}", path);

        String uri = request.getRequestURI();
        if (uri.contains("/images/")) {
            request.getServletContext().getNamedDispatcher("default").forward(request, response);
            return;
        }

        try {
            Command command = commandFactory.getCommand(path);
            String view = command.execute(request, response);

            if (view.startsWith("redirect:")) {
                String redirectPath = view.substring("redirect:".length());
                String contextPath = request.getContextPath();

                if (!redirectPath.startsWith("/")) {
                    redirectPath = "/" + redirectPath;
                }

                String finalUrl = response.encodeRedirectURL(contextPath + redirectPath);
                finalUrl = finalUrl.replace("//", "/");
                response.sendRedirect(finalUrl);
            } else {
                request.getRequestDispatcher(view).forward(request, response);
            }
        } catch (Exception e) {
            logger.error("Error executing command for path: {}", path, e);
            throw new ServletException("Error executing command", e);
        }
    }

    private String getCommandPath(HttpServletRequest request) {
        String path = request.getServletPath();
        path = path.replaceFirst("^/", "").replaceAll("\\.\\w+$", "");
        if (path.isEmpty() || "favicon.ico".equals(path)) {
            return "start";
        }
        return path;
    }
}
