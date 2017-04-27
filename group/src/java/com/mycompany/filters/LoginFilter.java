/*
 * Created by Patrick Gatewood on 2017.04.24  * 
 * Copyright © 2017 Patrick Gatewood. All rights reserved. * 
 */
package com.mycompany.filters;

import java.io.IOException;
import javax.faces.context.FacesContext;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * The purpose of this filter is to prevent users who are not logged in from
 * accessing confidential website areas.
 */
public class LoginFilter implements Filter {

    /**
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * Checks HTTP requests and responses and restricts access to the main site
     * to logged-in users only 
     * 
     * @param req the HTTP request sent
     * @param res the HTTP response received
     * @param chain
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        // Cast parameters to usable types
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Path to the webpage requested
        String path = request.getRequestURI().substring(request.getContextPath().length());
        System.out.println(path);

        HttpSession session = request.getSession(false);

        /* Restrict access to members-only content, but allow 
         the homepage, SignIn, and resources to be loaded */
        if (path.equals("/")
                || path.endsWith(".css.xhtml")
                || path.endsWith(".js")
                || path.endsWith("index.xhtml")
                || path.endsWith("index.xhtml?faces-redirect=true")
                || path.endsWith("CreateAccount.xhtml")
                || path.endsWith("SignIn.xhtml")
                || path.contains("/resources/")) {
            chain.doFilter(request, response);
        } else if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn.xhtml");
        } else {
            chain.doFilter(request, response);
        }
    }
  
    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
    }

}
