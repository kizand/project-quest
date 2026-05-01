package com.javarush.projectquest.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Pattern;

@WebFilter("/*")
public class StaticResourceFilter implements Filter {

    private static final Pattern STATIC_RESOURCES =
            Pattern.compile("^/(css|js|images|fonts)/.*|.*\\.(css|js|jpg|png|gif|ico|svg|woff2?)$",
                    Pattern.CASE_INSENSITIVE);


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getServletPath();

        if (STATIC_RESOURCES.matcher(path).matches()) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
